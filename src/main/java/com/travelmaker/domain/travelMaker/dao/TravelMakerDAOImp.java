package com.travelmaker.domain.travelMaker.dao;

import com.travelmaker.Web.form.travelMaker.PlanFormData;
import com.travelmaker.Web.form.travelMaker.TrvlSpotForm;
import com.travelmaker.domain.entity.TrvlLst;
import com.travelmaker.domain.entity.TrvlPlan;
import com.travelmaker.domain.entity.TrvlSpot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class TravelMakerDAOImp implements  TravelMakerDAO{
  private static final double EARTH_RADIUS_KM = 6371.01;

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  private final NamedParameterJdbcTemplate template;

  TravelMakerDAOImp(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  //  여행일정기능 부분

  //  지역별 여행지리스트 & 지역별 여행지개수
  @Override
  public List<TrvlSpot> findRagionSearch(String ragion, Long reqPage, Long reqCnt) {

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT t1.trvl_cd, t1.trvl_nm, t2.ragion_nm, t3.code_nm, t1.trvl_addr, ");
    sql.append("t1.img, t1.img2, t1.trvl_x, t1.trvl_y ");
    sql.append("FROM trvl_sp t1 ");
    sql.append("JOIN area_cd t2 ON t1.ragion_cd = t2.ragion_cd ");
    sql.append("JOIN class_cd t3 ON t1.trsp_cla = t3.code_id ");
    sql.append("WHERE t2.ragion_nm = :ragion ");
    sql.append("offset (:reqPage-1) * :recCnt rows ");
    sql.append("fetch first :recCnt rows only ");

    MapSqlParameterSource paramMap = new MapSqlParameterSource()
        .addValue("ragion",ragion)
        .addValue("reqPage",reqPage)
        .addValue("recCnt",reqCnt);

    List<TrvlSpot> list = template.query(sql.toString(), paramMap, BeanPropertyRowMapper.newInstance(TrvlSpot.class));


    return list;
  }


  @Override
  public int totalByRagion(String ragion) {

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT count(*) ");
    sql.append("FROM trvl_sp t1 ");
    sql.append("JOIN area_cd t2 ON t1.ragion_cd = t2.ragion_cd ");
    sql.append("JOIN class_cd t3 ON t1.trsp_cla = t3.code_id ");
    sql.append("WHERE t2.ragion_nm = :ragion ");


    MapSqlParameterSource paramMap = new MapSqlParameterSource()
        .addValue("ragion",ragion);

    Integer cnt = template.queryForObject(sql.toString(), paramMap, Integer.class);

    return cnt;
  }


  //  검색,태그선택,거리별로 여행지 리스트 뽑아내는 코드
  @Override
  public List<TrvlSpot> findConditionSearch(TrvlSpotForm trvlSpotForm) {


    StringBuilder sql = new StringBuilder();
    sql.append("SELECT t1.trvl_cd, t1.trvl_nm, t2.ragion_nm, t3.code_nm, t1.trvl_addr, ");
    sql.append("t1.img, t1.img2, t1.trvl_x, t1.trvl_y ");


    sql.append("FROM trvl_sp t1 ");
    sql.append("JOIN area_cd t2 ON t1.ragion_cd = t2.ragion_cd ");
    sql.append("JOIN class_cd t3 ON t1.trsp_cla = t3.code_id ");
    sql.append("WHERE 1=1 ");

    MapSqlParameterSource paramMap = new MapSqlParameterSource();

    // Tag value 처리
    if (trvlSpotForm.getTagValue() != null && !trvlSpotForm.getTagValue().isEmpty()) {
      if (trvlSpotForm.getTagValue().equals("A00")) {
        sql.append("AND (t3.pcode_id = 'A00' OR t3.pcode_id = 'A07') ");
      } else if (trvlSpotForm.getTagValue().equals("A07")) {
        sql.append("AND t3.pcode_id = 'A07' ");
      } else {
        sql.append("AND t3.code_id = :tagValue ");
        paramMap.addValue("tagValue", trvlSpotForm.getTagValue());
      }


    }

    // Region 처리
    if (trvlSpotForm.getRagion() != null && !trvlSpotForm.getRagion().isEmpty()) {
      sql.append("AND t2.ragion_nm = :ragion ");
      paramMap.addValue("ragion", trvlSpotForm.getRagion());
    }

    // Keyword 처리
    if (trvlSpotForm.getKeyword() != null && !trvlSpotForm.getKeyword().isEmpty()) {
      sql.append("AND t1.trvl_nm LIKE :keyword ");
      paramMap.addValue("keyword", "%" + trvlSpotForm.getKeyword() + "%");
    }

    // Distance 처리
    if (trvlSpotForm.getTrvlX() != null && trvlSpotForm.getTrvlY() != null && (trvlSpotForm.getTrvlX() != 0.0 || trvlSpotForm.getTrvlY() != 0.0)) {
      sql.append ("and (t1.trvl_x between :min_trvl_x and :max_trvl_x AND t1.trvl_y between :min_trvl_y and :max_trvl_y) ");
//     위도 y 경도 x
      double [] boundrybox = calculateBoundingBox(trvlSpotForm.getTrvlY(),trvlSpotForm.getTrvlX(),3.0);
      paramMap.addValue("min_trvl_x", boundrybox[1]);
      paramMap.addValue("max_trvl_x", boundrybox[3]);
      paramMap.addValue("min_trvl_y", boundrybox[0]);
      paramMap.addValue("max_trvl_y", boundrybox[2]);
      log.info("{} , {} , {} , {}" ,boundrybox[1],boundrybox[3],boundrybox[0],boundrybox[2]);
    }

    sql.append("OFFSET (:reqPage-1) * :recCnt ROWS ");
    sql.append("FETCH FIRST :recCnt ROWS ONLY ");

    paramMap.addValue("reqPage", trvlSpotForm.getReqPage());
    paramMap.addValue("recCnt", trvlSpotForm.getReqCnt());

    return template.query(sql.toString(), paramMap, BeanPropertyRowMapper.newInstance(TrvlSpot.class));
  }

  public static double[] calculateBoundingBox(double lat, double lon, double radius) {
    double latRadian = Math.toRadians(lat);
    double lonRadian = Math.toRadians(lon);

    double radiusLat = Math.toDegrees(radius / EARTH_RADIUS_KM);
    double radiusLon = Math.toDegrees(radius / (EARTH_RADIUS_KM * Math.cos(latRadian)));

    double minLat = lat - radiusLat;
    double maxLat = lat + radiusLat;
    double minLon = lon - radiusLon;
    double maxLon = lon + radiusLon;

    return new double[] { minLat, minLon, maxLat, maxLon };
  }


  //  검색기능으로 받은 파라미터들을 바탕으로 행의 개수를 조회하는 쿼리
  @Override
  public int totalByCondition(TrvlSpotForm trvlSpotForm) {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT count(*) ");
    sql.append("FROM trvl_sp t1 ");
    sql.append("JOIN area_cd t2 ON t1.ragion_cd = t2.ragion_cd ");
    sql.append("JOIN class_cd t3 ON t1.trsp_cla = t3.code_id ");
    sql.append("WHERE 1=1 ");

    MapSqlParameterSource paramMap = new MapSqlParameterSource();

    if (trvlSpotForm.getTagValue() != null && !trvlSpotForm.getTagValue().isEmpty()) {
      if (trvlSpotForm.getTagValue().equals("A00")) {
        sql.append("AND (t3.pcode_id = 'A00' OR t3.pcode_id = 'A07') ");
      } else if (trvlSpotForm.getTagValue().equals("A07")) {
        sql.append("AND t3.pcode_id = 'A07' ");
      } else {
        sql.append("AND t3.code_id = :tagValue ");
        paramMap.addValue("tagValue", trvlSpotForm.getTagValue());
      }
    }

    // Region 처리
    if (trvlSpotForm.getRagion() != null && !trvlSpotForm.getRagion().isEmpty()) {
      sql.append("AND t2.ragion_nm = :ragion ");
      paramMap.addValue("ragion", trvlSpotForm.getRagion());
    }

    // Keyword 처리
    if (trvlSpotForm.getKeyword() != null && !trvlSpotForm.getKeyword().isEmpty()) {
      sql.append("AND t1.trvl_nm LIKE :keyword ");
      paramMap.addValue("keyword", "%" + trvlSpotForm.getKeyword() + "%");
    }

    // Distance 처리
    if (trvlSpotForm.getTrvlX() != null && trvlSpotForm.getTrvlY() != null && (trvlSpotForm.getTrvlX() != 0.0 || trvlSpotForm.getTrvlY() != 0.0)) {
      sql.append ("and (t1.trvl_x between :min_trvl_x and :max_trvl_x AND t1.trvl_y between :min_trvl_y and :max_trvl_y) ");
//     위도 y 경도 x
      double [] boundrybox = calculateBoundingBox(trvlSpotForm.getTrvlY(),trvlSpotForm.getTrvlX(),3.0);
      paramMap.addValue("min_trvl_x", boundrybox[1]);
      paramMap.addValue("max_trvl_x", boundrybox[3]);
      paramMap.addValue("min_trvl_y", boundrybox[0]);
      paramMap.addValue("max_trvl_y", boundrybox[2]);
      log.info("{} , {} , {} , {}" ,boundrybox[1],boundrybox[3],boundrybox[0],boundrybox[2]);
    }

    return template.queryForObject(sql.toString(), paramMap, Integer.class);
  }



// 여행일정 목록 insert

  @Override
  public Long saveTrvlLst(TrvlLst trvlLst) {
    Long managementId = trvlLst.getManagementId();
    String trvlStd = trvlLst.getTrvlStd();
    String trvlLsd = trvlLst.getTrvlLsd();
    String trvlDays = trvlLst.getTravelDays(); //여행일수
    String ragionNm = trvlLst.getRagionNm(); //지역명

    StringBuilder sql = new StringBuilder();

    sql.append(" INSERT INTO TRVL_LST (PLAN_ID,MANAGEMENT_ID,TRAVEL_DAYS,RAGION_NM,TRVL_STD,TRVL_LSD) ");
    sql.append (" VALUES(TRVL_LST_PLAN_ID_SEQ.NEXTVAL,:managementId,:trvlDays,:ragionNm,:trvlStd,:trvlLsd) ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("managementId", managementId)
        .addValue("trvlDays", trvlDays)
        .addValue("ragionNm",ragionNm)
        .addValue("trvlStd",trvlStd)
        .addValue("trvlLsd",trvlLsd);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(),param,keyHolder,new String[]{"plan_id"});
    Long planId = ((BigDecimal)keyHolder.getKeys().get("plan_id")).longValue();
    return planId;
  }

  @Override
  public int saveTrvlPlan(PlanFormData planFormData) {


    List<Long> pmIds = new ArrayList<>();
    KeyHolder keyHolder = new GeneratedKeyHolder();

    planFormData.getPlanTabs().forEach(tag -> {
      tag.getPlanItems().forEach(item ->{
        StringBuilder sql = new StringBuilder();

        sql.append(" INSERT INTO TRVL_PL (PM_ID, TRVL_CD, PLAN_ID, MANAGEMENT_ID, TRVL_DAY) ");
        sql.append(" VALUES (TRVL_PM_ID_SEQ.NEXTVAL, :trvlCd, :planId, :managementId, :trvlDay) ");

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("trvlCd", item.getTrvlId())
            .addValue("planId",planFormData.getPlanId())
            .addValue("managementId", planFormData.getManagemetId())
            .addValue("trvlDay",item.getTrvlDay());

        template.update(sql.toString(),param,keyHolder,new String[]{"pm_id"});
        Long pmId = ((BigDecimal)keyHolder.getKeys().get("pm_id")).longValue();
        pmIds.add(pmId);
      });
    });

    return pmIds.size();

  }

//  여행일정목록 업데이트 날짜만


  @Override
  public int updateTrvlLst(TrvlLst trvlLst) {

    StringBuilder sql = new StringBuilder();

    sql.append(" UPDATE trvl_lst ");
    sql.append(" SET trvl_std = :trvlStd,  ");
    sql.append(" trvl_lsd = :trvlLsd  , travel_days = :travelDays");
    sql.append(" WHERE management_id = :managementId ");
    sql.append(" AND plan_id = :planId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("trvlStd", trvlLst.getTrvlStd())
        .addValue("trvlLsd",trvlLst.getTrvlLsd())
        .addValue("managementId", trvlLst.getManagementId())
        .addValue("planId",trvlLst.getPlanId())
        .addValue("travelDays",trvlLst.getTravelDays());

    int cnt = template.update(sql.toString(),param);

    return cnt;
  }

  //  여행일정 내용 업데이트(지우고 -> 다시채워넣기)
  public void deleteTrvlPlan(Long planId){

    String sql = "delete from trvl_pl where plan_id = :planId";
    Map<String, Object> params = new HashMap<>();
    params.put("planId", planId);

    template.update(sql, params);
  }

  @Override
  public int updateTrvlPlan(PlanFormData planFormData) {

    //    planFormData.getPlanId()로 planId에 해당하는 모든여행일정 삭제하고 가져온 여행일정 다시 넣기
    deleteTrvlPlan(planFormData.getPlanId());

    List<Long> pmIds = new ArrayList<>();
    KeyHolder keyHolder = new GeneratedKeyHolder();

    planFormData.getPlanTabs().forEach(tag -> {
      tag.getPlanItems().forEach(item ->{
        StringBuilder sql = new StringBuilder();

        sql.append(" INSERT INTO TRVL_PL (PM_ID, TRVL_CD, PLAN_ID, MANAGEMENT_ID, TRVL_DAY) ");
        sql.append(" VALUES (TRVL_PM_ID_SEQ.NEXTVAL, :trvlCd, :planId, :managementId, :trvlDay) ");

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("trvlCd", item.getTrvlId())
            .addValue("planId",planFormData.getPlanId())
            .addValue("managementId", planFormData.getManagemetId())
            .addValue("trvlDay",item.getTrvlDay());

        template.update(sql.toString(),param,keyHolder,new String[]{"pm_id"});
        Long pmId = ((BigDecimal)keyHolder.getKeys().get("pm_id")).longValue();
        pmIds.add(pmId);
      });
    });

    return pmIds.size();
  }

  //  최종화면에 member 관리용아이디와 planid로 여행지 리스트 뽑아오기


  @Override
  public List<TrvlPlan> getMemberFinalPlan(Long managementId, Long planId) {

    StringBuilder sql = new StringBuilder();

    sql.append(" select t1.pm_id,t1.trvl_day, t2.trvl_cd,t2.trvl_nm,t2.img,t2.trvl_x,t2.trvl_y ");
    sql.append(" from trvl_pl t1 join trvl_sp t2 on t1.trvl_cd = t2.trvl_cd ");
    sql.append(" where t1.plan_id = :planId and t1.management_id = :managementId ");
    sql.append(" order by t1.pm_id ");

    MapSqlParameterSource paramMap = new MapSqlParameterSource()
        .addValue("planId",planId)
        .addValue("managementId",managementId);

    List<TrvlPlan> list = template.query(sql.toString(), paramMap, BeanPropertyRowMapper.newInstance(TrvlPlan.class));

    return list;
  }
}