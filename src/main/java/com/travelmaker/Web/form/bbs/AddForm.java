package com.travelmaker.Web.form.bbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddForm {
  private Long managementId;     // MANAGEMENT_ID	NUMBER(10,0)
  private String title;          // TITLE	VARCHAR2(150 BYTE)
  private String codeId;         // CODE_ID	VARCHAR2(6 BYTE)
  private String nickname;       // NICKNAME	VARCHAR2(36 BYTE)
  @JsonProperty("bContent")
  private String bContent;       // BCONTENT	CLOB
  private Long planId;           // PLAN_ID	NUMBER(20,0)
}
