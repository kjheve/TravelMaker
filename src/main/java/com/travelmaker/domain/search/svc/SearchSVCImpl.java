package com.travelmaker.domain.search.svc;

import com.travelmaker.domain.entity.Bbs;
import com.travelmaker.domain.search.dao.SearchDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchSVCImpl implements SearchSVC {

  private SearchDAO searchDAO;

  public SearchSVCImpl(SearchDAO searchDAO) {
    this.searchDAO = searchDAO;
  }

  @Override
  public List<Bbs> searchList(String codeId, String word, int offset, int pageSize) {
    return searchDAO.searchList(codeId, word, offset, pageSize);
  }

  @Override
  public int countSearchResults(String codeId, String word) {
    return searchDAO.countSearchResults(codeId, word);
  }
}