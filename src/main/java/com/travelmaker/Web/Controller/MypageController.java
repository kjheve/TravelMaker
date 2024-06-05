package com.travelmaker.Web.Controller;


import com.travelmaker.Web.form.member.DetailForm;
import com.travelmaker.Web.form.member.LoginMember;
import com.travelmaker.Web.form.member.SessionConst;
import com.travelmaker.Web.form.member.UpdateForm;
import com.travelmaker.domain.entity.Member;
import com.travelmaker.domain.entity.TrvlLst;
import com.travelmaker.domain.member.svc.MemberSVC;
import com.travelmaker.domain.mypage.svc.MypageSVC;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/mypage")
public class MypageController {
  @Autowired
  MypageSVC mypageSVC;

  @GetMapping
  public String getMemberPlanLst(HttpServletRequest request,Model model) {

    HttpSession session = request.getSession(false);  // Get existing session, do not create a new one
    // Retrieve the LoginMember object from the session
    LoginMember loginMember = (LoginMember) session.getAttribute(SessionConst.LOGIN_MEMBER);

    List<TrvlLst> memberPlanLst = mypageSVC.getMemberPlanLst(loginMember.getManagementId());
    List<TrvlLst> lastTravel = new ArrayList<>();
    List<TrvlLst> forwardTravel = new ArrayList<>();

    if(memberPlanLst.size() == 0) return "mypage/myplan";

    else {
      memberPlanLst.forEach(item -> {
        log.info("lsd = {}", item.getTrvlLsd());

        item.setTrvlStd(item.getTrvlStd().substring(0, 10));
        item.setTrvlLsd(item.getTrvlLsd().substring(0, 10));
        // DateTimeFormatter로 날짜 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 문자열을 LocalDate로 파싱
        LocalDate givenDate = LocalDate.parse(item.getTrvlLsd(), formatter);

        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        Period period = Period.between(today, givenDate);
        item.setD_day(Math.abs(period.getDays()));

        // 날짜 비교
        if (givenDate.isBefore(today)) {
          lastTravel.add(item);
        } else if (givenDate.isAfter(today)) {
          forwardTravel.add(item);
        } else {
          forwardTravel.add(item);
        }

      });
      model.addAttribute("forwardTravel", forwardTravel);
      model.addAttribute("lastTravel", lastTravel);
      return "mypage/myplan";
    }
  }

  @GetMapping("delete/{planId}")
  public String deleteMemberPlanLst(HttpServletRequest request,Model model, @PathVariable("planId") Long planId) {

    HttpSession session = request.getSession(false);  // Get existing session, do not create a new one
    // Retrieve the LoginMember object from the session
    LoginMember loginMember = (LoginMember) session.getAttribute(SessionConst.LOGIN_MEMBER);

    List<TrvlLst> memberPlanLst = mypageSVC.planIdBYDelete(loginMember.getManagementId(),planId);

    List<TrvlLst> lastTravel = new ArrayList<>();
    List<TrvlLst> forwardTravel = new ArrayList<>();

    if(memberPlanLst.size() == 0) return "mypage/myplan";

    else {
      memberPlanLst.forEach(item -> {
        log.info("lsd = {}", item.getTrvlLsd());

        item.setTrvlStd(item.getTrvlStd().substring(0, 10));
        item.setTrvlLsd(item.getTrvlLsd().substring(0, 10));
        // DateTimeFormatter로 날짜 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 문자열을 LocalDate로 파싱
        LocalDate givenDate = LocalDate.parse(item.getTrvlLsd(), formatter);

        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        Period period = Period.between(today, givenDate);
        item.setD_day(Math.abs(period.getDays()));

        // 날짜 비교
        if (givenDate.isBefore(today)) {
          lastTravel.add(item);
        } else if (givenDate.isAfter(today)) {
          forwardTravel.add(item);
        } else {
          forwardTravel.add(item);
        }

      });
      model.addAttribute("forwardTravel", forwardTravel);
      model.addAttribute("lastTravel", lastTravel);
      return "mypage/myplan";
    }
  }
}