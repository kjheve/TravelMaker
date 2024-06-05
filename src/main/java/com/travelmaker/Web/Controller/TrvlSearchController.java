package com.travelmaker.Web.Controller;


import com.travelmaker.domain.entity.TrvlSpot;
import com.travelmaker.domain.travelSearch.svc.TravelSearchSVC;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
// Controller 역할을 하는 클래스
@AllArgsConstructor
@Controller
@RequestMapping("/travelSearch") // http://localhost:9080/travelSearch
public class TrvlSearchController {

  @Autowired
  TravelSearchSVC travelSearchSVC;

  @GetMapping // GET http://localhost:9080/travelSearch?reqPage=1&recCnt=10
  public String findList(
                        @RequestParam(value = "keyword", defaultValue = "") String keyWord,
                        @RequestParam(value = "tagValues", defaultValue = "") String tagValues,
                        @RequestParam(value = "typeValues", defaultValue = "") String typeValues,
                        Model model,
                        @RequestParam(value = "reqPage", defaultValue = "1") Long reqPage, // 요청 페이지
                        @RequestParam(value = "recCnt", defaultValue = "16") Long recCnt,  // 레코드 수
                        @RequestParam(value = "cpgs", defaultValue = "1") Long cpgs, // 페이지 그룹 시작번호
                        @RequestParam(value = "cp", defaultValue = "1") Long cp) {

    String keyword = keyWord;

    // tagValues와 typeValues에서 불필요한 문자를 제거하고 빈 리스트인 경우를 처리
    List<String> tagList = new ArrayList<>();
    if (tagValues != null && !tagValues.isEmpty()) {
      String[] tagArray = tagValues.replaceAll("\\[|\\]|\"", "").split(",");
      tagList = Arrays.asList(tagArray);
      tagList = tagList.stream().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    List<String> typeList = new ArrayList<>();
    if (typeValues!= null && !typeValues.isEmpty()) {
      String[] typeArray = typeValues.replaceAll("[\\[\\]\"#]", "").split(",");
      typeList = Arrays.asList(typeArray);
      typeList = typeList.stream().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    // 실제 검색 로직 수행
    List<TrvlSpot> list = travelSearchSVC.findSearch(reqPage, recCnt, tagList, typeList, keyword);
    int totalCnt = travelSearchSVC.searchGetCount(tagList, typeList, keyword);

    // 모델에 데이터 추가
    model.addAttribute("keyword", keyword);
    model.addAttribute("tagValues", tagList);
    model.addAttribute("typeValues", typeList);
    model.addAttribute("list", list);
    model.addAttribute("totalCnt", totalCnt);
    model.addAttribute("cpgs", cpgs);
    model.addAttribute("cp", cp);

    return "travelSearch/travelList";
  }


//  상세조회
@GetMapping("/{trvlcd}/detail")
public String receiveJson(@PathVariable("trvlcd") Long trvlCd,
                          Model model) {

  log.info("테스트 = {}", trvlCd);
  model.addAttribute("trvlCd",trvlCd);
  return "travelSearch/travelDetail";
}


}
