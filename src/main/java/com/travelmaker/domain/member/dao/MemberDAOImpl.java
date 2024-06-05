package com.travelmaker.domain.member.dao;

import com.travelmaker.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO{

  private final NamedParameterJdbcTemplate template;

  // 회원가입
  @Override
  public Long inserMember(Member member) {
    // SQL 작성
    String sql = "INSERT INTO member (MANAGEMENT_ID, MEMBER_ID, PW, TEL, NICKNAME, " +
        "GENDER, ADDRESS, CODE_ID, BIRTHDAY, EMAIL) " +
        "VALUES (MEMBER_MANAGEMENT_ID.nextval, :memberId, :pw, :tel, :nickname, " +
        ":gender, :address, :codeId, :birthday, :email)";

    // 파라미터 확인 및 디버깅
    log.info("Inserting member: {}", member);

    // SQL 실행을 위한 매핑
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("memberId", member.getMemberId())
        .addValue("pw", member.getPw())
        .addValue("tel", member.getTel())
        .addValue("nickname", member.getNickname())
        .addValue("gender", member.getGender())
        .addValue("address", member.getAddress())
        .addValue("codeId", member.getCodeId())
        .addValue("birthday", member.getBirthday())
        .addValue("email", member.getEmail());

    // 변경된 레코드 정보를 읽어오는 용도
    KeyHolder keyHolder = new GeneratedKeyHolder();

    // SQL 실행
    template.update(sql, param, keyHolder, new String[]{"MANAGEMENT_ID"});

    // Insert된 레코드에서 회원 번호 추출
    Long managementId = ((BigDecimal) keyHolder.getKeys().get("MANAGEMENT_ID")).longValue();

    log.info("Inserted member ID: {}", managementId);

    return managementId;
  }


  // 아이디 존재 유무
  @Override
  public boolean existMemberId(String memberId) {
    String sql = "select count(MEMBER_ID) from member where MEMBER_ID = :memberId ";

    Map param = Map.of("memberId", memberId);
    Integer cnt = template.queryForObject(sql, param, Integer.class);

    return cnt == 1 ? true : false;
  }
  // 이메일 존재 유무
  @Override
  public boolean existEmail(String email) {
    String sql = "select count(email) from member where email = :email ";

    Map param = Map.of("email", email);
    Integer cnt = template.queryForObject(sql, param, Integer.class);

    return cnt == 1 ? true : false;
  }
  // 닉네임 존재 유무
  @Override
  public boolean existNickname(String nickname) {
    String sql = "select count(nickname) from member where nickname = :nickname ";

    Map param = Map.of("nickname", nickname);
    Integer cnt = template.queryForObject(sql, param, Integer.class);

    return cnt == 1 ? true : false;
  }


  // 회원 조회
  @Override
  public Optional<Member> findByIdAndPw(String memberId, String pw) {
    StringBuffer sql = new StringBuffer();
    sql.append("select * from member ");
    sql.append(" where MEMBER_ID = :memberId ");
    sql.append("   and PW = :pw ");

    Map param = Map.of("memberId", memberId, "pw", pw);
    try {
      Member member = template.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(Member.class));
      return Optional.of(member);
    }catch(EmptyResultDataAccessException e){
      return Optional.empty();
    }
  }




  // 아이디 찾기
  @Override
//  Optional은 값이 있을수 있거나 없을 수 있다.
  public Optional<Member> findIdByEmail(String email) {
    StringBuffer sql = new StringBuffer();

    // 1) 쿼리작성
    sql.append("select * from member ");
    sql.append(" where email = :email ");

    // 2) 파라미터설정
    // SQL 쿼리에 사용할 파라미터(MapSqlParameterSource)를 설정. 이메일 주소를 파라미터로 추가.
    MapSqlParameterSource param = new MapSqlParameterSource();
    param.addValue("email", email);

//    상단을 사용하거나 하단을 사용(같은 표현)
//  Map param = Map.of("email", email);


    try {
      // 3) 쿼리실행
      Member member = template.queryForObject(
          sql.toString(),
          param,
          new BeanPropertyRowMapper<>(Member.class) //Member 객체로 매핑
      );
      return Optional.of(member); //이메일 존재할 경우 member반환
      // 4) 예외처리
      // 이메일로 회원을 찾지 못할 경우 empty반환
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  //비밀번호 유무 확인
  @Override
  public boolean hasPasswd(String memberId, String email) {
    StringBuffer sql = new StringBuffer();

    sql.append("select count(*) ");
    sql.append("from member ");
    sql.append("where member_id = :memberId ");
    sql.append("and email = :email ");

    Map<String, String> param = Map.of("memberId", memberId, "email", email);
    Integer cnt = template.queryForObject(sql.toString(), param, Integer.class);

//    return cnt == 1;
    return cnt == 1 ? true : false;
  }

  //비밀번호 변경
  @Override
  public int changePasswd(String memberId, String pw) {
    StringBuffer sql = new StringBuffer();
    sql.append("update member ");
    sql.append("   set pw = :pw ");
    sql.append(" where member_id  = :memberId ");

    Map<String, String> param = Map.of("memberId", memberId, "pw", pw);

    // 로그 추가
    log.info("Executing SQL: {} with params: {}", sql, param);

    int updateRow = template.update(sql.toString(), param);

    return updateRow;
  }


  //회원 조회
  @Override
  public Optional<Member> checkAccount(String memberId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select member_id,email,pw, ");
    sql.append("       tel,nickname,gender,address,birthday, ");
    sql.append("       cdate,udate ");
    sql.append("from member ");
    sql.append("where member_id = :memberId ");

    Map<String, String> param = Map.of("memberId", memberId);
    // Member.class 필드명과 Result 컬럼명과 동일한경우 자동 매핑
    RowMapper rowMapper = new BeanPropertyRowMapper(Member.class);
    try {
      // queryForObject() : 단일행 다중열 레코드의 결과 셋을 가져올때 사용하는 메소드
      //                     결과셋이 없으면 EmptyResultDataAccessException 예외를 발생
      Member findedMember = (Member) template.queryForObject(sql.toString(), param, rowMapper);
      return Optional.of(findedMember);
    }catch (EmptyResultDataAccessException e){
      return Optional.empty();
    }
  }
  //회원정보수정
  @Override
  public int updateMember(String memberId, Member member) {
    StringBuffer sql = new StringBuffer();
    sql.append("update member ");
    sql.append("set tel = :tel, ");
    sql.append("    nickname = :nickname, ");
    sql.append("    gender = :gender, ");
    sql.append("    address = :address, ");
    sql.append("    birthday = :birthday ");
    sql.append("where member_id = :memberId ");

    SqlParameterSource parm = new MapSqlParameterSource()
        .addValue("tel",member.getTel())
        .addValue("nickname",member.getNickname())
        .addValue("gender",member.getGender())
        .addValue("address",member.getAddress())
        .addValue("birthday",member.getBirthday())
        .addValue("memberId",memberId);

//    rows : 업데이트된 레코드 수
    int rows = template.update(sql.toString(), parm);

    return rows;
  }

  //회원탈퇴
  @Override
  public int deleteMember(String memberId) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from member ");
    sql.append(" where member_id = :memberId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("memberId",memberId);

    int deletedRowCnt = template.update(sql.toString(), param);

    return deletedRowCnt;
  }
}
