package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TEvtHst;
import com.autonomous.pm.model.Do.VEvtHst;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.RestRawEvent;

public interface EvtHstMapper {
	List<TEvtHst> selectLastAll();
	List<VEvtHst> selectLast20();
	int insert(TEvtHst data);
	int insertList(List<TEvtHst> datas);
	
	List<RestRawEvent> selectRawList(ReqFromToDay params);
}