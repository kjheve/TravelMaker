package com.travelmaker.domain.search.svc;

import com.travelmaker.domain.entity.Bbs;

import java.util.List;

public interface SearchSVC {

  List<Bbs> searchList(String codeId, String word, int offset, int pageSize);

  int countSearchResults(String codeId, String word);

}
