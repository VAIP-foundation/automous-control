package com.autonomous.pm.domain.structure.ack;

import com.autonomous.pm.domain.common.ResultTy;

public class AckJoin {
	public String type = ResultTy.JOIN.toString();
	public Ack data;

	public AckJoin(Ack o) {
		this.data = o;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Ack getData() {
		return data;
	}
	public void setData(Ack data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return data.toString();
	}
}
