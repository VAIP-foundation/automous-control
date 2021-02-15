package com.autonomous.pm.domain.structure.ack;

import com.autonomous.pm.domain.common.ResultTy;
import com.autonomous.pm.domain.structure.Notification;

public class AckNotification {
	public String type = ResultTy.NOTIFICATION.toString();
	public Notification data;

	public AckNotification(Notification o) {
		this.data = o;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Notification getData() {
		return data;
	}
	public void setData(Notification data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return data.toString();
	}
}
