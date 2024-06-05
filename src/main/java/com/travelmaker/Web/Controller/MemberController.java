package com.travelmaker.Web.Controller;

import com.travelmaker.Web.form.member.DetailForm;
import com.travelmaker.Web.form.member.UpdateForm;
import com.travelmaker.Web.req.member.ReqFindPwd;
import com.travelmaker.Web.res.ApiResponse;
import com.travelmaker.Web.res.ResCode;
import com.travelmaker.domain.common.mail.MailService;
import com.travelmaker.domain.entity.Member;
import com.travelmaker.domain.member.svc.MemberSVC;
import com.travelmaker.Web.form.member.JoinForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/members")   // http://localhost:9080/members
@RequiredArgsConstructor
public class MemberController {

  private final MemberSVC memberSVC;
  private final MailService mailService; // MailService 주입
  private  final LoginController loginController;

//  // 서비스 객체를 생성자로 주입받는 코드
//  public MemberController(MemberSVC memberSVC) {
//    this.memberSVC = memberSVC;
//  }

  // 회원가입 페이지
  @GetMapping("/join")      // http://localhost:9080/members/join
  public String joinForm() {
    return "join";
  }

  @PostMapping("/join")
  public String join(@ModelAttribute JoinForm joinForm) {
    log.info("join={}", joinForm);

    // codeId 기본 값 설정
    joinForm.setCodeId("M0101");

    // 회원 아이디를 설정
    String memberId = joinForm.getMemberId();

    // Member 객체 생성
    Member member = new Member();
    BeanUtils.copyProperties(joinForm, member);

    // 회원가입 서비스 호출
    Long managementId = memberSVC.joinMember(member);

    return (managementId != null) ? "redirect:/login" : "redirect:/members/join";
  }

  //------------------------------------------------------------------------------------
  // ** 아이디 찾기
  // 1) '아이디찾기' 폼 보여주기 ex) 이메일 입력
  @GetMapping("/findId")
  public String showFindIdForm() {

    return "/member/findIdForm"; // 템플릿 이름
  }

  // 2)'아이디찾기' 처리. ex) "고객님의 아이디는 ~"
  @PostMapping("/findId")
  public String findId(@RequestParam("email") String email, Model model) {
    Optional<Member> findId = memberSVC.findIdByEmail(email);

    if (findId.isPresent()) {
      model.addAttribute("memberId", findId.get().getMemberId());
      return "member/findId"; // 아이디를 보여주는 템플릿
    } else {
      model.addAttribute("error", "존재하지 않는 이메일입니다. ");
      return "/member/findIdForm"; // 다시 폼으로 돌아감
    }
  }

//  -------------------------------------------------------------------------------------
// ** 비밀번호 찾기
//       1) 비밀번호 찾기 폼 보여주기 ex) 아이디와 이메일 입력
//  @ResponseBody
  @GetMapping("/findPw")
  public String findPwdForm() {

    return "/member/findPwForm";
  }

  //      2) 비밀번호 찾기 플로우
  @ResponseBody
  @PostMapping("/findPwd")
  public ApiResponse<?> findPwd(@RequestBody ReqFindPwd reqFindPwd) {

//    ApiResponse<?> res = null;
    log.info("memberId={}", reqFindPwd.getMemberId());
    log.info("email={}", reqFindPwd.getEmail());

    boolean hasPasswd = memberSVC.hasPasswd(reqFindPwd.getMemberId(), reqFindPwd.getEmail());
    //상단 메서드 호출 -> 해당 아이디와 비밀번호 존재확인

    if (hasPasswd) {
      //    2-1)임시번호 생성 6자리 랜덤 생성
      String tmpPwd = UUID.randomUUID().toString().substring(0, 6); //a98ea7

      log.info("Generated temporary password: {}", tmpPwd);

      //    2-2 )회원 비밀번호를 임시 비밀번호로 변경
      int updateCount = memberSVC.changePasswd(reqFindPwd.getMemberId(), tmpPwd);

      log.info("Password update count: {}", updateCount);

      //    2-3) 메일 발송
      StringBuffer str = new StringBuffer();
      str.append("<html>");
      str.append("<p><b>");
      str.append(tmpPwd);
      str.append("</b></p>");
      str.append("<p><a href='http://localhost:9080/login'>로그인 후 비밀번호를 변경하시기 바랍니다.</a></p>");
      str.append("</html>");

      mailService.sendMail(reqFindPwd.getEmail(), "트래블메이커 임시비밀번호", str.toString());
      return ApiResponse.createApiResponse(ResCode.EXIST.getCode(), ResCode.EXIST.name(), null);
    }

    return ApiResponse.createApiResponse(ResCode.NONE.getCode(), ResCode.NONE.name(), null);
  }



  //1. 회원정보 조회
  @GetMapping("/{memberId}")
  public String detail(@PathVariable("memberId") String memberId, Model model) {
    log.info("memberId={}", memberId);
    Optional<Member> optionalMember = memberSVC.checkAccount(memberId);
    if (optionalMember.isPresent()) {
      Member member = optionalMember.get();
      DetailForm detailForm = new DetailForm();

      detailForm.setMemberId(member.getMemberId());
      detailForm.setEmail(member.getEmail());
      detailForm.setTel(member.getTel());
      detailForm.setNickname(member.getNickname());
      detailForm.setGender(member.getGender());
      detailForm.setBirthday(member.getBirthday());  // setBirthday에서 날짜 형식 변환 처리
      detailForm.setAddress(member.getAddress());

      log.info("detailForm={}", detailForm);
      model.addAttribute("detailForm", detailForm);

      return "mypage/editMember/detailForm";
    } else {
      model.addAttribute("error", "회원 정보를 불러오는 데 실패했습니다.");
      return "redirect:/";
    }
  }

  // 2. 회원정보 수정 폼 보여주기
  @GetMapping("/{memberId}/edit")
  public String updateForm(@PathVariable("memberId") String memberId, Model model) {

    Optional<Member> optionalMember = memberSVC.checkAccount(memberId);
    Member member = null;

    if (optionalMember.isPresent()) {
      member = optionalMember.get();

      UpdateForm updateForm = new UpdateForm();

      updateForm.setMemberId(member.getMemberId());
      updateForm.setEmail(member.getEmail());
      updateForm.setTel(member.getTel());
      updateForm.setNickname(member.getNickname());
      updateForm.setGender(member.getGender());
      updateForm.setBirthday(member.getBirthday());
      updateForm.setAddress(member.getAddress());

      log.info("updateForm={}", updateForm);
      model.addAttribute("updateForm", updateForm);

      return "mypage/editMember/updateForm";
    } else {
      model.addAttribute("error", "회원 정보가 존재하지 않습니다.");
      return "redirect:/";
    }
  }

  // 3. 회원 수정 처리
  @PatchMapping("/{memberId}/edit")
  public String updateAccount(
      @PathVariable("memberId") String memberId,
      @Valid @ModelAttribute UpdateForm updateForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    // 필드 어노테이션 기반 검증 - 오류가 있으면 다시 리다이렉트
    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "mypage/editMember/updateForm";
    }

    Member member = new Member();

    member.setMemberId(updateForm.getMemberId());
    member.setEmail(updateForm.getEmail());
    member.setTel(updateForm.getTel());
    member.setNickname(updateForm.getNickname());
    member.setGender(updateForm.getGender());
    member.setAddress(updateForm.getAddress());
    member.setBirthday(updateForm.getBirthday());


    log.info("updateForm={}", updateForm);

    // 4. 수정처리
    int rows = memberSVC.modifyForm(memberId, member);

    redirectAttributes.addAttribute("memberId", memberId);
    return "redirect:/members/{memberId}";
  }

  //--------------------------------------------------------------------------------------------------------------
// 회원탈퇴
  @PostMapping("/{memberId}/del")
  public String deleteMember(@PathVariable("memberId") String memberId, RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {
    log.info("Attempting to delete memberId={}", memberId);

    int deletedRowCnt = memberSVC.deleteMember(memberId);
    if (deletedRowCnt > 0) {
      log.info("Member deletion successful for memberId={}", memberId);
      redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.");
        // 회원탈퇴시 세션끊기
        loginController.logout(request);
//      return "redirect:/findMember/findId"; // 수정된 부분
      return "redirect:/selectMenu"; //메인화면으로 이동
    } else {
      log.warn("No member found to delete for memberId={}", memberId);
      redirectAttributes.addFlashAttribute("error", "회원 탈퇴 처리에 실패하였습니다.");
      return "redirect:/login"; // 수정된 부분
    }
  }

  // --------------------------------------------------------------------------------------------------------------
//  //비밀번호 변경 폼
  @GetMapping("/{memberId}/changePw")
  public String changePwdForm(@PathVariable("memberId") String memberId, Model model) {
    model.addAttribute("memberId", memberId);
    return "mypage/editMember/changePwForm"; // 비밀번호 변경 폼 페이지 경로
  }

  // 비밀번호 변경 처리
  @PostMapping("/{memberId}/changePw")
  public String changePassword(@PathVariable("memberId") String memberId,
                               @RequestParam("newPassword") String newPassword,
                               @RequestParam("newPasswordConfirm") String newPasswordConfirm,
                               RedirectAttributes redirectAttributes) {
    if (!newPassword.equals(newPasswordConfirm)) {
      redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
      return "redirect:/members/{memberId}/changePw";
    }

    int updatedRows = memberSVC.changePasswd(memberId, newPassword);
    log.info("updatedRows={}", updatedRows);
    if (updatedRows > 0) {
      redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
    } else {
      redirectAttributes.addFlashAttribute("error", "비밀번호 변경에 실패하였습니다.");
    }
    return "redirect:/members/{memberId}";
  }


}
