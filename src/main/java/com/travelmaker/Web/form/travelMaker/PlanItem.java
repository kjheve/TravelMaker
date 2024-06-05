package com.travelmaker.Web.form.travelMaker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlanItem {

  @JsonProperty("trvl_id")
  private Long trvlId;

  @JsonProperty("trvl_x")
  private Double trvlX;

  @JsonProperty("trvl_y")
  private Double trvlY;

  @JsonProperty("trvl_day")
  private String trvlDay;

  @JsonProperty("trvl_time")//세션에서 관리아이디 받아서 넣기
  private String trvlTime;
  @JsonProperty("trvl_stime")//여행지활동시간
  private String trvlStime;
  @JsonProperty("comment")//여행일 활동시간
  private String comment; // comment넣기

}