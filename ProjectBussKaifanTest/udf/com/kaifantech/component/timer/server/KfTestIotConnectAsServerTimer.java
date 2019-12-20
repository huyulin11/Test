package com.kaifantech.component.timer.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kaifantech.component.comm.worker.client.IConnectWorker;
import com.kaifantech.init.sys.qualifier.DefaultQualifier;
import com.kaifantech.util.seq.ThreadID;
import com.kaifantech.util.thread.ThreadTool;

@Component
@Lazy(false)
public class KfTestIotConnectAsServerTimer {
	private static boolean isRunning = false;
	private static String timerType = "IOT_SERVER设备连接器";
	private final Logger logger = Logger.getLogger(KfTestIotConnectAsServerTimer.class);

	@Autowired
	@Qualifier(DefaultQualifier.DEFAULT_AGV_SERVER_WORKER)
	private IConnectWorker agvServerWorker;

	public KfTestIotConnectAsServerTimer() {
		logger.info(timerType + "开始启动！");
	}

	@Scheduled(cron = "0/5 * * * * ?")
	public void resolute() {
		if (!isRunning) {
			Thread.currentThread().setName(timerType + ThreadID.num());
			isRunning = true;
			agvSimulate();
		}
		isRunning = false;
	}

	private void agvSimulate() {
		ThreadTool.run(() -> {
			agvServerWorker.startConnect();
		});
	}

}
