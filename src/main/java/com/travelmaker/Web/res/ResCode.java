package com.travelmaker.Web.res;

import lombok.Getter;

@Getter
public enum ResCode {
  OK("00"), FAIL("01"),EXIST("21"),NONE("22"),
  EFFECTIVENESS_ERROR("23") , ETC("99")
  ;
  private final String code;
  ResCode(String code){
    this.code = code;
  }


}

