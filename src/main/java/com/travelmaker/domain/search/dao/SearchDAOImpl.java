package com.travelmaker.domain.search.dao;

import com.travelmaker.domain.entity.Bbs;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchDAOImpl implements SearchDAO{

  private final NamedParameterJdbcTemplate template;


  // 게시판 검색
  @Override
  public List<Bbs> searchList(String codeId, String word, int offset, int pageSize) {

    StringBuffer sql = new StringBuffer();
    sql.append("    SELECT *  ");
    sql.append("      FROM ( SELECT ROWNUM AS RNUM, A.* ");
    sql.append("               FROM( SELECT * ");
    sql.append("                       FROM BBS ");
    sql.append("                      WHERE CODE_ID = :codeId ");
    sql.append("                        AND TITLE LIKE :word ");
    sql.append("                      ORDER BY BBS_ID DESC) ");
    sql.append("            A WHERE ROWNUM <= :endRow) ");
    sql.append("     WHERE RNUM > :startRow ");

    int startRow = offset;
    int endRow = offset + pageSize;

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("word", "%" + word + "%")
        .addValue("codeId", codeId)
        .addValue("startRow", startRow)
        .addValue("endRow", endRow);


    List<Bbs> list = template.query(sql.toString(), param, BeanPropertyRowMapper.newInstance(Bbs.class));
    return list;
  }

  // 페이징
  @Override
  public int countSearchResults(String codeId, String word) {
    StringBuffer sql = new StringBuffer();
    sql.append("    SELECT COUNT(*) ");
    sql.append("      FROM BBS ");
    sql.append("     WHERE CODE_ID = :codeId ");
    sql.append("       AND TITLE LIKE :word ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("codeId", codeId)
        .addValue("word", "%" + word + "%");

    return template.queryForObject(sql.toString(), param, Integer.class);
  }

}
