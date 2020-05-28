package com.kaifantech.component.service.taskexe.detail.dealer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaifantech.bean.msg.fancy.agv.FancyAgvMsgBean;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.bean.taskexe.TaskexeDetailBean;
import com.kaifantech.bean.taskexe.TaskexeDetailWorksBean;
import com.kaifantech.component.service.taskexe.detail.worker.KfTestTaskexeDetailLiftWorker;
import com.kaifantech.component.service.taskexe.detail.worker.KfTestTaskexeDetailWaitWorker;
import com.kaifantech.init.sys.params.AppSysParameters;
import com.kaifantech.util.constant.taskexe.ArrivedActType;

@Service
public class KfTestTaskexeDetailDealer extends FancyTaskexeDetailDealer {
	public boolean dealDetail(TaskexeBean taskexeBean, FancyAgvMsgBean fancyAgvMsgBean, TaskexeDetailBean taskexeDetail)
			throws Exception {
		if (ArrivedActType.WAIT.equals(taskexeDetail.getArrivedact())
				|| ArrivedActType.LIFT_READY.equals(taskexeDetail.getArrivedact())) {
			List<TaskexeDetailWorksBean> works = taskexeDetailWorksService.getWorks(taskexeDetail);
			if (ArrivedActType.WAIT.equals(taskexeDetail.getArrivedact())) {
				taskexeDetailWaitDealer.work(fancyAgvMsgBean, taskexeBean, taskexeDetail, works);
				return false;
			} else if (ArrivedActType.LIFT_READY.equals(taskexeDetail.getArrivedact())) {
				taskexeDetailWorksLiftDealer.work(fancyAgvMsgBean, taskexeBean, taskexeDetail, works);
				return false;
			}
		}

		AppSysParameters.setTaskstop(fancyAgvMsgBean.agvId(), false);
		return super.dealDetail(taskexeBean, fancyAgvMsgBean, taskexeDetail);
	}

	@Autowired
	private KfTestTaskexeDetailWaitWorker taskexeDetailWaitDealer;
	@Autowired
	private KfTestTaskexeDetailLiftWorker taskexeDetailWorksLiftDealer;
}
