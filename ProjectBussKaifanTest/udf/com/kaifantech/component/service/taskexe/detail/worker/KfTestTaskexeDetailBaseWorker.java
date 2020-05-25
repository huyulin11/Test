package com.kaifantech.component.service.taskexe.detail.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.calculatedfun.util.AppTool;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvMsgBean;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.bean.tasksite.TaskSiteInfoBean;
import com.kaifantech.component.comm.manager.ILightManager;
import com.kaifantech.component.dao.agv.info.AgvInfoDao;
import com.kaifantech.component.service.ctrl.deal.IFancyCtrlModule;
import com.kaifantech.component.service.iot.client.IIotClientService;
import com.kaifantech.component.service.taskexe.detail.op.ITaskexeDetailOpService;
import com.kaifantech.component.service.taskexe.info.TaskexeInfoService;
import com.kaifantech.component.service.tasksite.TaskSiteDevService;
import com.kaifantech.component.service.tasksite.info.IFancyCurrentSiteService;
import com.kaifantech.init.sys.qualifier.UdfQualifier;

public abstract class KfTestTaskexeDetailBaseWorker implements ITaskexeDetailWorker<FancyAgvMsgBean> {
	protected boolean isAutodoorOccupy() throws Exception {
		for (IotClientBean agv : iotClientService.getAgvCacheList()) {
			TaskSiteInfoBean thisSite = taskSiteInfoService.getCurrentSite(agv.getId());
			if (AppTool.isNull(thisSite)) {
				TaskexeBean taskexeBean = taskexeInfoService.getNextOne(agv.getId());
				if (!AppTool.isNull(taskexeBean)) {
					return true;
				}
			} else if (thisSite.isAutodoorSite()) {
				return true;
			}
		}
		return false;
	}

	protected boolean isLiftOccupy() throws Exception {
		for (IotClientBean agv : iotClientService.getAgvCacheList()) {
			TaskSiteInfoBean thisSite = taskSiteInfoService.getCurrentSite(agv.getId());
			if (AppTool.isNull(thisSite)) {
				TaskexeBean taskexeBean = taskexeInfoService.getNextOne(agv.getId());
				if (!AppTool.isNull(taskexeBean)) {
					return true;
				}
			} else if (thisSite.isLiftSite()) {
				return true;
			}
		}
		return false;
	}

	@Autowired
	private TaskexeInfoService taskexeInfoService;

	@Autowired
	protected ITaskexeDetailOpService taskexeDetailOpService;

	@Autowired
	protected TaskSiteDevService taskSiteDevService;

	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_AGV_INFO_DAO)
	protected AgvInfoDao agvInfoDao;

	@Autowired
	protected ILightManager devManager;

	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_CTRL_MODULE)
	protected IFancyCtrlModule ctrlModule;

	@Autowired
	protected IFancyCurrentSiteService taskSiteInfoService;

	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_IOT_CLIENT_SERVICE)
	protected IIotClientService iotClientService;
}
