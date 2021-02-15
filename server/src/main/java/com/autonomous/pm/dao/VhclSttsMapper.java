package com.autonomous.pm.dao;

import com.autonomous.pm.model.Dto.VhclStts;

public interface VhclSttsMapper {
	int insertSelective(VhclStts record);
	int updateSelective(VhclStts record);
    int updateByIdvSelective(VhclStts record);
    int update(VhclStts record);
    int updateFromDest(VhclStts record);
    
}