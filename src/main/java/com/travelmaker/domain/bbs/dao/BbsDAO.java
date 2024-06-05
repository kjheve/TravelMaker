package com.travelmaker.domain.bbs.dao;

import com.travelmaker.domain.entity.Bbs;

import java.util.List;
import java.util.Optional;

public interface BbsDAO {
  // 작성
  Long save(String codeId,Bbs bbs);

  // 조회
  Optional<Bbs> findById(Long bbsId);

  // 조회수 증가
  int upHit(Long bbsId);

  // 좋아요 증가
  public int upGood(Long bbsId, Long managementId);

  // 수정
  int updateById(Long bbsId, Bbs bbs);

  // 삭제
  int deleteById(Long bbsId);

  // 자유게시판 목록
  List<Bbs> findFreeAll(int page, int pageSize);

  // 자유게시판 페이징
  int countFreeAll();

  // 공유게시판 목록
  List<Bbs> findShareAll(int offset, int pageSize);

  // 공유게시판 페이징
  int countShareAll();
}
