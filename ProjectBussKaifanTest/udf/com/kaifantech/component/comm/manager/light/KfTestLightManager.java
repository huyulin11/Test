package com.kaifantech.component.comm.manager.light;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.component.comm.cmd.sender.light.LightCmdSender;
import com.kaifantech.component.comm.manager.IDevRtnManager;
import com.kaifantech.component.comm.manager.ILightManager;
import com.kaifantech.util.msg.iot.LightMsgGetter;

@Service
public class KfTestLightManager implements ILightManager, IDevRtnManager {
	@Autowired
	private LightCmdSender sender;

	public AppMsg yellow(int devId, boolean isRev) throws Exception {
		String targetStatus = "";
		String command = "";
		if (isRev) {
			command = LightMsgGetter.YELLOW_REV_CMD;
			targetStatus = LightMsgGetter.YELLOW_REV_MSG;
		} else {
			command = LightMsgGetter.YELLOW_CMD;
			targetStatus = LightMsgGetter.YELLOW_MSG;
		}
		AppMsg appMsg = sendNeedRtn(devId, command, targetStatus);
		return appMsg;
	}

	public AppMsg red(int devId, boolean isRev) throws Exception {
		String targetStatus = "";
		String command = "";
		if (isRev) {
			command = LightMsgGetter.RED_REV_CMD;
			targetStatus = LightMsgGetter.RED_REV_MSG;
		} else {
			command = LightMsgGetter.RED_CMD;
			targetStatus = LightMsgGetter.RED_MSG;
		}
		AppMsg appMsg = sendNeedRtn(devId, command, targetStatus);
		return appMsg;
	}

	public AppMsg green(int devId, boolean isRev) throws Exception {
		String targetStatus = "";
		String command = "";
		if (isRev) {
			command = LightMsgGetter.GREEN_REV_CMD;
			targetStatus = LightMsgGetter.GREEN_REV_MSG;
		} else {
			command = LightMsgGetter.GREEN_CMD;
			targetStatus = LightMsgGetter.GREEN_MSG;
		}
		AppMsg appMsg = sendNeedRtn(devId, command, targetStatus);
		return appMsg;
	}

	public AppMsg close(int devId, boolean isRev) throws Exception {
		String targetStatus = "";
		String command = "";
		if (isRev) {
			command = LightMsgGetter.CLOSE_REV_CMD;
			targetStatus = LightMsgGetter.CLOSE_REV_MSG;
		} else {
			command = LightMsgGetter.CLOSE_CMD;
			targetStatus = LightMsgGetter.CLOSE_MSG;
		}
		AppMsg appMsg = sendNeedRtn(devId, command, targetStatus);
		return appMsg;
	}

	@Override
	public AppMsg doSend(int devId, String command) {
		return sender.send(devId, command);
	}

	@Override
	public boolean isCmdSend(int devId, String... targetStatus) {
		for (String tt : targetStatus) {
			if (tt.equals(LightMsgGetter.getStatus(devId))) {
				return true;
			}
		}
		return false;
	}
}
