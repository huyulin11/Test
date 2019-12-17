package com.kaifantech.component.comm.manager.autodoor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.component.comm.cmd.sender.autodoor.AutoDoorCmdSender;
import com.kaifantech.component.comm.manager.IAutoDoorManager;
import com.kaifantech.component.comm.manager.IDevRtnManager;
import com.kaifantech.util.msg.iot.AutoDoorMsgGetter;

@Service
public class KfTestAutoDoorManager implements IAutoDoorManager, IDevRtnManager {
	@Autowired
	private AutoDoorCmdSender sender;

	public AppMsg open(int devId) throws Exception {
		AppMsg appMsg = sendNeedRtn(devId, AutoDoorMsgGetter.OPEN_CMD, AutoDoorMsgGetter.OPEN_MSG);
		return appMsg;
	}

	public AppMsg close(int devId) throws Exception {
		AppMsg appMsg = sendNeedRtn(devId, AutoDoorMsgGetter.CLOSE_CMD, AutoDoorMsgGetter.CLOSE_MSG,
				AutoDoorMsgGetter.CLOSE_CMD);
		return appMsg;
	}

	public AppMsg search(int devId) throws Exception {
		return doSend(devId, AutoDoorMsgGetter.SEARCH_CMD);
	}

	@Override
	public AppMsg doSend(int devId, String command) {
		return sender.send(devId, command);
	}

	@Override
	public boolean isCmdSend(int devId, String... targetStatus) {
		for (String tt : targetStatus) {
			if (tt.equals(AutoDoorMsgGetter.getStatus(devId))) {
				return true;
			}
		}
		return false;
	}
}
