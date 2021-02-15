package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Dto.IisAfsQueue;

public interface IisAfsQueueMapper {

	IisAfsQueue selectById(Long idQueue);
	List<IisAfsQueue> selectInitRows(Integer pmCallStartMin, Integer pmCallEndMin);
	List<IisAfsQueue> selectNewRows(Integer pmCallStartMin, Integer pmCallEndMin);
	List<IisAfsQueue> selectNewRowsAll(Integer pmCallStartMin, Integer pmCallEndMin);
	List<IisAfsQueue> selectBlockRows(Integer pmCallBlockStartMin, Integer pmCallBlockEndMin);
	List<IisAfsQueue> selectLastKnownRows(Integer pmCallStartMin, Integer pmCallEndMin);
	
	int updateDirtyByKey(IisAfsQueue params);
	int updateDirtyList(List<IisAfsQueue> params);
	List<IisAfsQueue> selectFlightSchLs(Integer pmFlightSchStartMin, Integer pmFlightSchEndMin, String term);
	List<IisAfsQueue> selectFlightSchTodayLs(String tTy);
}