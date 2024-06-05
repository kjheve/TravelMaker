package com.travelmaker.domain.member.dao;

import com.travelmaker.domain.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

//@Slf4j
@SpringBootTest
class MemberDAOImplTest {

  @Autowired
  MemberDAO memberDAO;


  @Test
  @DisplayName("회원가입")
  void inserMember(){
    Member member = new Member();
    member.setMemberId("daotest");
    member.setPw("dao1234");
    member.setTel("01011112222");
    member.setNickname("다오테스트");
    member.setGender("남");
    member.setAddress("울산남구");
    member.setBirthday("900909");
    member.setEmail("dao@kh.com");
    member.setCodeId("M0101");

//    Member insertedMember = memberDAO.inserMember(member);
//    assertNotNull(insertedMember.getManagementId());
//    System.out.println("insertedMember=" + insertedMember);
  }

  @Test
  @DisplayName("아이디(O)")
  void existMemberId(){
    boolean exit = memberDAO.existMemberId("test1");
    Assertions.assertThat(exit).isEqualTo(true);
  }
  @Test
  @DisplayName("아이디(X)")
  void dontExistMemberId(){
    boolean exit = memberDAO.existMemberId("test999");
    Assertions.assertThat(exit).isEqualTo(false);
  }

  @Test
  @DisplayName("회원조회(O)")
  void findByIdAndPw(){
    Optional<Member> optionalMember = memberDAO.findByIdAndPw("test1", "test1234");
    // 결과 검증
    Assertions.assertThat(optionalMember).isPresent(); // Optional이 존재해야 함

    Member findedMember = optionalMember.get();
    Assertions.assertThat(findedMember.getMemberId()).isEqualTo("test1"); // 아이디 일치 여부 확인
    Assertions.assertThat(findedMember.getPw()).isEqualTo("test1234"); // 비밀번호 일치 여부 확인
  }
  @Test
  @DisplayName("회원조회(X)")
  void findByIdAndPw2(){
    Optional<Member> optionalMember = memberDAO.findByIdAndPw("admin", "admin1234");
    // 결과 검증
    Assertions.assertThat(optionalMember).isEmpty(); // Optional이 없어 함
  }



}