package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TCd;

public interface CdMapper {

	List<TCd> selectByCat(String cat);
	
}