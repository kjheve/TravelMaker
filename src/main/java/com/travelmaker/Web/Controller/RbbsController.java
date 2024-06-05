package com.travelmaker.Web.Controller;

import com.travelmaker.Web.form.rbbs.AddCommentForm;
import com.travelmaker.Web.form.rbbs.EditCommentForm;
import com.travelmaker.domain.entity.Rbbs;
import com.travelmaker.domain.rbbs.svc.RbbsSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/board/{bbsId}/comment")
@RequiredArgsConstructor
public class RbbsController {

  private final RbbsSVC rbbsSVC;

  // 댓글 등록
  @PostMapping
  public ResponseEntity<Map<String, Object>> addComment(
      @PathVariable("bbsId") Long bbsId,
      @RequestBody AddCommentForm addCommentForm) {
    Rbbs rbbs = new Rbbs();
    rbbs.setBbsId(bbsId);
    rbbs.setManagementId(addCommentForm.getManagementId());
    rbbs.setNickname(addCommentForm.getNickname());
    rbbs.setBContent(addCommentForm.getBContent());

    Long rbbsId = rbbsSVC.addRbbs(rbbs);

    Map<String, Object> response = new HashMap<>();
    Map<String, String> header = new HashMap<>();
    header.put("rtcd", "00");
    header.put("errMsg", null);
    response.put("header", header);
    response.put("body", Map.of("message", "등록완료"));

    return ResponseEntity.ok(response);
  }

  // 댓글 목록
  @GetMapping
  public ResponseEntity<List<Rbbs>> listComment(
      @PathVariable("bbsId") Long bbsId) {
    log.info("bbsId={}",bbsId);
    System.out.println(bbsId);

    List<Rbbs> list = rbbsSVC.findAll(bbsId);
    ResponseEntity<List<Rbbs>> result = ResponseEntity.ok(list);
    System.out.println(result);
    log.info("result={}",result);

    return result;
  }

  // 댓글 수정
  @PatchMapping("/{rbbsId}")
  public ResponseEntity<Map<String, Object>> editComment(
      @PathVariable("rbbsId") Long rbbsId,
      @RequestBody EditCommentForm editCommentForm
  ){
    Rbbs rbbs = new Rbbs();
    rbbs.setBContent(editCommentForm.getBContent());

    int isUpdated = rbbsSVC.updateById(rbbsId, rbbs);
    log.info(String.valueOf(isUpdated));

    Map<String, Object> response = new HashMap<>();
    Map<String, String> header = new HashMap<>();
    header.put("rtcd", "00");
    header.put("errMsg", null);
    response.put("header", header);
    response.put("body", Map.of("message", "수정완료"));

    return ResponseEntity.ok(response);
  }

  // 댓글 삭제
  @DeleteMapping("/{rbbsId}")
  public ResponseEntity<String> deleteComment(
      @PathVariable("rbbsId") Long rbbsId) {

    int delCnt = rbbsSVC.deleteById(rbbsId);
    if (delCnt==1) {
      return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 완료");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 삭제 실패");
    }
  }
}
