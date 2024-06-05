package com.travelmaker.domain.mypage.svc;


import com.travelmaker.domain.entity.TrvlLst;
import com.travelmaker.domain.mypage.dao.MypageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MypageSVCImp implements MypageSVC{

  @Autowired
  MypageDAO mypageDAO;

  @Override
  public List<TrvlLst> getMemberPlanLst(Long managementId) {
    return mypageDAO.getMemberPlanLst(managementId);
  }

  @Override
  public List<TrvlLst> planIdBYDelete(Long managementId, Long planId) {
    return mypageDAO.planIdBYDelete(managementId,planId);
  }
}