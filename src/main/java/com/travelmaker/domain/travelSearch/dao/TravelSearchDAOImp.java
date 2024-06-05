package com.travelmaker.domain.travelSearch.dao;

import com.travelmaker.domain.entity.TrvlSpot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class TravelSearchDAOImp  implements TravelSearchDAO {

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  private final NamedParameterJdbcTemplate template;

  TravelSearchDAOImp(NamedParameterJdbcTemplate template) {
    this.template = template;
  }



  @Override
  public List<TrvlSpot> find(Long reqPage, Long recCnt) {

    StringBuffer sql = new StringBuffer();

    sql.append("select t1.trvl_cd,t1.trvl_nm ,t2.ragion_nm, t3.code_nm, t1.trvl_addr, ");
    sql.append("t1.img, t1.img2, t1.trvl_x, t1.trvl_y ");
    sql.append("from trvl_sp t1 join area_cd t2 ");
    sql.append("on t1.ragion_cd = t2.ragion_cd ");
    sql.append("join class_cd t3 ");
    sql.append("on t1.trsp_cla = t3.code_id ");
    sql.append("where t3.code_id in ('A01', 'A02', 'A03')");
    sql.append("offset (:reqPage-1) * :recCnt rows ");
    sql.append("fetch first :recCnt rows only ");

    try {
      Map<String, Long> map = Map.of("reqPage", reqPage, "recCnt", recCnt);
      List<TrvlSpot> list = template.query(sql.toString(), map,
              BeanPropertyRowMapper.newInstance(TrvlSpot.class));
      return list;

    } catch (EmptyResultDataAccessException e) {
      return List.of();
    }
  }

  @Override
  public int getCount() {
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT COUNT(*) FROM TRVL_SP ");
    sql.append("WHERE TRSP_CLA IN('A01', 'A02', 'A03') ");

    SqlParameterSource param = new MapSqlParameterSource();
    Integer cnt = template.queryForObject(sql.toString(), param, Integer.class);

    return cnt;
  }

//  검색기능으로 받은 파라미터들을 바탕으로 조회하는 쿼리
  @Override
  public List<TrvlSpot> findSearch(Long reqPage, Long recCnt, List<String> ragions, List<String> types, String keyword) {

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT t1.trvl_cd, t1.trvl_nm, t2.ragion_nm, t3.code_nm, t1.trvl_addr, ");
    sql.append("t1.img, t1.img2, t1.trvl_x, t1.trvl_y ");
    sql.append("FROM trvl_sp t1 ");
    sql.append("JOIN area_cd t2 ON t1.ragion_cd = t2.ragion_cd ");
    sql.append("JOIN class_cd t3 ON t1.trsp_cla = t3.code_id ");
    sql.append("WHERE t3.code_id IN ('A01', 'A02', 'A03') ");

    MapSqlParameterSource paramMap = new MapSqlParameterSource();

    // ragions 파라미터 처리
    if (ragions != null && !ragions.isEmpty()) {
      sql.append("AND t1.ragion_cd IN (");
      for (int i = 0; i < ragions.size(); i++) {
        if (i > 0) {
          sql.append(", ");
        }
        sql.append(":ragion").append(i);
        paramMap.addValue("ragion" + i, ragions.get(i));
      }
      sql.append(") ");
    }

    // types 파라미터 처리
    if (types != null && !types.isEmpty()) {
      sql.append("AND t3.code_nm IN (");
      for (int i = 0; i < types.size(); i++) {
        if(types.get(i).equals("축제공연행사")) types.set(i, "축제/공연/행사");
        if (i > 0) {
          sql.append(", ");
        }
        sql.append(":type").append(i);
        paramMap.addValue("type" + i, types.get(i));
      }
      sql.append(") ");
    }
    // keyword 파라미터 처리
    if (keyword != null && !keyword.isEmpty()) {
      sql.append("AND t1.trvl_nm LIKE :keyword ");
      paramMap.addValue("keyword", "%" + keyword + "%");
    }

    sql.append("OFFSET (:reqPage-1) * :recCnt ROWS ");
    sql.append("FETCH FIRST :recCnt ROWS ONLY ");

    paramMap.addValue("reqPage", reqPage);
    paramMap.addValue("recCnt", recCnt);

    return template.query(sql.toString(), paramMap, BeanPropertyRowMapper.newInstance(TrvlSpot.class));
  }

  //  검색기능으로 받은 파라미터들을 바탕으로 행의 개수를 조회하는 쿼리
  @Override
  public int searchGetCount(List<String> ragions, List<String> types, String keyword) {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT count(*) ");
    sql.append("FROM trvl_sp t1 ");
    sql.append("JOIN area_cd t2 ON t1.ragion_cd = t2.ragion_cd ");
    sql.append("JOIN class_cd t3 ON t1.trsp_cla = t3.code_id ");
    sql.append("WHERE t3.code_id IN ('A01', 'A02', 'A03') ");

    MapSqlParameterSource paramMap = new MapSqlParameterSource();

    // ragions 파라미터 처리
    if (ragions != null && !ragions.isEmpty()) {
      sql.append("AND t1.ragion_cd IN (");
      for (int i = 0; i < ragions.size(); i++) {
        if (i > 0) {
          sql.append(", ");
        }
        sql.append(":ragion").append(i);
        paramMap.addValue("ragion" + i, ragions.get(i));
      }
      sql.append(") ");
    }

    // types 파라미터 처리
    if (types != null && !types.isEmpty()) {
      sql.append("AND t3.code_nm IN (");
      for (int i = 0; i < types.size(); i++) {
        if (i > 0) {
          sql.append(", ");
        }
        sql.append(":type").append(i);
        paramMap.addValue("type" + i, types.get(i));
      }
      sql.append(") ");
    }

    // keyword 파라미터 처리
    if (keyword != null && !keyword.isEmpty()) {
      sql.append("AND t1.trvl_nm LIKE :keyword ");
      paramMap.addValue("keyword", "%" + keyword + "%");
    }
    return template.queryForObject(sql.toString(), paramMap, Integer.class);
  }



}



