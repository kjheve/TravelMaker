package com.travelmaker.Web.Controller;

import com.travelmaker.Web.form.bbs.AddForm;
import com.travelmaker.Web.form.bbs.EditForm;
import com.travelmaker.domain.bbs.svc.BbsSVC;
import com.travelmaker.domain.entity.Bbs;
import com.travelmaker.domain.search.svc.SearchSVC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/board")
public class BbsController {

  private BbsSVC bbsSVC;
  private SearchSVC searchSVC;

  // 두 서비스를 모두 주입받는 단일 생성자
  public BbsController(BbsSVC bbsSVC, SearchSVC searchSVC) {
    this.bbsSVC = bbsSVC;
    this.searchSVC = searchSVC;
  }

  // 작성화면
  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("addForm", new AddForm());
    return "board/boardAdd";
  }

  // 작성요청
  @PostMapping("/add")
  public ResponseEntity<String> add(@ModelAttribute
                                    AddForm addForm,
                                    Model model,
                                    RedirectAttributes redirectAttributes
  ) {

    // 글 등록
    Bbs bbs = new Bbs();
    bbs.setManagementId(addForm.getManagementId());
    bbs.setTitle(addForm.getTitle());
    bbs.setNickname(addForm.getNickname());
    bbs.setBContent(addForm.getBContent());
    bbs.setPlanId(addForm.getPlanId());

    Long bbsId = bbsSVC.save(addForm.getCodeId(), bbs);


    String redirectUrl = "/board/" + bbsId + "/detail";
    return ResponseEntity.ok(redirectUrl);
  }

  // 자유게시판 목록
  @GetMapping("/free")
  public String findFreeAll(
      @RequestParam(value = "page",defaultValue = "1") int page,
      Model model) {
    int pageSize = 10; // 페이지 당 게시글 수
    if (page < 1) page = 1; // 페이지 번호가 1 이하일 경우 1로 설정
    List<Bbs> freeList = bbsSVC.findFreeAll(page, pageSize);

    int totalBbsCount = bbsSVC.countFreeAll();
    int totalPages = (int) Math.ceil((double) totalBbsCount / pageSize);
    int windowSize = 5; // 화면에 보여줄 페이지 번호의 개수
    int startPage = ((page - 1) / windowSize) * windowSize + 1;
    int endPage = Math.min(startPage + windowSize - 1, totalPages);

    log.info("totalBbsCount={}",totalBbsCount);
    log.info("totalPages={}",totalPages);

    model.addAttribute("freeList", freeList);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    String codeId = "F0101";
    String word = null;

    model.addAttribute("codeId", codeId);
    model.addAttribute("word", word);

    return "board/freeBoardList";
  }

  // 공유게시판 목록
  @GetMapping("/share")
  public String findShareAll(
      @RequestParam(value = "page",defaultValue = "1") int page,
      Model model) {

    int pageSize = 10; // 페이지 당 게시글 수
    if (page < 1) page = 1; // 페이지 번호가 1 이하일 경우 1로 설정
    List<Bbs> shareList = bbsSVC.findShareAll(page, pageSize);

    int totalBbsCount = bbsSVC.countShareAll();
    int totalPages = (int) Math.ceil((double) totalBbsCount / pageSize);
    int windowSize = 5; // 화면에 보여줄 페이지 번호의 개수
    int startPage = ((page - 1) / windowSize) * windowSize + 1;
    int endPage = Math.min(startPage + windowSize - 1, totalPages);

    model.addAttribute("shareList", shareList);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    return "board/shareBoardList";
  }

  // 자유게시판 검색
  @GetMapping("/free/search")
  public String freeSearch (
      @RequestParam("codeId") String codeId,
      @RequestParam("word") String word,
      @RequestParam(value = "page",defaultValue = "1") int page,
      Model model) {

    int pageSize = 10; // 페이지 당 게시글 수
    if (page <= 1) page = 1; // 페이지 번호가 1 이하일 경우 1로 설정
    int offset = (page - 1) * pageSize;

    // 자유게시판 검색 결과
    List<Bbs> freeList = searchSVC.searchList(codeId, word, offset, pageSize);
    int totalBbsCount = searchSVC.countSearchResults(codeId, word);
    int totalPages = (int) Math.ceil((double) totalBbsCount / pageSize);
    int windowSize = 5; // 화면에 보여줄 페이지 번호의 개수
    int startPage = ((page - 1) / windowSize) * windowSize + 1;
    int endPage = Math.min(startPage + windowSize - 1, totalPages);

    if (totalPages == 0) { // 검색 결과가 없을 경우
      startPage = 1;
      endPage = 1;
    }

    model.addAttribute("freeList", freeList);
    model.addAttribute("codeId", codeId);
    model.addAttribute("word", word);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    return "board/searchFreeList";
  }

  // 공유게시판 검색
  @GetMapping("/share/search")
  public String shareSearch(
      @RequestParam("codeId") String codeId,
      @RequestParam("word") String word,
      @RequestParam(value = "page",defaultValue = "1") int page,
      Model model) {

    int pageSize = 10; // 페이지 당 게시글 수
    if (page <= 1) page = 1; // 페이지 번호가 1 이하일 경우 1로 설정
    int offset = (page - 1) * pageSize;

    // 공유게시판 검색 결과
    List<Bbs> shareList = searchSVC.searchList(codeId, word, offset, pageSize);
    int totalBbsCount = searchSVC.countSearchResults(codeId, word);
    int totalPages = (int) Math.ceil((double) totalBbsCount / pageSize);
    int windowSize = 5; // 화면에 보여줄 페이지 번호의 개수
    int startPage = ((page - 1) / windowSize) * windowSize + 1;
    int endPage = Math.min(startPage + windowSize - 1, totalPages);

    if (totalPages == 0) { // 검색 결과가 없을 경우
      startPage = 1;
      endPage = 1;
    }

    model.addAttribute("shareList", shareList);
    model.addAttribute("codeId", codeId);
    model.addAttribute("word", word);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    return "board/searchShareList";
  }


  // 조회
  @GetMapping("/{bbsId}/detail")
  public String findById(
      @PathVariable("bbsId") Long bbsId,
      Model model
  ) {
    Optional<Bbs> foundBbs = bbsSVC.findById(bbsId);
    int hitCnt = bbsSVC.upHit(bbsId);
    log.info("hitCnt={}",hitCnt);
    Bbs bbs = foundBbs.orElseThrow();
    model.addAttribute("bbs", bbs);

    return "board/boardDetail";
  }

  // 좋아요 요청
  @PostMapping("{bbsId}/detail/{managementId}")
  @ResponseBody
  public Map<String, Object> goodUp(
      @PathVariable("bbsId") Long bbsId,
      @PathVariable("managementId") Long managementId,
      Model model,
      RedirectAttributes redirectAttributes
  ) {
    int goodCnt = bbsSVC.upGood(bbsId, managementId);

    log.info("goodCnt={}",goodCnt);
    Map<String, Object> response = new HashMap<>();
    response.put("goodCnt", goodCnt);

    return response;
  }

  // 삭제
  @PostMapping("/{bbsId}/del")
  public String delete(
      @PathVariable("bbsId") Long bbsId,
      RedirectAttributes redirectAttributes) {
    int deleteRowCnt = bbsSVC.deleteById(bbsId);

    redirectAttributes.addAttribute("bbsId", bbsId);

    return "redirect:/board/free";
  }

  // 수정 화면
  @GetMapping("{bbsId}/edit")
  public String editForm(
      @PathVariable("bbsId") Long bbsId,
      Model model
  ) {

    Optional<Bbs> bbs = bbsSVC.findById(bbsId);
    Bbs foundBbs = bbs.orElseThrow();

    model.addAttribute("bbs", foundBbs);
    return "board/boardEdit";
  }

  // 수정 처리
  @PostMapping("{bbsId}/edit")
  public String edit(
      @PathVariable("bbsId") Long bbsId,
      EditForm editForm,
      RedirectAttributes redirectAttributes,
      Model model
  ){

    // 글 수정
    Bbs bbs = new Bbs();
    bbs.setManagementId(editForm.getManagementId());
    bbs.setTitle(editForm.getTitle());
    bbs.setBContent(editForm.getBContent());
    bbs.setPlanId(editForm.getPlanId());

    int updateRowCnt = bbsSVC.updateById(bbsId, bbs);

    redirectAttributes.addAttribute("bbsId", bbsId);

    return "redirect:/board/{bbsId}/detail";
  }

}