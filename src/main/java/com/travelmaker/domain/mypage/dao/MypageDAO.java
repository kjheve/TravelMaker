
package com.travelmaker.domain.mypage.dao;

import com.travelmaker.domain.entity.TrvlLst;

import java.util.List;

public interface MypageDAO {

  List<TrvlLst> getMemberPlanLst(Long managementId);

  List<TrvlLst> planIdBYDelete(Long managementId, Long planId);
}
