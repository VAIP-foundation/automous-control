/**
 * 
 */
package com.autonomous.pm.memcache.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autonomous.pm.memcache.base.MemEntity;
import com.autonomous.pm.model.Do.TDrvRawYymm;
import com.autonomous.pm.model.Do.TSnsrRawYymm;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.TVhcl;

/**
 *
 */
public class MemVhcl extends TVhcl {
	public static final Logger logger = LoggerFactory.getLogger(MemVhcl.class);
	
	public MemVhcl(TVhcl v) {
		this.setIdV(v.getIdV());
	    this.setIdV(v.getIdV());
	    this.setVrn(v.getVrn());
	    this.setIdGop(v.getIdGop());
	    this.setBatMax(v.getBatMax());
	    this.setStts(v.getStts());
	    this.setActiv(v.getActiv());
	    this.setcDt(v.getcDt());
	    this.setmDt(v.getmDt());
	    this.setdDt(v.getdDt());
	    this.setdFlg(v.getdFlg());
	}

}
