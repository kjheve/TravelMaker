package com.travelmaker.Web.form.member;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class DetailForm {
  private String memberId;
  private String email;
  private String tel;
  private String nickname;
  private String gender;
  private String address;
  private String pw;
  private String confirmPw;
  private String birthday;


  // LocalDateTime을 사용하여 날짜와 시간 문자열을 "yyyy-MM-dd"로 포맷
  public void setBirthday(String birthday) {
    try {
      LocalDateTime dateTime = LocalDateTime.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      this.birthday = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    } catch (Exception e) {
      // 파싱에 실패한 경우 원래 문자열을 그대로 사용
      this.birthday = birthday;
    }
  }

}