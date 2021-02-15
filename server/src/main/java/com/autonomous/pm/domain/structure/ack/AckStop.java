package com.autonomous.pm.domain.structure.ack;

import com.autonomous.pm.domain.common.ResultTy;
import com.autonomous.pm.domain.structure.Stop;

public class AckStop {
	public String type = ResultTy.STOP.toString();
	public Stop data;

	public AckStop(Stop o) {
		this.data = o;
	}

	public AckStop(String type, Stop o) {
		this.type = type;
		this.data = o;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Stop getData() {
		return data;
	}
	public void setData(Stop data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return data.toString();
	}
}
