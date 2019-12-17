package com.kaifantech.util.msg.iot;

import com.kaifantech.bean.iot.msg.KfTestLightMsgBean;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.init.sys.params.KfTestCacheKeys;

public class LightMsgGetter {
	public static final String YELLOW_CMD = "C1";
	public static final String GREEN_CMD = "C2";
	public static final String RED_CMD = "C3";
	public static final String CLOSE_CMD = "C4";
	public static final String SEARCH_CMD = "C6";

	public static final String YELLOW_REV_CMD = "D1";
	public static final String GREEN_REV_CMD = "D2";
	public static final String RED_REV_CMD = "D3";
	public static final String CLOSE_REV_CMD = "D4";
	public static final String SEARCH_REV_CMD = "D6";

	public static final String YELLOW_MSG = "21";
	public static final String GREEN_MSG = "22";
	public static final String RED_MSG = "23";
	public static final String CLOSE_MSG = "24";

	public static final String YELLOW_REV_MSG = "31";
	public static final String GREEN_REV_MSG = "32";
	public static final String RED_REV_MSG = "33";
	public static final String CLOSE_REV_MSG = "34";

	public static String getMsg(Integer devId) {
		return AppCache.worker().getOrInit(KfTestCacheKeys.lightMsgKey(), "" + devId, "0000");
	}

	public static KfTestLightMsgBean getMsgBean(Integer devId) {
		String msg = getMsg(devId);
		return new KfTestLightMsgBean(msg);
	}

	public static String getStatus(Integer devId) {
		KfTestLightMsgBean msgBean = getMsgBean(devId);
		return msgBean.getStatus();
	}
}
