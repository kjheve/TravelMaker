package com.travelmaker.domain.travelSearch.dao;

import com.travelmaker.domain.entity.TrvlSpot;

import java.util.List;

public interface TravelSearchDAO {



  //페이징 조회
  List<TrvlSpot> find(Long reqPage, Long recCnt);

  List<TrvlSpot> findSearch(Long reqPage, Long recCnt,List<String> ragions,List<String> types, String keyword);



//  List<TrvlSpot> ragionSearch()
  int getCount();

  int searchGetCount(List<String> ragions,List<String> types, String keyword);

}
