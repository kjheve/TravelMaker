package com.travelmaker.domain.travelSearch.svc;

import com.travelmaker.domain.entity.TrvlSpot;
import com.travelmaker.domain.travelSearch.dao.TravelSearchDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class TravelSearchSVCImp implements TravelSearchSVC{

  @Autowired
  TravelSearchDAO travelSearchDAO;



  @Override
  public List<TrvlSpot> find(Long reqPage, Long recCnt) {
    return travelSearchDAO.find(reqPage, recCnt);
  }

  @Override
  public List<TrvlSpot> findSearch(Long reqPage, Long recCnt, List<String> ragions, List<String> types, String keyword) {
    log.info("svc_test(findSearch)");
    return travelSearchDAO.findSearch(reqPage,recCnt,ragions,types,keyword);
  }

  @Override
  public int getCount() {
    return travelSearchDAO.getCount();
  }

  @Override
  public int searchGetCount(List<String> ragions, List<String> types, String keyword) {
    return travelSearchDAO.searchGetCount(ragions,types,keyword);
  }
}
