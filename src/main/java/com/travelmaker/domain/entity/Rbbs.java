package com.travelmaker.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Rbbs {
  private Long rbbsId;           // RBBS_ID	NUMBER(10,0)
  private Long bbsId;            // BBS_ID	NUMBER(10,0)
  private Long managementId;     // MANAGEMENT_ID	NUMBER(10,0)
  private String nickname;       // NICKNAME	VARCHAR2(36 BYTE)
  private LocalDateTime cdate;   // CDATE	TIMESTAMP(6)
  private LocalDateTime udate;   // UDATE	TIMESTAMP(6)
  @JsonProperty("bContent")
  private String bContent;       // BCONTENT	CLOB
}
