package com.travelmaker.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
  private Long managementId;   // NUMBER(10) NOT NULL,
  private String memberId;     // VARCHAR2(12) NOT NULL,
  private String email;         // VARCHAR2(100)
  private String pw;            // VARCHAR2(20) NOT NULL,
  private String nickname;      // VARCHAR2(36) NOT NULL,
  private String address;       // VARCHAR2(120),
  private String birthday;      // DATE NOT NULL;
  private String tel;           // VARCHAR2(11),
  private String gender;        // VARCHAR2(3),
  private LocalDateTime udate;  // TIMESTAMP DEFAULT SYSDATE NOT NULL,
  private LocalDateTime cdate;  // TIMESTAMP DEFAULT SYSDATE NOT NULL,
  private String codeId;       // VARCHAR2(6) NOT NULL

}
