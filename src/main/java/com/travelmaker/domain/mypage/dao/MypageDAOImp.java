package com.travelmaker.domain.mypage.dao;

import com.travelmaker.domain.entity.TrvlLst;
import com.travelmaker.domain.entity.TrvlSpot;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MypageDAOImp implements MypageDAO {
  private final NamedParameterJdbcTemplate template;


  MypageDAOImp(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  //  회원이 가지고있는 모든 여행일정 목록 삭제
  @Override
  public List<TrvlLst> getMemberPlanLst(Long managementId) {

    StringBuffer sql = new StringBuffer();

    sql.append("select plan_id,management_id,trvl_nm,trvl_std,trvl_lsd,travel_days,ragion_nm ");
    sql.append("from trvl_lst ");
    sql.append("where management_id = :managementId ");
    sql.append("order by plan_id desc ");

    try {
      Map<String, Long> map = Map.of("managementId", managementId);
      List<TrvlLst> list = template.query(sql.toString(), map,
          BeanPropertyRowMapper.newInstance(TrvlLst.class));
      return list;

    } catch (EmptyResultDataAccessException e){
      return List.of();
    }
  }

//  회원이 선택한 여행일정목록 삭제후 회원이 가지고있는 여행일정 목록 반환

  @Transactional
  @Override
  public List<TrvlLst> planIdBYDelete(Long managementId, Long planId) {
    String delete_querry = "delete from trvl_lst where plan_id = :planId ";
    Map<String, Object> params = new HashMap<>();
    params.put("planId", planId);

    template.update(delete_querry, params);

    List<TrvlLst> list = getMemberPlanLst(managementId);

    return list;
  }
}