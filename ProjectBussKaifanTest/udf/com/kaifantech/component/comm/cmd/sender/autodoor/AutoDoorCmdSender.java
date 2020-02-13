package com.kaifantech.component.comm.cmd.sender.autodoor;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kaifantech.component.comm.worker.client.IConnectWorker;
import com.kaifantech.component.log.AgvStatusDBLogger;
import com.kaifantech.init.sys.qualifier.UdfQualifier;
import com.kaifantech.util.log.AppFileLogger;
import com.calculatedfun.util.msg.AppMsg;

@Service
public class AutoDoorCmdSender {
	@Autowired
	private AgvStatusDBLogger dbLogger;

	@Inject
	@Qualifier(UdfQualifier.DEFAULT_AUTO_DOOR_WORKER)
	private IConnectWorker worker;

	public AppMsg send(Integer devId, String cmd) {
		String finalCmd = cmd;
		AppMsg appMsg = worker.get(devId).sendCmd(finalCmd);
		if (appMsg.isSuccess()) {
			String msg = "向" + devId + "号AUTO_DOOR下达命令: " + cmd;
			AppFileLogger.connectInfo(msg);
			dbLogger.warning(msg, devId, AgvStatusDBLogger.MSG_LEVEL_WARNING);
		}
		return appMsg;
	}
}
