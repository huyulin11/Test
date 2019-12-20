package com.kaifantech.component.service.taskexe.auto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.bean.info.agv.AgvInfoBean;
import com.kaifantech.component.dao.agv.info.AgvInfoDao;
import com.kaifantech.component.dao.agv.info.AgvOpChargeDao;
import com.kaifantech.component.dao.agv.info.AgvOpInitDao;
import com.kaifantech.component.service.taskexe.singletype.FancyTaskexeChargeDealer;
import com.kaifantech.component.service.taskexe.singletype.FancyTaskexeDeliverDealer;
import com.kaifantech.component.service.taskexe.singletype.FancyTaskexeToInitDealer;
import com.kaifantech.component.service.taskexe.singletype.FancyTaskexeTransportDealer;
import com.kaifantech.component.service.tasksite.info.IFancyCurrentSiteService;
import com.kaifantech.init.sys.params.AppAutoParameters;
import com.kaifantech.init.sys.qualifier.DefaultQualifier;
import com.kaifantech.util.constant.taskexe.ctrl.AgvSiteStatus;
import com.kaifantech.util.constant.taskexe.ctrl.AgvTaskType;
import com.kaifantech.util.log.AppFileLogger;

@Component
@Lazy(false)
public class KfTestAutoTaskexeModule implements IAutoTaskexeModule {
	@Autowired
	private FancyTaskexeDeliverDealer agvToDeliverDealer;

	@Autowired
	private FancyTaskexeTransportDealer agvToTransportDealer;

	@Autowired
	private FancyTaskexeChargeDealer agvChargeDealer;

	@Autowired
	private FancyTaskexeToInitDealer agvToInitDealer;

	@Autowired
	private AgvOpInitDao agvOpInitDao;

	@Autowired
	private IFancyCurrentSiteService taskSiteInfoService;

	@Autowired
	@Qualifier(DefaultQualifier.DEFAULT_AGV_INFO_DAO)
	private AgvInfoDao agvInfoDao;

	@Autowired
	private AgvOpChargeDao agvOpChargeDao;

	public synchronized void doWork() {
		autoCharge();
		toCharge();
		toInit();
		toDeliver();
		toTransport();
	}

	private void autoCharge() {
		if (AppAutoParameters.isAutoCharge()) {
			List<AgvInfoBean> list = agvInfoDao.getList();
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
		List<AgvInfoBean> list = agvInfoDao.getToDeliverList();
		AppMsg msg = AppMsg.fail();
		for (AgvInfoBean agv : list) {
			if (AgvSiteStatus.INIT.equals(agv.getSitestatus()) && AgvTaskType.DELIVER.equals(agv.getTaskstatus())) {
				msg = agvToDeliverDealer.newTask(agv, agv.getTaskstatus());
				break;
			}
		}
		if (!AppTool.isNull(msg.getMsg())) {
			AppFileLogger.error(msg.getMsg());
		}
	}

	private synchronized void toTransport() {
		List<AgvInfoBean> list = agvInfoDao.getToTransportList();
		AppMsg msg = AppMsg.fail();
		for (AgvInfoBean agv : list) {
			if (AgvSiteStatus.INIT.equals(agv.getSitestatus()) && AgvTaskType.TRANSPORT.equals(agv.getTaskstatus())) {
				msg = agvToTransportDealer.newTask(agv, agv.getTaskstatus());
				break;
			}
		}
		if (!AppTool.isNull(msg.getMsg())) {
			AppFileLogger.error(msg.getMsg());
		}
	}

	private synchronized void toCharge() {
		List<AgvInfoBean> list = agvInfoDao.getChargedList();
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
		AppMsg msg = AppMsg.fail();
		List<AgvInfoBean> list = agvInfoDao.getFreeList();
		if (AppAutoParameters.isAutoTask()) {
			for (AgvInfoBean agv : list) {
				if (AgvSiteStatus.INIT.equals(agv.getSitestatus()) && AgvTaskType.FREE.equals(agv.getTaskstatus())) {
					Integer currentSiteId = taskSiteInfoService.getCurrentSiteId(agv.getId());
					if (AppTool.equals(currentSiteId, 71, 53, 62, 89, 105)) {
						agvOpInitDao.commandToInit(agv.getId());
					}
				}
			}
		}
		list = agvInfoDao.getToInitList();
		for (AgvInfoBean agv : list) {
			if (AgvSiteStatus.INIT.equals(agv.getSitestatus()) && AgvTaskType.GOTO_INIT.equals(agv.getTaskstatus())) {
				msg = agvToInitDealer.newTask(agv, agv.getTaskstatus());
				break;
			}
		}
		if (!AppTool.isNull(msg.getMsg())) {
			AppFileLogger.error(msg.getMsg());
		}
	}

}
