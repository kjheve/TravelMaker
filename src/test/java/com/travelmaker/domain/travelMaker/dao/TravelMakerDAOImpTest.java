package com.travelmaker.domain.travelMaker.dao;

import com.travelmaker.Web.form.travelMaker.TrvlSpotForm;
import com.travelmaker.domain.entity.TrvlLst;
import com.travelmaker.domain.entity.TrvlSpot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class TravelMakerDAOImpTest {

  @Autowired
  TravelMakerDAO travelMakerDAO;

  @Test
  @DisplayName("지역으로검색")
  void findRagionSearch() {
    List<TrvlSpot> list = travelMakerDAO.findRagionSearch("B0101",5l,9l);

    list.forEach(ele -> log.info("ele = {} ", ele));
  }

  @Test
  @DisplayName("지역으로검색한거 개수")
  void totalByRagion() {

    log.info("지역별 개수 = {} ", travelMakerDAO.totalByRagion("서울특별시"));
  }

  @Test
  @DisplayName("지역별 검색카테고리별검색")
  void findConditionSearch() {
    TrvlSpotForm trvlSpotForm = new TrvlSpotForm();
    trvlSpotForm.setReqPage(5l);
    trvlSpotForm.setReqCnt(20l);
    trvlSpotForm.setRagion("서울특별시");
    trvlSpotForm.setTagValue("A00");
    trvlSpotForm.setKeyword("");
    trvlSpotForm.setTrvlX(126.9717664185);
    trvlSpotForm.setTrvlY(37.5545416306);

    List<TrvlSpot> list = travelMakerDAO.findConditionSearch(trvlSpotForm);

    list.forEach(ele -> log.info("ele = {} ", ele));
  }

  @Test
  @DisplayName("지역별 검색카테고리별 개수")
  void totalByCondition() {

    TrvlSpotForm trvlSpotForm = new TrvlSpotForm();
    trvlSpotForm.setReqPage(5l);
    trvlSpotForm.setReqCnt(8l);
    trvlSpotForm.setRagion("서울특별시");
    trvlSpotForm.setTagValue("A07");
    trvlSpotForm.setKeyword("");
    trvlSpotForm.setTrvlX(126.9717664185);
    trvlSpotForm.setTrvlY(37.5545416306);

    log.info("검색결과 개수 : {}", travelMakerDAO.totalByCondition(trvlSpotForm));
  }

  @Test
  @DisplayName("여행일정목록 저장")
  void saveTrvlLst() {
    TrvlLst trvlLst = new TrvlLst();
    trvlLst.setManagementId(45l);
    trvlLst.setTrvlStd("2024-05-24");
    trvlLst.setTrvlLsd("2024-05-25");
    trvlLst.setRagionNm("서울특별시");
    trvlLst.setTravelDays("2");

    log.info("plan_id = {}", travelMakerDAO.saveTrvlLst(trvlLst));
  }
}