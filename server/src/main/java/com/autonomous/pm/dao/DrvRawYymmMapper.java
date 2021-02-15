package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TDrvRawYymm;
import com.autonomous.pm.model.Dto.DrvRawYymm;

public interface DrvRawYymmMapper {
	
	List<DrvRawYymm> selectLastAll();
	DrvRawYymm selectById(Long idDr);
	DrvRawYymm selectLastByIdV(Long idV);
	
	int insert(TDrvRawYymm data);
	int insertList(List<DrvRawYymm> datas);

}