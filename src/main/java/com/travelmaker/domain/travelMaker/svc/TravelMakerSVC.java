package com.travelmaker.domain.travelMaker.svc;

import com.travelmaker.Web.form.travelMaker.PlanFormData;
import com.travelmaker.Web.form.travelMaker.TrvlSpotForm;
import com.travelmaker.domain.entity.TrvlLst;
import com.travelmaker.domain.entity.TrvlPlan;
import com.travelmaker.domain.entity.TrvlSpot;

import java.util.List;

public interface TravelMakerSVC {

  List<TrvlSpot> findRagionSearch(String ragion, Long reqPage, Long reqCnt);
  int totalByRagion(String ragion);

  List<TrvlSpot> findConditionSearch(TrvlSpotForm trvlSpotForm);
  int totalByCondition(TrvlSpotForm trvlSpotForm);

  //  여행일정목록 등록

  Long saveTrvlLst(TrvlLst trvlLst);

  //  여행일정 등록
  int saveTrvlPlan(PlanFormData planFormData);

  //
  List<TrvlPlan> getMemberFinalPlan(Long managementId, Long planId);

  int updateTrvlLst(TrvlLst trvlLst);
  int updateTrvlPlan(PlanFormData planFormData);
}