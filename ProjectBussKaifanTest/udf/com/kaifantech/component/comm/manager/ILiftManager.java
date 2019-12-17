package com.kaifantech.component.comm.manager;

import com.kaifantech.util.msg.iot.LiftMsgGetter;
import com.calculatedfun.util.msg.AppMsg;

public interface ILiftManager {
	public AppMsg open(int devId) throws Exception;

	public AppMsg close(int devId) throws Exception;

	public boolean isClose(int devId) throws Exception;

	public AppMsg gotoLayer(int devId, String layer) throws Exception;

	public AppMsg generalSearch(int devId);

	default AppMsg gotoLayerNum(int devId, String nextLayer) throws Exception {
		AppMsg appMsg = AppMsg.fail();
		if (nextLayer.equals("1")) {
			appMsg = gotoLayer(devId, LiftMsgGetter.LAYER_1_CMD);
		} else if (nextLayer.equals("2")) {
			appMsg = gotoLayer(devId, LiftMsgGetter.LAYER_2_CMD);
		} else if (nextLayer.equals("3")) {
			appMsg = gotoLayer(devId, LiftMsgGetter.LAYER_3_CMD);
		}
		return appMsg;
	}
}
