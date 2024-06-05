package com.travelmaker.domain.member.svc;

import com.travelmaker.domain.entity.Member;
import com.travelmaker.domain.member.dao.MemberDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSVCImpl implements MemberSVC{

  private final MemberDAO memberDAO;

  // 회원가입
  @Override
  public Long joinMember(Member member) {
    return memberDAO.inserMember(member);
  }

  // 회원 아이디 조회
  @Override
  public boolean existMemberId(String memberId) {
    return memberDAO.existMemberId(memberId);
  }
  public boolean existEmail(String email){
    return memberDAO.existEmail(email);
  }
  public boolean existNickname(String nickname){
    return memberDAO.existNickname(nickname);
  }

  // 회원 조회
  @Override
  public Optional<Member> findByIdAndPw(String memberId, String pw) {
    return memberDAO.findByIdAndPw(memberId, pw);
  }


  //이메일찾기
  @Override
  public Optional<Member> findIdByEmail(String email) {
    return memberDAO.findIdByEmail(email);
  }

  // 비밀번호 유무확인
  @Override
  public boolean hasPasswd(String memberId, String email) {
    return memberDAO.hasPasswd(memberId, email);
  }

  // 비밀번호 변경
  @Override
  public int changePasswd(String memberId, String pw) {
    return memberDAO.changePasswd(memberId, pw);
  }

  //회원정보 조회
  @Override
  public Optional<Member> checkAccount(String memberId) {
    return memberDAO.checkAccount(memberId);
  }

  //회원정보수정
  @Override
  public int modifyForm(String memberId, Member member) {

    log.info("Updating member info for ID: {}, Data: {}", memberId, member);
    // SQL 쿼리를 실행하여 회원 정보를 업데이트.
    return memberDAO.updateMember(memberId,member);
  }

  //회원탈퇴
  @Override
  public int deleteMember(String memberId) {
    return memberDAO.deleteMember(memberId);
  }
}
