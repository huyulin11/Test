package com.kaifantech.component.service.taskexe.watch;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kaifantech.bean.msg.fancy.agv.FancyAgvMsgBean;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.bean.taskexe.TaskexeDetailBean;
import com.kaifantech.init.sys.qualifier.KfTestQualifier;

@Service(KfTestQualifier.LOCK_WATCHER)
public class KfTestLockWatcher extends AbsAcsLocksWatcher<FancyAgvMsgBean> {
	@Override
	public void watch(FancyAgvMsgBean agvMsg, TaskexeBean taskexe, List<TaskexeDetailBean> details) throws Exception {
	}
}
