package com.travelmaker.interceptor;

import com.travelmaker.Web.form.member.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;

@Slf4j
public class LoginCheckInterCeptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //리다이렉트 URL만들기
    String redirectUrl = null;

    String requestURI = request.getRequestURI();
    log.info("요청uri={}",requestURI);
    log.info("요청url={}",request.getRequestURL());


    if(request.getQueryString() != null){
      log.info("요청url getQueryString={}",request.getQueryString());
      //요청파리미터 인코딩
      String queryString = URLEncoder.encode(request.getQueryString(),"UTF-8");
      StringBuffer str = new StringBuffer();
      redirectUrl = str.append(requestURI).append("?").append(queryString).toString();
      log.info("redirectUrl={}",redirectUrl);
    }else{
      redirectUrl = requestURI;
    }

    //세션 정보 읽어오기
    HttpSession session = request.getSession(false);
    if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
      log.info("미인증상태");
      response.sendRedirect("/login?redirectUrl="+redirectUrl);
      return false;
    }
    return true;
  }
}