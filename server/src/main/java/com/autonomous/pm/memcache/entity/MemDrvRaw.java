package com.autonomous.pm.memcache.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autonomous.pm.model.Do.TDrvRawYymm;
import com.autonomous.pm.model.Do.TVhcl;

import lombok.ToString;

@ToString
public class MemDrvRaw extends TDrvRawYymm {

	public static final Logger logger = LoggerFactory.getLogger(MemDrvRaw.class);

	public MemDrvRaw(TVhcl v) {
		this.setIdV(v.getIdV());
	}

	public MemDrvRaw(TDrvRawYymm dr) {
		this.setIdDr(dr.getIdDr());
		this.setIdV(dr.getIdV());
		this.setTss(dr.getTss());
		this.setSpd(dr.getSpd());
		this.setLngLat(dr.getLngLat());
		this.setHdng(dr.getHdng());
		this.setdStts(dr.getdStts());
		this.setFront(dr.getFront());
		this.setAngl(dr.getAngl());
		this.setBrk(dr.getBrk());
		this.setDrTmS(dr.getDrTmS());
		this.setDrAuto(dr.getDrAuto());
		this.setcDt(dr.getcDt());
	}

}
