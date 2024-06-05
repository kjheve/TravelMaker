package com.travelmaker.Web.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

  // 파이썬 실행 위치
  final String INTERPRETER_PATH = "d:/kdit/anaconda3/python.exe";
  // 파이썬 소스코드 위치
  final String PYTHON_SOURCE_PATH = "./python_file";

  // 물리적인 이미지 파일이 있는 경우
  @GetMapping
  public String showPlot2(Model model) {

    // 파이썬 스크립트 실행
    ProcessBuilder processBuilder = new ProcessBuilder( INTERPRETER_PATH, "pieplot.py");
    processBuilder.directory(new File(PYTHON_SOURCE_PATH));
    try {
      processBuilder.start().waitFor();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    // 이미지 파일 경로를 모델에 추가
    model.addAttribute("pieImg", "/img/pie.png");
    model.addAttribute("barImg", "/img/bar.png");
    return "admin";
  }

  //  // 물리적인 이미지 파일이 없이 메모리 입출력
//  @GetMapping
//  public String showPlot(Model model) {
//
//    // 파이썬 스크립트 실행
//    ProcessBuilder processBuilder = new ProcessBuilder(INTERPRETER_PATH, "pieplot.py");
//    processBuilder.directory(new File(PYTHON_SOURCE_PATH));
//
//    // 외부 스크립트파일 실행 결과를 저장하기 위한 문자열 객체
//    StringBuilder result = new StringBuilder();
//
//    try {
//      Process process = processBuilder.start(); // 외부 스크립트 실행
//      // 외부 스크립트 실행 결과를 읽어들여 메모리에 저장
//      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//      String line;
//      while ((line = reader.readLine()) != null) {
//        result.append(line);
//      }
//      int exitCode = process.waitFor();
//      if (exitCode != 0) {
//        // 에러 스트림 처리
//        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//        StringBuilder errorMsg = new StringBuilder();
//        while ((line = errorReader.readLine()) != null) {
//          errorMsg.append(line);
//        }
//        log.error("Python script error: " + errorMsg.toString());
//        model.addAttribute("error", "Python script execution failed");
//        return "error";
//      }
//    } catch (IOException | InterruptedException e) {
//      e.printStackTrace();
//      model.addAttribute("error", "An error occurred while executing the script");
//      return "error";
//    }
//
//    // base64 인코딩된 이미지 데이터를 모델에 추가
//    model.addAttribute("imageData", result.toString());
//    return "admin";
//  }

}
