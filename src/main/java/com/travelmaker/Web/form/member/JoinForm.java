package com.travelmaker.Web.form.member;

import lombok.Data;

@Data
public class JoinForm {
  private String memberId;
  private String email;
  private String pw;
  private String nickname;
  private String address;
  private String birthday;
  private String tel;
  private String gender;
  private String codeId;
}
