package com.kaifantech.component.timer.connect.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kaifantech.component.comm.worker.client.IConnectWorker;
import com.kaifantech.init.sys.AppBusinessInfo;
import com.kaifantech.init.sys.BaseBusinessInfo;
import com.kaifantech.init.sys.qualifier.DefaultQualifier;
import com.kaifantech.util.seq.ThreadID;

@Component
@Lazy(false)
public class KfTestIotConnectAsClientTimer {
	private static boolean isRunning = false;
	private static String timerType = "IOT_CLIENT_SOCKET设备连接器";
	private final Logger logger = Logger.getLogger(KfTestIotConnectAsClientTimer.class);

	@Autowired
	@Qualifier(DefaultQualifier.DEFAULT_LIGHT_WORKER)
	private IConnectWorker lightWorker;

	@Autowired
	@Qualifier(DefaultQualifier.DEFAULT_AUTO_DOOR_WORKER)
	private IConnectWorker autoDoorWorker;

	@Autowired
	@Qualifier(DefaultQualifier.DEFAULT_LIFT_WORKER)
	private IConnectWorker liftWorker;

	@Scheduled(cron = "0/1 * * * * ?")
	public void resolute() {
		if (AppBusinessInfo.CURRENT_CLIENT.equals(BaseBusinessInfo.Clients.YUFENG)) {
			return;
		}
		if (!isRunning) {
			Thread.currentThread().setName(timerType + ThreadID.num());
			isRunning = true;
			try {
				lightWorker.startConnect();
				autoDoorWorker.startConnect();
				liftWorker.startConnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			isRunning = false;
		} else {
			logger.error(timerType + "：上一次任务执行还未结束");
			isRunning = false;
		}
	}
}
