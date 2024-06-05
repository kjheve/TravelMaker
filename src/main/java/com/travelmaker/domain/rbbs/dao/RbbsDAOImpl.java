package com.travelmaker.domain.rbbs.dao;

import com.travelmaker.domain.entity.Rbbs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RbbsDAOImpl implements RbbsDAO{

  private final NamedParameterJdbcTemplate template;

  // 목록
  @Override
  public List<Rbbs> findAll(Long bbsId) {
    StringBuffer sql = new StringBuffer();
    sql.append("    select *      ");
    sql.append("      from rbbs   ");
    sql.append("     where bbs_id = :bbsId   ");
    sql.append("  order by cdate  ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("bbsId", bbsId);

    List<Rbbs> list = template.query(sql.toString(), param, BeanPropertyRowMapper.newInstance(Rbbs.class));

    return list;
  }

  // 작성
  @Override
  public Long addRbbs(Rbbs rbbs) {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into rbbs( rbbs_id, bbs_id, management_id, nickname, bcontent ) ");
    sql.append(" values( rbbs_rbbs_id_seq.nextval, :bbsId, :managementId, :nickname, :bContent ) ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("managementId", rbbs.getManagementId())
        .addValue("bbsId", rbbs.getBbsId())
        .addValue("nickname", rbbs.getNickname())
        .addValue("bContent", rbbs.getBContent());

    KeyHolder keyHolder = new GeneratedKeyHolder();

    template.update(sql.toString(), param, keyHolder, new String[]{"rbbs_id"});
    Long rbbs_id = ((BigDecimal)keyHolder.getKeys().get("rbbs_id")).longValue();

    return rbbs_id;
  }

  // 삭제
  @Override
  public int deleteById(Long rbbsId) {
    StringBuffer sql = new StringBuffer();
    sql.append(" delete from rbbs ");
    sql.append("       where rbbs_id = :rbbsId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("rbbsId", rbbsId);

    int deleteCnt = template.update(sql.toString(), param);

    return deleteCnt;
  }

  // 수정
  @Override
  public int updateById(Long rbbsId, Rbbs rbbs) {
    StringBuffer sql = new StringBuffer();
    sql.append(" update rbbs ");
    sql.append("    set bcontent = :bContent, ");
    sql.append("        udate = default ");
    sql.append("  where rbbs_id = :rbbsId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("bContent", rbbs.getBContent())
        .addValue("rbbsId", rbbsId);

    int updateCnt = template.update(sql.toString(),param);

    return updateCnt;
  }
}