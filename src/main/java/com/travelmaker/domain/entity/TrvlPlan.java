package com.travelmaker.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrvlPlan {

  private Long pmId;
  private String trvlDay;// PM_ID
  private Long trvlCd;        // TRVL_CD
  private String trvlNm ;     // TRVL_DAY
  private String img ;   // TRVL_DTIME
  private Double trvlX ; // TRAFFIC_TIME
  private Double trvlY;     // DAY_WEEK
}