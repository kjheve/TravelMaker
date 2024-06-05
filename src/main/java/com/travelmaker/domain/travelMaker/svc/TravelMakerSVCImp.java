package com.travelmaker.domain.travelMaker.svc;


import com.travelmaker.Web.form.travelMaker.PlanFormData;
import com.travelmaker.Web.form.travelMaker.TrvlSpotForm;
import com.travelmaker.domain.entity.TrvlLst;
import com.travelmaker.domain.entity.TrvlPlan;
import com.travelmaker.domain.entity.TrvlSpot;
import com.travelmaker.domain.travelMaker.dao.TravelMakerDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TravelMakerSVCImp implements  TravelMakerSVC{

  @Autowired
  TravelMakerDAO travelMakerDAO;
  @Override
  public List<TrvlSpot> findRagionSearch(String ragion, Long reqPage, Long reqCnt) {
    return travelMakerDAO.findRagionSearch(ragion,reqPage,reqCnt);
  }

  @Override
  public int totalByRagion(String ragion) {
    return travelMakerDAO.totalByRagion(ragion);
  }


  @Override
  public List<TrvlSpot> findConditionSearch(TrvlSpotForm trvlSpotForm) {
    return travelMakerDAO.findConditionSearch(trvlSpotForm);
  }

  @Override
  public int totalByCondition(TrvlSpotForm trvlSpotForm) {
    return travelMakerDAO.totalByCondition(trvlSpotForm);
  }


  @Override
  public Long saveTrvlLst(TrvlLst trvlLst) {
    return travelMakerDAO.saveTrvlLst(trvlLst);
  }

  @Override
  public int saveTrvlPlan(PlanFormData planFormData) {
    return travelMakerDAO.saveTrvlPlan(planFormData);
  }

  @Override
  public List<TrvlPlan> getMemberFinalPlan(Long managementId, Long planId) {
    return travelMakerDAO.getMemberFinalPlan(managementId,planId);
  }


  @Override
  public int updateTrvlLst(TrvlLst trvlLst) {
    return travelMakerDAO.updateTrvlLst(trvlLst);
  }

  @Override
  public int updateTrvlPlan(PlanFormData planFormData) {
    return travelMakerDAO.updateTrvlPlan(planFormData);
  }
}