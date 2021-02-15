package com.autonomous.pm.domain.structure;

import com.autonomous.pm.model.Do.TVhcl;

public class Vhcl extends TVhcl {

	@Override
	public String toString() {
		return "TVhcl [idV=" + getIdV() + ", vrn=" + getVrn() + ", idGop=" + getIdGop() + ", batMax=" + getBatMax() + ", stts=" + getStts()
				+ ", activ=" + getActiv() + ", cDt=" + getcDt() + ", mDt=" + getmDt() + ", dDt=" + getdDt() + ", dFlg=" + getdFlg() + "]";
	}
    
}