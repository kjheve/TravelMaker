package com.travelmaker.Web.form.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class UpdateForm {

  @NotBlank
  private String memberId;       // 변경 불가
  @Email
  private String email;          // 변경 불가
  @Pattern(regexp = "^\\d{1,11}$", message = "최대 11자리 숫자로만 입력해주세요.")
  private String tel;
  @NotNull
  private String nickname;
  @Pattern(regexp = "남|여", message = "성별은 '남' 또는 '여' 이어야 합니다.")
  private String gender;
  private String birthday;
  private String address;
  private String pw;
  private String confirmPw;

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

