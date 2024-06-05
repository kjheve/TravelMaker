package com.travelmaker.domain.travelMaker.dao;

import com.travelmaker.Web.form.travelMaker.PlanFormData;
import com.travelmaker.Web.form.travelMaker.TrvlSpotForm;
import com.travelmaker.domain.entity.TrvlLst;
import com.travelmaker.domain.entity.TrvlPlan;
import com.travelmaker.domain.entity.TrvlSpot;

import java.util.List;

public interface TravelMakerDAO {


  List<TrvlSpot> findRagionSearch(String ragion, Long reqPage, Long reqCnt);

  List<TrvlSpot> findConditionSearch(TrvlSpotForm trvlSpotForm);
  int totalByCondition(TrvlSpotForm trvlSpotForm);
  int totalByRagion(String ragion);



//  여행일정목록 등록

  Long saveTrvlLst(TrvlLst trvlLst);

  int saveTrvlPlan(PlanFormData planFormData);

  List<TrvlPlan> getMemberFinalPlan(Long managementId, Long planId);



  //  여행일정 목록 업데이트
  int updateTrvlLst(TrvlLst trvlLst);
  //  여행일정내용 업데이트
  int updateTrvlPlan(PlanFormData planFormData);
}