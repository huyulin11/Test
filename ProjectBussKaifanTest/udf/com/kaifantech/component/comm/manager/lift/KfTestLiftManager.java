package com.kaifantech.component.comm.manager.lift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.component.comm.cmd.sender.lift.LiftCmdSender;
import com.kaifantech.component.comm.manager.IDevRtnManager;
import com.kaifantech.component.comm.manager.ILiftManager;
import com.kaifantech.init.sys.params.KfTestCacheKeys;
import com.kaifantech.util.msg.iot.LiftMsgGetter;

@Service
public class KfTestLiftManager implements ILiftManager, IDevRtnManager {
	@Autowired
	private LiftCmdSender sender;

	public AppMsg open(int devId) throws Exception {
		return doSend(devId, "");
	}

	public AppMsg close(int devId) throws Exception {
		AppMsg appMsg = sendNeedRtn(devId, LiftMsgGetter.CLOSE_CMD, LiftMsgGetter.CLOSE_MSG);
		if (appMsg.isSuccess()) {
			AppCache.worker().hset(KfTestCacheKeys.liftLayer(), "" + devId, "0");
		}
		return appMsg;
	}

	public AppMsg generalSearch(int devId) {
		return doSend(devId, LiftMsgGetter.SEARCH_CMD);
	}

	private String getStatus(int devId) throws Exception {
		return LiftMsgGetter.getStatus(devId);
	}

	public boolean isClose(int devId) throws Exception {
		String status = getStatus(devId);
		return LiftMsgGetter.CLOSE_MSG.equals(status);
	}

	public AppMsg gotoLayer(int devId, String layer) throws Exception {
		String targetStatus = "";
		String nextLayer = "0";
		if (LiftMsgGetter.LAYER_1_CMD.equals(layer)) {
			targetStatus = LiftMsgGetter.LAYER_1_MSG;
			nextLayer = "1";
		} else if (LiftMsgGetter.LAYER_2_CMD.equals(layer)) {
			targetStatus = LiftMsgGetter.LAYER_2_MSG;
			nextLayer = "2";
		} else if (LiftMsgGetter.LAYER_3_CMD.equals(layer)) {
			targetStatus = LiftMsgGetter.LAYER_3_MSG;
			nextLayer = "3";
		}
		AppMsg appMsg = sendNeedRtn(devId, layer, targetStatus);
		if (appMsg.isSuccess()) {
			AppCache.worker().hset(KfTestCacheKeys.liftLayer(), "" + devId, nextLayer);
		}
		return appMsg;
	}

	@Override
	public AppMsg doSend(int devId, String command) {
		return sender.send(devId, command);
	}

	@Override
	public boolean isCmdSend(int devId, String... targetStatus) {
		for (String tt : targetStatus) {
			if (tt.equals(LiftMsgGetter.getStatus(devId))) {
				return true;
			}
		}
		return false;
	}
}
