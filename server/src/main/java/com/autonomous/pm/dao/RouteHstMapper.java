package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Dto.RouteHst;

public interface RouteHstMapper {
	List<RouteHst> selectLastAll();
	int insert(RouteHst data);
	int insertList(List<RouteHst> datas);
}