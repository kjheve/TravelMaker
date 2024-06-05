package com.travelmaker.Web.form.travelMaker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlanFormData {

  @JsonProperty("planTabs")
  private List<PlanTab> planTabs;
  @JsonProperty("plan_id")// 디비에서 시퀀스로
  private Long planId;
  @JsonProperty("management_id")// 플랜일정아이디 받아서 넣기
  private Long managemetId;
  @JsonProperty("ragion_nm")
  private String ragionNm = "";
  @JsonProperty("planDays")
  private String planDays ="";
}
