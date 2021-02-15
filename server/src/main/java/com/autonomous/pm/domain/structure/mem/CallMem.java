package com.autonomous.pm.domain.structure.mem;

import com.autonomous.pm.domain.structure.Call;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallMem extends CommonMem {
	
	public CallMem(Long idV, Call info) {
		this.setIdV(idV);
		this.setData(info);
	}
	
	public CallMem() {
	}
	
	public Call getData() {
		return (Call) data;
	}
	
	@Override
	public String toString() {
		return "CallMem [" + (idV != null ? "idV=" + idV + ", " : "") + "dirty=" + dirty + ", "
				+ (data != null ? "data=" + getData() : "") + "]";
	}
	
	
}