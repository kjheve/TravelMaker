package com.travelmaker.Web.Api;


import com.travelmaker.Web.form.member.LoginMember;
import com.travelmaker.Web.form.member.SessionConst;
import com.travelmaker.Web.form.travelMaker.PlanFormData;
import com.travelmaker.Web.form.travelMaker.PlanTab;
import com.travelmaker.Web.form.travelMaker.TravelMakerFinalForm;
import com.travelmaker.Web.form.travelMaker.TrvlSpotForm;
import com.travelmaker.Web.res.ApiResponse;
import com.travelmaker.Web.res.ResCode;
import com.travelmaker.domain.entity.Member;
import com.travelmaker.domain.entity.TrvlLst;
import com.travelmaker.domain.entity.TrvlSpot;
import com.travelmaker.domain.travelMaker.svc.TravelMakerSVC;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/travelmaker")
@RequiredArgsConstructor
public class ApiTravelMakerController {

  @Autowired
  TravelMakerSVC travelMakerSVC;

  @PostMapping("/all")
  public ApiResponse<?> allRagionList(@RequestBody TrvlSpotForm trvlSpotForm) {

    List<TrvlSpot> ragion_lst = travelMakerSVC.findConditionSearch(trvlSpotForm);// 해당 게시물 댓글 목록 찾기
//
    ApiResponse<List<TrvlSpot>> res = null;
    if (ragion_lst.size() > 0) { // 해당 게시물의 댓글이 1개라도 존재시
      res = ApiResponse.createApiResponse(ResCode.OK.getCode(), ResCode.OK.name(), ragion_lst);
      int totalCnt = travelMakerSVC.totalByCondition(trvlSpotForm);
      res.setTotalCnt(totalCnt); // 총 댓글 갯수 응답메세지로
      res.setReqPage(trvlSpotForm.getReqPage().intValue());
      res.setRecCnt(trvlSpotForm.getReqCnt().intValue());
      return res;
    } else { // 해당 게시물의 댓글이 0개면
      res = ApiResponse.createApiResponseDetail(ResCode.FAIL.getCode(), ResCode.FAIL.name(), "댓글이 없는 게시물", ragion_lst);
      return res;
    }
  }

  @PostMapping("/planner")
  public ApiResponse<?> postPlanner(HttpServletRequest request, @RequestBody PlanFormData planFormData) {

    HttpSession session = request.getSession(false);  // Get existing session, do not create a new one
    // Retrieve the LoginMember object from the session
    LoginMember loginMember = (LoginMember) session.getAttribute(SessionConst.LOGIN_MEMBER);

    log.info("management_id = {} ", loginMember.getManagementId());
    List<String> stdLsd = parseDateRange(planFormData.getPlanDays());
//     여행일정목록table insert 함수 -> 여행일정id 받아오기
    TrvlLst trvlLst = new TrvlLst();
    trvlLst.setManagementId(loginMember.getManagementId());
    trvlLst.setTrvlStd(stdLsd.get(0));
    trvlLst.setTrvlLsd(stdLsd.get(1));
    trvlLst.setRagionNm(planFormData.getRagionNm());
    trvlLst.setTravelDays(Integer.toString(planFormData.getPlanTabs().size()));

    Long planId = travelMakerSVC.saveTrvlLst(trvlLst);
    planFormData.setPlanId(planId);
    planFormData.setManagemetId(loginMember.getManagementId());

//
    ApiResponse<?> res = null;

//    선택한 총여행지수
    int pl_count = planFormData.getPlanTabs().stream()
        .mapToInt(ele -> ele.getPlanItems().size())
        .sum();

//     선택한 총여행지수와 insert한 개수가 같다면 OK
    if (pl_count == travelMakerSVC.saveTrvlPlan(planFormData)) {
      TravelMakerFinalForm travelMakerFinalForm = new TravelMakerFinalForm(planId,stdLsd.get(0),stdLsd.get(1),planFormData.getRagionNm());
      res = ApiResponse.createApiResponse(ResCode.OK.getCode(), ResCode.OK.name(), travelMakerFinalForm);
      return res;
    } else { // 해당 게시물의 댓글이 0개면
      res = ApiResponse.createApiResponseDetail(ResCode.FAIL.getCode(), ResCode.FAIL.name(), "일정등록 실패", new TravelMakerFinalForm(0l,"","",""));
      return res;
    }
  }


  @PostMapping("/update/planner")
  public ApiResponse<?> updatePlanner(HttpServletRequest request, @RequestBody PlanFormData planFormData) {

    HttpSession session = request.getSession(false);  // Get existing session, do not create a new one
    // Retrieve the LoginMember object from the session
    LoginMember loginMember = (LoginMember) session.getAttribute(SessionConst.LOGIN_MEMBER);

    log.info("management_id = {} ", loginMember.getManagementId());
    List<String> stdLsd = parseDateRange(planFormData.getPlanDays());
//     여행일정목록table insert 함수 -> 여행일정id 받아오기

    TrvlLst trvlLst = new TrvlLst();
    trvlLst.setPlanId(planFormData.getPlanId());
    trvlLst.setManagementId(loginMember.getManagementId());
    trvlLst.setTrvlStd(stdLsd.get(0));
    trvlLst.setTrvlLsd(stdLsd.get(1));
    trvlLst.setTravelDays(Integer.toString(planFormData.getPlanTabs().size()));

    travelMakerSVC.updateTrvlLst(trvlLst);

    Long planId = planFormData.getPlanId();
    log.info("planId = {} ", planId);
    planFormData.setPlanId(planId);
    planFormData.setManagemetId(loginMember.getManagementId());

//
    ApiResponse<?> res = null;

//    선택한 총여행지수
    int pl_count = planFormData.getPlanTabs().stream()
        .mapToInt(ele -> ele.getPlanItems().size())
        .sum();

//     선택한 총여행지수와 insert한 개수가 같다면 OK
    if (pl_count == travelMakerSVC.updateTrvlPlan(planFormData)) {
      TravelMakerFinalForm travelMakerFinalForm = new TravelMakerFinalForm(planId,stdLsd.get(0),stdLsd.get(1),planFormData.getRagionNm());
      res = ApiResponse.createApiResponse(ResCode.OK.getCode(), ResCode.OK.name(), travelMakerFinalForm);
      return res;
    } else { // 해당 게시물의 댓글이 0개면
      res = ApiResponse.createApiResponseDetail(ResCode.FAIL.getCode(), ResCode.FAIL.name(), "일정등록 실패", new TravelMakerFinalForm(0l,"","",""));
      return res;
    }
  }

  private List<String> parseDateRange(String dateRange) {
    String[] dates = dateRange.split(" ~ ");

    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    int currentYear = LocalDate.now().getYear();

    LocalDate startDate = LocalDate.parse(currentYear + "년 " + dates[0], inputFormatter);
    LocalDate endDate = LocalDate.parse(currentYear + "년 " + dates[1], inputFormatter);

    List<String> dateStrings = new ArrayList<>();
    dateStrings.add(startDate.format(outputFormatter));
    dateStrings.add(endDate.format(outputFormatter));

    return dateStrings;
  }


}