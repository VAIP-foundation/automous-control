package com.autonomous.pm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.CdMapper;
import com.autonomous.pm.model.KeyValue;
import com.autonomous.pm.model.Do.TCd;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    CdMapper cdMapper;
	
	public List<KeyValue> getCommonKeylist(String sCat) {
		List<KeyValue> keyList = new ArrayList<KeyValue>();
		
		List<TCd> cds = cdMapper.selectByCat(sCat);
		cds.stream().forEach(cd->{
			keyList.add(new KeyValue(cd.getCd(), cd.getNm()));
		});
		
		return keyList;
	}
	


}