package com.travelmaker.domain.travelSearch.dao;

import com.travelmaker.domain.entity.TrvlSpot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@SpringBootTest
class TravelSearchDAOImpTest {

  @Autowired
  TravelSearchDAO travelSearchDAO;




  @Test
  @DisplayName("개수확인")
  void getCount() {
    Integer cnt = travelSearchDAO.getCount();

    log.info("cnt = {} ", cnt);
  }

  @Test
  @DisplayName("검색")
  void findSearch() {
    List<String> ragions = new ArrayList<>();
    List<String> types = new ArrayList<>();
    List<TrvlSpot> list = travelSearchDAO.findSearch(1l,10l,ragions,types,"가람");
    list.forEach(ele -> log.info("list = {}", ele));
  }

  @Test
  @DisplayName("검색개수")
  void searchGetCount() {
    List<String> ragions = new ArrayList<>();
    List<String> types = new ArrayList<>();
    int cnt = travelSearchDAO.searchGetCount(ragions,types,"산");
   log.info("cnt = {}", cnt);
  }


}