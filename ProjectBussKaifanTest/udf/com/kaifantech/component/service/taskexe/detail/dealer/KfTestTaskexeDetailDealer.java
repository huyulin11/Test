package com.kaifantech.component.service.taskexe.detail.dealer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaifantech.bean.msg.fancy.agv.FancyAgvMsgBean;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.bean.taskexe.TaskexeDetailBean;
import com.kaifantech.bean.taskexe.TaskexeDetailWorksBean;
import com.kaifantech.component.service.taskexe.detail.work.ITaskexeDetailWorksInfoService;
import com.kaifantech.component.service.taskexe.detail.worker.KfTestTaskexeDetailLiftWorker;
import com.kaifantech.component.service.taskexe.detail.worker.KfTestTaskexeDetailWaitWorker;
import com.kaifantech.init.sys.params.AppSysParameters;
import com.kaifantech.mappings.taskexe.TaskexeDetailMapper;
import com.kaifantech.util.constant.taskexe.ArrivedActType;
import com.kaifantech.util.constant.taskexe.WmsDetailOpFlag;

@Service
public class KfTestTaskexeDetailDealer extends AcsTaskexeDetailDealer<FancyAgvMsgBean> {
	public boolean dealDetail(TaskexeBean taskexeBean, FancyAgvMsgBean fancyAgvMsgBean, TaskexeDetailBean taskexeDetail)
			throws Exception {
		if (ArrivedActType.WAIT.equals(taskexeDetail.getArrivedact())
				|| ArrivedActType.LIFT_READY.equals(taskexeDetail.getArrivedact())) {
			List<TaskexeDetailWorksBean> works = taskexeDetailWorksService.getWorks(taskexeDetail);
			if (ArrivedActType.WAIT.equals(taskexeDetail.getArrivedact())) {
				taskexeDetailWaitDealer.when(fancyAgvMsgBean, taskexeBean, taskexeDetail, works);
				return false;
			} else if (ArrivedActType.LIFT_READY.equals(taskexeDetail.getArrivedact())) {
				taskexeDetailWorksLiftDealer.when(fancyAgvMsgBean, taskexeBean, taskexeDetail, works);
				return false;
			}
		}

		AppSysParameters.setTaskstop(fancyAgvMsgBean.agvId(), false);
		if (ArrivedActType.START.equals(taskexeDetail.getArrivedact())) {
			if (taskexeDetail.isSend() && taskexeDetail.matchThisSite(fancyAgvMsgBean.currentSite())) {
				taskexeDetailMapper.updateOpflag(taskexeDetail.setOpflag(WmsDetailOpFlag.OVER));
			}
			return false;
		} else if (ArrivedActType.TURN_LEFT.equals(taskexeDetail.getArrivedact())) {
			if (taskexeDetail.matchThisSite(fancyAgvMsgBean.currentSite())) {
				taskexeDetailMapper.updateOpflag(taskexeDetail.setOpflag(WmsDetailOpFlag.OVER));
			}
			return false;
		} else if (ArrivedActType.TURN_RIGHT.equals(taskexeDetail.getArrivedact())) {
			if (taskexeDetail.matchThisSite(fancyAgvMsgBean.currentSite())) {
				taskexeDetailMapper.updateOpflag(taskexeDetail.setOpflag(WmsDetailOpFlag.OVER));
			}
			return false;
		} else if (ArrivedActType.SLOW_STOP.equals(taskexeDetail.getArrivedact())) {
			if (taskexeDetail.matchThisSite(fancyAgvMsgBean.currentSite())) {
				taskexeDetailMapper.updateOpflag(taskexeDetail.setOpflag(WmsDetailOpFlag.OVER));
			}
			return false;
		} else if (ArrivedActType.CHANGE_SPEED.equals(taskexeDetail.getArrivedact())) {
			if (taskexeDetail.matchThisSite(fancyAgvMsgBean.currentSite())) {
				taskexeDetailMapper.updateOpflag(taskexeDetail.setOpflag(WmsDetailOpFlag.OVER));
			}
			return false;
		}
		return true;
	}

	@Autowired
	private TaskexeDetailMapper taskexeDetailMapper;

	@Autowired
	private KfTestTaskexeDetailWaitWorker taskexeDetailWaitDealer;

	@Autowired
	private KfTestTaskexeDetailLiftWorker taskexeDetailWorksLiftDealer;

	@Autowired
	private ITaskexeDetailWorksInfoService taskexeDetailWorksService;
}
