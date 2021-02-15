package com.autonomous.pm.model.Dto;

import com.autonomous.pm.model.Do.TSnsrRawYymm;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.VVhcl;

public class Vhcl extends VVhcl {
	DrvRawYymm drvRaw;
	TSnsrRawYymm snsrRawYymm;
	TTrpRaw trpRaw;
	
	public DrvRawYymm getDrvRaw() {
		return drvRaw;
	}
	public void setDrvRaw(DrvRawYymm drvRaw) {
		this.drvRaw = drvRaw;
	}
	public TSnsrRawYymm getSnsrRawYymm() {
		return snsrRawYymm;
	}
	public void setSnsrRawYymm(TSnsrRawYymm snsrRawYymm) {
		this.snsrRawYymm = snsrRawYymm;
	}
	public TTrpRaw getTrpRaw() {
		return trpRaw;
	}
	public void setTrpRaw(TTrpRaw trpRaw) {
		this.trpRaw = trpRaw;
	}
	
	

}