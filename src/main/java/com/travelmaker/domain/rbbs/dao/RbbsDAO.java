package com.travelmaker.domain.rbbs.dao;

import com.travelmaker.domain.entity.Rbbs;

import java.util.List;

public interface RbbsDAO {

  // 목록
  List<Rbbs> findAll(Long bbsId);

  // 추가
  Long addRbbs(Rbbs rbbs);

  // 삭제
  int deleteById(Long rbbsId);

  // 수정
  int updateById(Long rbbsId, Rbbs rbbs);
}