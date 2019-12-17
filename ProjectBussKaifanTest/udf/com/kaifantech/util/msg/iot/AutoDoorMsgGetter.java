package com.kaifantech.util.msg.iot;

import com.kaifantech.bean.iot.msg.KfTestAutoDoorMsgBean;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.init.sys.params.KfTestCacheKeys;

public class AutoDoorMsgGetter {
	public static final String OPEN_CMD = "B1";
	public static final String CLOSE_CMD = "B2";
	public static final String ALLOW_IN_CMD = "B3";
	public static final String FORBIT_IN_CMD = "B4";
	public static final String SEARCH_CMD = "B6";

	public static final String OPEN_MSG = "21";
	public static final String CLOSE_MSG = "22";
	public static final String ALLOW_IN_MSG = "23";
	public static final String FORBIT_IN_MSG = "24";

	public static String getMsg(Integer devId) {
		return AppCache.worker().getFresh(KfTestCacheKeys.autoDoorMsgKey(), "" + devId);
	}

	public static KfTestAutoDoorMsgBean getMsgBean(Integer devId) {
		String msg = getMsg(devId);
		return new KfTestAutoDoorMsgBean(msg);
	}

	public static String getStatus(Integer devId) {
		KfTestAutoDoorMsgBean msgBean = getMsgBean(devId);
		return msgBean.getStatus();
	}
}
