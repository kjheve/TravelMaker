package com.travelmaker.Web.form.rbbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EditCommentForm {
  private Long rbbsId;  // RBBS_ID	NUMBER(10,0)
  private Long bbsId;  // BBS_ID	NUMBER(10,0)
  private Long managementId;  // MANAGEMENT_ID	NUMBER(10,0)
  private String nickname;  // NICKNAME	VARCHAR2(36 BYTE)
  @JsonProperty("bContent")
  private String bContent;  // BCONTENT	CLOB
}
