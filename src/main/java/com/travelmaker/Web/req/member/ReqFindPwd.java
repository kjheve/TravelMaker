package com.travelmaker.Web.req.member;

import lombok.Data;

@Data
public class ReqFindPwd {
  private String memberId;
  private String email;
}
