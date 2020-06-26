package com.kaifantech.component.service.taskexe.auto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.bean.info.agv.AgvInfoBean;
import com.kaifantech.component.cache.worker.AcsBeanFactory;
import com.kaifantech.component.dao.agv.info.AgvOpChargeDao;
import com.kaifantech.component.service.taskexe.singletype.AcsAutoChargeDealer;
import com.kaifantech.component.service.tasksite.info.IFancyCurrentSiteService;
import com.kaifantech.init.sys.params.AppAutoParameters;
import com.kaifantech.util.constant.taskexe.ctrl.AgvSiteStatus;
import com.kaifantech.util.constant.taskexe.ctrl.AgvTaskType;
import com.kaifantech.util.log.AppFileLogger;

@Component
@Lazy(false)
public class KfTestAutoModule implements IAutoModule {
	@Autowired
	private AcsAutoChargeDealer agvChargeDealer;
	@Autowired
	private IFancyCurrentSiteService taskSiteInfoService;
	@Autowired
	private AgvOpChargeDao agvOpChargeDao;

	public synchronized void arrangeTask() {
		autoCharge();
		toCharge();
		toInit();
		toDeliver();
		toTransport();
	}

	private void autoCharge() {
		if (AppAutoParameters.isAutoCharge()) {
			List<AgvInfoBean> list = AcsBeanFactory.agvInfoDao().getList();
			for (AgvInfoBean agv : list) {
				if (AgvSiteStatus.INIT.equals(agv.getSitestatus()) && AgvTaskType.FREE.equals(agv.getTaskstatus())) {
					Integer currentSiteId = taskSiteInfoService.getCurrentSiteId(agv.getId());
					if (AgvInfoBean.NEED_CHARGE.equals(agv.getBattery())
							&& AppTool.equals(currentSiteId, 28, 53, 62, 89, 105)) {
						AppFileLogger.warning(agv.getId() + "号AGV电量过低，系统自动安排其去充电！");
						agvOpChargeDao.commandToCharge(agv.getId());
					}
				}
			}
		}
	}

	private synchronized void toDeliver() {
	}

	private synchronized void toTransport() {
	}

	private synchronized void toCharge() {
		List<AgvInfoBean> list = AcsBeanFactory.agvInfoDao().getChargedList();
		AppMsg msg = AppMsg.fail();
		for (AgvInfoBean agv : list) {
			if ((AgvSiteStatus.INIT.equals(agv.getSitestatus()) && AgvTaskType.GOTO_CHARGE.equals(agv.getTaskstatus()))
					|| (AgvSiteStatus.CHARGING.equals(agv.getSitestatus())
							&& AgvTaskType.BACK_CHARGE.equals(agv.getTaskstatus()))) {
				msg = agvChargeDealer.newTask(agv, agv.getTaskstatus());
				break;
			}
		}
		if (!AppTool.isNull(msg.getMsg())) {
			AppFileLogger.error(msg.getMsg());
		}
	}

	private synchronized void toInit() {
	}

}
