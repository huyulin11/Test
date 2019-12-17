package com.kaifantech.init.sys.params;

public class KfTestCacheKeys extends CacheKeys {
	public static String autoDoorMsgList(Integer devId) {
		String keyVal = "AUTODOOR" + separator + "MSG_LIST";
		return getSocketKey(devId, keyVal);
	}

	public static String autoDoorMsgKey() {
		String keyVal = "AUTODOOR" + separator + "MSG_KEY";
		return getStableKey(null, keyVal);
	}

	public static String autoDoorCmd(Integer devId) {
		String keyVal = "AUTODOOR" + separator + "CMD";
		return getSocketKey(devId, keyVal);
	}

	public static String lightMsgList(Integer devId) {
		String keyVal = "LIGHT" + separator + "MSG_LIST";
		return getSocketKey(devId, keyVal);
	}

	public static String lightMsgKey() {
		String keyVal = "LIGHT" + separator + "MSG_KEY";
		return getStableKey(null, keyVal);
	}

	public static String lightCmd(Integer devId) {
		String keyVal = "LIGHT" + separator + "CMD";
		return getSocketKey(devId, keyVal);
	}

	public static String liftMsgList(Integer devId) {
		String keyVal = "LIFT" + separator + "MSG_LIST";
		return getSocketKey(devId, keyVal);
	}

	public static String liftMsgKey() {
		String keyVal = "LIFT" + separator + "MSG_KEY";
		return getStableKey(null, keyVal);
	}

	public static String liftCmd(Integer devId) {
		String keyVal = "LIFT" + separator + "CMD";
		return getSocketKey(devId, keyVal);
	}

	public static String liftLayer() {
		String keyVal = "LIFT" + separator + "LAYER";
		return getStableKey(null, keyVal);
	}

	public static String liftTrans() {
		String keyVal = "LIFT" + separator + "TRANS";
		return getStableKey(null, keyVal);
	}
}
