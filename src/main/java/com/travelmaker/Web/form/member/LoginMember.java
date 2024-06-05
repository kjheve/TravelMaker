package com.travelmaker.Web.form.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor  // 모든 멤버필드를 매개변수로 갖는 생성자를 만들어줌.
public class LoginMember{
  private Long managementId;
  private String memberId;      //회원 로그인 아이디
  private String nickname;
  private String codeId;  // 일반, 관리자
}
