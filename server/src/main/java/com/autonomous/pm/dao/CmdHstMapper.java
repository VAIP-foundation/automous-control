package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Dto.CmdHst;

public interface CmdHstMapper {
    int insertSelective(CmdHst record);
    int updateByMidSelective(CmdHst record);
}