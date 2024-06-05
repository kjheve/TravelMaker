package com.travelmaker.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
public class TrvlSpot {
  private Long trvlCd;    // TRVL_CD
  private String trvlNm;    // TRVL_NM
  private String ragionNm;       // REGION_CD
  private String codeNm; // TRSP_CLA
  private String trvlAddr;   // TRVL_ADDR
  private String img;      // IMG
  private String img2;      // IMG2
  private Double trvlX;      // TRVL_X
  private Double trvlY;      // TRVL_Y

  @Transient
  private Double distance;
}
