package com.travelmaker.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrvlLst {
  private Long planId; // PLAN_ID
  private Long managementId; // MANAGEMENT_ID
  private String trvlNm; // TRVL_NM
  private String trvlStd; // TRVL_STD
  private String trvlLsd; // TRVL_LSD
  private String travelDays; // TRAVEL_DAYS
  private LocalDateTime cdate; // CDATE
  private LocalDateTime udate; // UDATE
  private String ragionNm; // RAGION_NM
  private int D_day = 0;
}