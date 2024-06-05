package com.travelmaker.domain.mypage.svc;

import com.travelmaker.domain.entity.TrvlLst;
import org.springframework.stereotype.Service;

import java.util.List;


public interface MypageSVC {

  List<TrvlLst> getMemberPlanLst(Long managementId);

  List<TrvlLst> planIdBYDelete(Long managementId, Long planId);
}