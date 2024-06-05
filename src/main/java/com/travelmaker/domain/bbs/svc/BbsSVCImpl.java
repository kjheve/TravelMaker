package com.travelmaker.domain.bbs.svc;

import com.travelmaker.domain.bbs.dao.BbsDAO;
import com.travelmaker.domain.entity.Bbs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BbsSVCImpl implements BbsSVC {

  private BbsDAO bbsDAO;

  public BbsSVCImpl(BbsDAO bbsDAO) {
    this.bbsDAO = bbsDAO;
  }

  // 작성
  @Override
  public Long save(String codeId, Bbs bbs) {
    return bbsDAO.save(codeId, bbs);
  }

  // 조회
  @Override
  public Optional<Bbs> findById(Long bbsId) {
    return bbsDAO.findById(bbsId);
  }

  // 조회수 증가
  @Override
  public int upHit(Long bbsId) {
    return bbsDAO.upHit(bbsId);
  }

  // 좋아요 증가
  @Override
  public int upGood(Long bbsId, Long managementId) {
    return bbsDAO.upGood(bbsId, managementId);
  }

  // 수정
  @Override
  public int updateById(Long bbsId, Bbs bbs) {
    return bbsDAO.updateById(bbsId, bbs);
  }

  // 삭제
  @Override
  public int deleteById(Long bbsId) {
    return bbsDAO.deleteById(bbsId);
  }

  // 자유게시판 목록
  @Override
  public List<Bbs> findFreeAll(int page, int pageSize) {
    int offset = (page - 1) * pageSize;
    return bbsDAO.findFreeAll(offset, pageSize);
  }

  // 자유게시판 페이징
  @Override
  public int countFreeAll() {
    return bbsDAO.countFreeAll();
  }

  // 공유게시판 목록
  @Override
  public List<Bbs> findShareAll(int offset, int pageSize) {
    return bbsDAO.findShareAll(offset,pageSize);
  }

  // 공유게시판 페이징
  @Override
  public int countShareAll() {
    return bbsDAO.countShareAll();
  }
}
