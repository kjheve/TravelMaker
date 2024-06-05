package com.travelmaker.Web.Api;

import com.travelmaker.Web.req.member.ReqEmail;
import com.travelmaker.Web.req.member.ReqExistId;
import com.travelmaker.Web.req.member.ReqNickname;
import com.travelmaker.Web.res.ApiResponse;
import com.travelmaker.Web.res.ResCode;
import com.travelmaker.domain.member.svc.MemberSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ApiMemberController {

  private final MemberSVC memberSVC;

  //회원 중복체크
  @PostMapping("/dupchk")
  public ApiResponse<?> dupchk(@RequestBody ReqExistId reqExistId){
    ApiResponse<?> res = null;
    log.info("reqExitsId={}", reqExistId);
    boolean existMemberId = memberSVC.existMemberId(reqExistId.getMemberId());
    if (existMemberId) {
      res = ApiResponse.createApiResponse(ResCode.EXIST.getCode(), ResCode.EXIST.name(), null);
    }else{
      res = ApiResponse.createApiResponse(ResCode.NONE.getCode(), ResCode.NONE.name(), null);
    }
    return res;
  }

  // 이메일 중복체크
  @PostMapping("/dupchkEmail")
  public ApiResponse<?> dupchkEmail(@RequestBody ReqEmail reqEmail) {
    ApiResponse<?> res;
    log.info("reqEmail={}", reqEmail);
    boolean existEmail = memberSVC.existEmail(reqEmail.getEmail());
    if (existEmail) {
      res = ApiResponse.createApiResponse(ResCode.EXIST.getCode(), ResCode.EXIST.name(), null);
    } else {
      res = ApiResponse.createApiResponse(ResCode.NONE.getCode(), ResCode.NONE.name(), null);
    }
    return res;
  }

  // 닉네임 중복체크
  @PostMapping("/dupchkNickname")
  public ApiResponse<?> dupchkNickname(@RequestBody ReqNickname reqNickname) {
    ApiResponse<?> res;
    log.info("reqNickname={}", reqNickname);
    boolean existNickname = memberSVC.existNickname(reqNickname.getNickname());
    if (existNickname) {
      res = ApiResponse.createApiResponse(ResCode.EXIST.getCode(), ResCode.EXIST.name(), null);
    } else {
      res = ApiResponse.createApiResponse(ResCode.NONE.getCode(), ResCode.NONE.name(), null);
    }
    return res;
  }
}
