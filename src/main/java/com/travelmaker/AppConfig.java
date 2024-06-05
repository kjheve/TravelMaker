package com.travelmaker;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
//    //인증 체크
//    registry.addInterceptor(new LoginCheckInterCeptor())
//        .order(1)
//        .addPathPatterns("/**")
//        .excludePathPatterns(
//            "/",
//            "/login",
//            "/logout",
//            "/members/join",
//            "/css/**",
//            "/img/**",
//            "/js/**",
//            "/test/**",
//            "/api/**",
//            "/board/**"
//        );
  }
}