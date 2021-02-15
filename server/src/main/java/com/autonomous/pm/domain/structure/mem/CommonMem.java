package com.autonomous.pm.domain.structure.mem;

import lombok.ToString;

@ToString
public class CommonMem {
	Long idV;
	boolean dirty = false;	// 아직 전송하지 않은 데이터는 false. 전송 후에는 재전송을 막기 위해 true로 변경한다.
	Object data;
	
	public Long getIdV() {
		return idV;
	}
	public void setIdV(Long idV) {
		this.idV = idV;
	}
	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
