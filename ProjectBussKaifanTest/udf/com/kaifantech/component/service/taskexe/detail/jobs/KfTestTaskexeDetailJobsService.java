package com.kaifantech.component.service.taskexe.detail.jobs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.calculatedfun.util.AppTool;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.bean.taskexe.TaskexeDetailBean;
import com.kaifantech.bean.tasksite.TaskSiteInfoBean;
import com.kaifantech.init.sys.qualifier.KfTestQualifier;
import com.kaifantech.util.constant.taskexe.ArrivedActType;
import com.kaifantech.util.constant.taskexe.ctrl.AgvTaskType;

/***
 * 描述任务从用户下达到发送AGV执行前的逻辑
 ***/
@Service(KfTestQualifier.TASKEXE_DETAIL_JOBS_SERVICE)
public class KfTestTaskexeDetailJobsService extends FancyTaskexeDetailJobsService {
	protected List<TaskexeDetailBean> getJobs(TaskexeBean taskexeBean) throws Exception {
		if (AgvTaskType.GOTO_INIT.equals(taskexeBean.getTasktype())) {
			return getGotoInitJobs(currentSiteService.getCurrentSite(taskexeBean.getAgvId()), taskexeBean);
		} else if (AgvTaskType.GOTO_CHARGE.equals(taskexeBean.getTasktype())) {
			return getGotoChargeJobs(taskexeBean);
		} else if (AgvTaskType.DELIVER.equals(taskexeBean.getTasktype())) {
			return getDeliverJobs(taskexeBean);
		} else if (AgvTaskType.TRANSPORT.equals(taskexeBean.getTasktype())) {
			return getTransportJobs(taskexeBean);
		}
		return null;
	}

	public List<TaskexeDetailBean> getTransportJobs(TaskexeBean taskexeBean) throws Exception {
		List<TaskexeDetailBean> tmpTaskexeDetailList = new ArrayList<>();
		TaskSiteInfoBean tasksiteBean;
		TaskexeDetailBean taskexeDetail;
		Integer toSiteId = taskexeBean.getJsonItem("TO", Integer.class);
		tasksiteBean = currentSiteService.getBean(toSiteId);
		taskexeDetail = new TaskexeDetailBean(taskexeBean.getTaskexesid(), tasksiteBean);
		taskexeDetail.setArrivedact(ArrivedActType.SLOW_STOP);
		tmpTaskexeDetailList.add(taskexeDetail);
		return tmpTaskexeDetailList;
	}

	public List<TaskexeDetailBean> getDeliverJobs(TaskexeBean taskexeBean) throws Exception {
		List<TaskexeDetailBean> tmpTaskexeDetailList = new ArrayList<>();
		TaskSiteInfoBean tasksiteBean;
		TaskexeDetailBean taskexeDetail;
		String toSiteStr = taskexeBean.getJsonItem("TO");
		if (AppTool.isNull(toSiteStr)) {
			return null;
		}
		String[] toSiteList = toSiteStr.split("#");
		int a = 0;
		for (String toSiteStrTemp : toSiteList) {
			a++;
			Integer toSiteId = Integer.parseInt(toSiteStrTemp);
			tasksiteBean = currentSiteService.getBean(toSiteId);
			taskexeDetail = new TaskexeDetailBean(taskexeBean.getTaskexesid(), tasksiteBean);
			if (a == toSiteList.length) {
				if (tasksiteBean.isAllocSite()) {
					taskexeDetail.setArrivedact(ArrivedActType.SLOW_STOP);
				} else if (tasksiteBean.isOutSite()) {
					taskexeDetail.setArrivedact(ArrivedActType.NORMAL_STOP);
				}
			} else {
				taskexeDetail.setArrivedact(ArrivedActType.WAIT);
			}
			tmpTaskexeDetailList.add(taskexeDetail);
		}
		return tmpTaskexeDetailList;
	}

	public List<TaskexeDetailBean> getGotoChargeJobs(TaskexeBean taskexeBean) {
		List<TaskexeDetailBean> tmpTaskexeDetailList = new ArrayList<>();
		TaskSiteInfoBean tasksiteBean = currentSiteService.getBean(46);
		TaskexeDetailBean taskexeDetail = new TaskexeDetailBean(taskexeBean.getTaskexesid(), tasksiteBean);
		taskexeDetail.setArrivedact(ArrivedActType.SLOW_STOP);
		tmpTaskexeDetailList.add(taskexeDetail);
		return tmpTaskexeDetailList;
	}

	public List<TaskexeDetailBean> getGotoInitJobs(TaskSiteInfoBean currentSite, TaskexeBean taskexeBean)
			throws Exception {
		List<TaskexeDetailBean> tmpTaskexeDetailList = new ArrayList<>();
		TaskSiteInfoBean initSite = currentSiteService.getBean(3054);
		if (!currentSite.equals(initSite)) {
			TaskexeDetailBean initDetail = new TaskexeDetailBean(taskexeBean.getTaskexesid(), initSite);
			initDetail.setArrivedact(ArrivedActType.SLOW_STOP);
			tmpTaskexeDetailList.add(initDetail);
		}
		return tmpTaskexeDetailList;
	}

}
