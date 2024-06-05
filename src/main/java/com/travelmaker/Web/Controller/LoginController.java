package com.travelmaker.Web.Controller;

import com.travelmaker.domain.entity.Member;
import com.travelmaker.domain.member.svc.MemberSVC;
import com.travelmaker.Web.form.member.LoginForm;
import com.travelmaker.Web.form.member.LoginMember;
import com.travelmaker.Web.form.member.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller // Controller 역할을 하는 클래스
@RequiredArgsConstructor
public class LoginController {

  private final MemberSVC memberSVC;

  // 메인메뉴
  @GetMapping("/selectMenu")
  public String selectMenu() {
    return "selectMenu";  // templates 폴더의 selectMenu.html 파일을 반환
  }
  
  //로그인 양식
  @GetMapping("/login")
  public String loginForm() {

    return "login";
  }

  // 로그인 처리
  @PostMapping("/login")     // /login?redirectUrl=사용자가요청한url
  public String login(LoginForm loginForm, HttpServletRequest request,
                      @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                      RedirectAttributes redirectAttributes) {  // RedirectAttributes 추가
    log.info("loginForm={}", loginForm);

    // 회원 아이디 존재 유무 체크
    if (memberSVC.existMemberId(loginForm.getMemberId())) {
      Optional<Member> optionalMember = memberSVC.findByIdAndPw(loginForm.getMemberId(), loginForm.getPw());

      // 회원 정보가 존재하면 세션에 저장
      if (optionalMember.isPresent()) {
        HttpSession session = request.getSession(true);  // 세션 생성
        Member member = optionalMember.get();
        LoginMember loginMember = new LoginMember(
            member.getManagementId(), member.getMemberId(),
            member.getNickname(), member.getCodeId());
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        // 관리자 아이디일 경우 /admin으로 리디렉션
        if ("M0102".equals(loginMember.getCodeId())) {
          return "redirect:/admin";
        }
      } else {
        // 비밀번호 불일치
        redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
        return "redirect:/login";
      }
    } else {
      // 아이디가 존재하지 않음
      redirectAttributes.addFlashAttribute("message", "존재하지 않는 사용자 아이디입니다.");
      return "redirect:/login";
    }

    return redirectUrl != null ? "redirect:" + redirectUrl : "redirect:/selectMenu";
  }

  //로그아웃
  @ResponseBody
  @PostMapping("/logout")
  public String logout(HttpServletRequest request) {

    String flag = "NOK";
    //세션이 있으면 가져오고 없으면 생성하지 않는다.
    HttpSession session = request.getSession(false);

    //세션 제거
    if(session !=null ){
      session.invalidate();
      flag ="OK";
    }
    return flag;
  }
}
