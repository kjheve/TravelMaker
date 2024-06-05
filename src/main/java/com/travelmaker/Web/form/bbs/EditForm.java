package com.travelmaker.Web.form.bbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EditForm {
  private Long bbsId;
  private Long managementId;
  private String title;
  @JsonProperty("bContent")
  private String bContent;
  private Long planId;
}
