package com.travelmaker.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Bbs {
  private Long bbsId;            // BBS_ID	NUMBER(10,0)
  private Long managementId;     // MANAGEMENT_ID	NUMBER(10,0)
  private String title;          // TITLE	VARCHAR2(150 BYTE)
  private String codeId;         // CODE_ID	VARCHAR2(6 BYTE)
  private String nickname;       // NICKNAME	VARCHAR2(36 BYTE)
  @JsonProperty("bContent")
  private String bContent;       // BCONTENT	CLOB
  private String status;         // STATUS	CHAR(1 BYTE)
  private Integer hit;           // HIT	NUMBER(10,0)
  private Integer good;          // GOOD	NUMBER(5,0)
  private Integer bad;           // BAD	NUMBER(5,0)
  private Long planId;           // PLAN_ID	NUMBER(20,0)
  private LocalDateTime cdate;   // CDATE	TIMESTAMP(6)
  private LocalDateTime udate;   // UDATE	TIMESTAMP(6)
}
