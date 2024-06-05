package com.travelmaker.domain.member.dao;

import com.travelmaker.domain.entity.Member;

import java.util.Optional;

public interface MemberDAO {

  // 회원가입
  Long inserMember(Member member);

  // 회원존재유무
  boolean existMemberId(String memberId);
  boolean existEmail(String email);
  boolean existNickname(String nickname);

  // 회원 조회
  Optional<Member> findByIdAndPw(String memberId, String pw);


  //아이디 찾기
  Optional<Member> findIdByEmail(String email);

  //비밀번호 유무 확인
  boolean hasPasswd(String memberId, String email);

  //비밀번호 변경
  int changePasswd(String memberId, String pw);

  //회원조회
  Optional<Member> checkAccount(String memberId);

  //회원수정
  int updateMember(String memberId, Member member);

  // 회원탈퇴
  int deleteMember(String memberId);
}
