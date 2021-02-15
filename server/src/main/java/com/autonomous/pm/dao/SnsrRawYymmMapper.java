package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TSnsrRawYymm;

public interface SnsrRawYymmMapper {
	List<TSnsrRawYymm> selectLastAll();
	TSnsrRawYymm selectById(Long idSnsr);
	TSnsrRawYymm selectLastByIdV(Long idV);
	
//	int insert(TSnsrRawYymm data);
	int insertList(List<TSnsrRawYymm> datas);
}