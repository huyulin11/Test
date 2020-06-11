package com.kaifantech.component.service.taskexe.detail.dealer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaifantech.bean.msg.agv.FancyAgvMsgBean;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.bean.taskexe.TaskexeDetailBean;
import com.kaifantech.bean.taskexe.TaskexeDetailWorksBean;
import com.kaifantech.component.service.taskexe.detail.worker.KfTestTaskexeDetailLiftWorker;
import com.kaifantech.component.service.taskexe.detail.worker.KfTestTaskexeDetailWaitWorker;
import com.kaifantech.init.sys.params.AppSysParameters;
import com.kaifantech.util.constant.taskexe.ArrivedActType;

@Service
public class KfTestTaskexeDetailDealer extends FancyTaskexeDetailDealer {
	public boolean dealDetail(FancyAgvMsgBean agvMsg, TaskexeBean taskexe, TaskexeDetailBean detail) throws Exception {
		if (ArrivedActType.WAIT.equals(detail.getArrivedact())
				|| ArrivedActType.LIFT_READY.equals(detail.getArrivedact())) {
			List<TaskexeDetailWorksBean> works = taskexeDetailWorksService.getWorks(detail);
			if (ArrivedActType.WAIT.equals(detail.getArrivedact())) {
				taskexeDetailWaitDealer.work(agvMsg, taskexe, detail, works);
				return false;
			} else if (ArrivedActType.LIFT_READY.equals(detail.getArrivedact())) {
				taskexeDetailWorksLiftDealer.work(agvMsg, taskexe, detail, works);
				return false;
			}
		}

		AppSysParameters.setTaskstop(agvMsg.agvId(), false);
		return super.dealDetail(agvMsg, taskexe, detail);
	}

	@Autowired
	private KfTestTaskexeDetailWaitWorker taskexeDetailWaitDealer;
	@Autowired
	private KfTestTaskexeDetailLiftWorker taskexeDetailWorksLiftDealer;
}
