package com.travelmaker.domain.travelSearch.svc;

import com.travelmaker.domain.entity.TrvlSpot;

import java.util.List;

public interface TravelSearchSVC {


  List<TrvlSpot> find(Long reqPage, Long recCnt);

  List<TrvlSpot> findSearch(Long reqPage, Long recCnt,List<String> ragions,List<String> types, String keyword);

  int getCount();

  int searchGetCount(List<String> ragions,List<String> types, String keyword);
}
