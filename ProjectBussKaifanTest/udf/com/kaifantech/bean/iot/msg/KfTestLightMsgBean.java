package com.kaifantech.bean.iot.msg;

import com.calculatedfun.util.AppTool;

public class KfTestLightMsgBean {
	private String str;

	public KfTestLightMsgBean(String str) {
		setStr(str);
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getStatus() {
		if (AppTool.isNull(getStr()) || getStr().length() != 14) {
			return null;
		}
		return getStr().substring(8, 10);
	}
}
