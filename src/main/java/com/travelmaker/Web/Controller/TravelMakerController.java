package com.travelmaker.Web.Controller;


import com.travelmaker.Web.form.member.LoginMember;
import com.travelmaker.Web.form.member.SessionConst;
import com.travelmaker.Web.form.travelMaker.TravelMakerFinalForm;
import com.travelmaker.domain.entity.TrvlPlan;
import com.travelmaker.domain.travelMaker.svc.TravelMakerSVC;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/travelmaker") // http://localhost:9080/travelSearch
public class TravelMakerController {

  @Autowired
  TravelMakerSVC travelMakerSVC;

  @GetMapping
  public String selectTravel() {

    return "travelMaker/selectTravel";
  }

  @GetMapping("dateselect")
  public String selectTime(Model model,
                           @RequestParam(value = "ragion", defaultValue = "false") String ragion) {

    model.addAttribute("ragion",ragion);
    return "travelMaker/planTravel";
  }

  @GetMapping("planner")
  public String setplan(Model model,
                        @RequestParam(value = "ragion", defaultValue = "") String ragion,
                        @RequestParam(value = "dayOfWeek", defaultValue = "") String dayOfWeekStr,
                        @RequestParam(value = "date", defaultValue = "") String dateStr,
                        @RequestParam(value = "startTime", defaultValue = "") String startTimeStr,
                        @RequestParam(value = "endTime", defaultValue = "") String endTimeStr) {

    List<String> dayOfWeeks = dayOfWeekStr.isEmpty() ? Collections.emptyList() : Arrays.asList(dayOfWeekStr.split(","));
    List<String> dates = dateStr.isEmpty() ? Collections.emptyList() : Arrays.asList(dateStr.split(","));
    List<String> startTimes = startTimeStr.isEmpty() ? Collections.emptyList() : Arrays.asList(startTimeStr.split(","));
    List<String> endTimes = endTimeStr.isEmpty() ? Collections.emptyList() : Arrays.asList(endTimeStr.split(","));

    model.addAttribute("ragion", ragion);
    model.addAttribute("planDays", convertToDate(dates.get(0)) +" ~ "+ convertToDate(dates.get(dates.size()-1)));
    model.addAttribute("day",dates.size());


    return "travelMaker/planPlace";
  }

  @PostMapping("final")
  public String finalScreen(HttpServletRequest request, @RequestParam Map<String, String> travelMakerFinalForm, Model model){

    HttpSession session = request.getSession(false);  // Get existing session, do not create a new one
    // Retrieve the LoginMember object from the session
    LoginMember loginMember = (LoginMember) session.getAttribute(SessionConst.LOGIN_MEMBER);

    log.info("travelMakerFinalForm = {}", travelMakerFinalForm);
    Long planId = Long.parseLong(travelMakerFinalForm.get("planId"));
    List<TrvlPlan> trvlPlans = travelMakerSVC.getMemberFinalPlan(loginMember.getManagementId(),planId);

    model.addAttribute("planId",planId);
    model.addAttribute("trvlPlans",trvlPlans);
    model.addAttribute("days",trvlPlans.get(trvlPlans.size()-1).getTrvlDay());
    model.addAttribute("trvlStd",travelMakerFinalForm.get("trvlStd"));
    model.addAttribute("trvlLsd",travelMakerFinalForm.get("trvlLsd"));
    model.addAttribute("ragionNm",travelMakerFinalForm.get("ragionNm"));

    trvlPlans.forEach(ele -> log.info("ele = {}",ele));
    return "travelMaker/planResult";
  }

  //  마이페이지에서 조회화면 만들기
  @PostMapping("check")
  public String planScreen(HttpServletRequest request, @RequestParam Map<String, String> travelMakerFinalForm, Model model){

    log.info("travelmakerFinalForm = {}", travelMakerFinalForm);

    HttpSession session = request.getSession(false);  // Get existing session, do not create a new one
    // Retrieve the LoginMember object from the session
    LoginMember loginMember = (LoginMember) session.getAttribute(SessionConst.LOGIN_MEMBER);

    log.info("travelMakerFinalForm = {}", travelMakerFinalForm);
    Long planId = Long.parseLong(travelMakerFinalForm.get("planId"));
    List<TrvlPlan> trvlPlans = travelMakerSVC.getMemberFinalPlan(loginMember.getManagementId(),planId);


    model.addAttribute("planId",planId);
    model.addAttribute("trvlPlans",trvlPlans);
    model.addAttribute("days",trvlPlans.get(trvlPlans.size()-1).getTrvlDay());
    model.addAttribute("trvlStd",travelMakerFinalForm.get("trvlStd"));
    model.addAttribute("trvlLsd",travelMakerFinalForm.get("trvlLsd"));
    model.addAttribute("ragionNm",travelMakerFinalForm.get("ragionNm"));

    trvlPlans.forEach(ele -> log.info("ele = {}",ele));
    return "travelMaker/planResult";
  }


  //  마이페이지에서 조회화면 만들기
  @PostMapping("modify")
  public String modifyScreen(HttpServletRequest request, @RequestParam Map<String, String> travelMakerFinalForm, Model model){

    HttpSession session = request.getSession(false);  // Get existing session, do not create a new one
    // Retrieve the LoginMember object from the session
    LoginMember loginMember = (LoginMember) session.getAttribute(SessionConst.LOGIN_MEMBER);

    log.info("travelMakerFinalForm = {}", travelMakerFinalForm);
    Long planId = Long.parseLong(travelMakerFinalForm.get("planId"));
    List<TrvlPlan> trvlPlans = travelMakerSVC.getMemberFinalPlan(loginMember.getManagementId(),planId);


    model.addAttribute("planId",planId);
    model.addAttribute("trvlPlans",trvlPlans);
    model.addAttribute("day",trvlPlans.get(trvlPlans.size()-1).getTrvlDay());
    model.addAttribute("trvlStd",travelMakerFinalForm.get("trvlStd"));
    model.addAttribute("trvlLsd",travelMakerFinalForm.get("trvlLsd"));
    model.addAttribute("ragionNm",travelMakerFinalForm.get("ragionNm"));

    trvlPlans.forEach(ele -> log.info("ele = {}",ele));

    return "travelMaker/planPlaceEdit";
  }

  private String convertToDate(String dateStr) {
    if (dateStr == null || dateStr.length() != 4) {
      throw new IllegalArgumentException("Invalid date format. Expected format is 'MMDD'.");
    }

    String month = dateStr.substring(0, 2);
    String day = dateStr.substring(2, 4);

    // 앞에 '0'이 있으면 제거
    int monthInt = Integer.parseInt(month);
    int dayInt = Integer.parseInt(day);

    return monthInt + "월 " + dayInt + "일";
  }


}