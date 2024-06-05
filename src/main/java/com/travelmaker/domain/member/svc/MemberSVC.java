package com.travelmaker.domain.member.svc;

import com.travelmaker.domain.entity.Member;

import java.util.Optional;

public interface MemberSVC {
  // 회원가입
  Long joinMember(Member member);

  // 회원 아이디,이메일,닉네임 조회
  boolean existMemberId(String memberId);
  boolean existEmail(String email);
  boolean existNickname(String nickname);

  // 회원 조회
  Optional<Member> findByIdAndPw(String memberId, String pw);

  // 아이디찾기
  Optional<Member> findIdByEmail(String email);

  //비밀번호 유무확인
  boolean hasPasswd(String memberId, String email);

  //비밀번호 변경
  int changePasswd(String memberId, String pw);

  //회원조회
  Optional<Member> checkAccount(String MemberId);

  //회원정보 수정
  int modifyForm(String memberId, Member member);

  //회원 탈퇴
  int deleteMember(String memberId);
}
