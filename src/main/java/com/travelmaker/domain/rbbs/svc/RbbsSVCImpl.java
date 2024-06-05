package com.travelmaker.domain.rbbs.svc;

import com.travelmaker.domain.rbbs.dao.RbbsDAO;
import com.travelmaker.domain.entity.Rbbs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service

public class RbbsSVCImpl implements RbbsSVC{

  private RbbsDAO rbbsDAO;

  public RbbsSVCImpl(RbbsDAO rbbsDAO) {
    this.rbbsDAO = rbbsDAO;
  }

  @Override
  public List<Rbbs> findAll(Long bbsId) {
    return rbbsDAO.findAll(bbsId);
  }

  @Override
  public Long addRbbs(Rbbs rbbs) {
    return rbbsDAO.addRbbs(rbbs);
  }

  @Override
  public int deleteById(Long rbbsId) {
    return rbbsDAO.deleteById(rbbsId);
  }

  @Override
  public int updateById(Long rbbsId, Rbbs rbbs) {
    return rbbsDAO.updateById(rbbsId, rbbs);
  }
}