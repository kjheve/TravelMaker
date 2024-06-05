package com.travelmaker.Web.form.travelMaker;


import lombok.Data;


@Data
public class TrvlSpotForm {

  private Long reqPage;
  private Long reqCnt;
  private String ragion;
  private String keyword;
  private String tagValue;
  private Double trvlX;
  private Double trvlY;
}
