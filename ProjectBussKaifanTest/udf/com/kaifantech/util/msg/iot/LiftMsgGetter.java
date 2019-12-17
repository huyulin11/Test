package com.kaifantech.util.msg.iot;

import com.kaifantech.bean.iot.msg.KfTestLiftMsgBean;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.init.sys.params.KfTestCacheKeys;
import com.calculatedfun.util.AppTool;

public class LiftMsgGetter {
	public static final String LAYER_1_CMD = "A1";
	public static final String LAYER_2_CMD = "A2";
	public static final String LAYER_3_CMD = "A3";
	public static final String CLOSE_CMD = "A4";
	public static final String SEARCH_MOD_CMD = "A5";
	public static final String SEARCH_CMD = "A6";

	public static final String LAYER_1_MSG = "21";
	public static final String LAYER_2_MSG = "22";
	public static final String LAYER_3_MSG = "23";
	public static final String OPEN_MSG = "31";
	public static final String CLOSE_MSG = "32";
	public static final String MOD_AUTO_MSG = "51";
	public static final String MOD_MANU_MSG = "52";

	public static String getMsg(Integer devId) {
		return AppCache.worker().getFresh(KfTestCacheKeys.liftMsgKey(), "" + devId);
	}

	public static KfTestLiftMsgBean getMsgBean(Integer devId) {
		String msg = getMsg(devId);
		if (AppTool.isNull(msg)) {
			return null;
		}
		return new KfTestLiftMsgBean(msg);
	}

	public static String getStatus(Integer devId) {
		KfTestLiftMsgBean msgBean = getMsgBean(devId);
		return msgBean == null ? null : msgBean.getStatus();
	}
}
