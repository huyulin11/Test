package com.kaifantech.component.service.taskexe.watch;

import org.springframework.stereotype.Service;

import com.kaifantech.bean.info.agv.AgvInfoBean;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.bean.msg.agv.FancyAgvMsgBean;
import com.kaifantech.init.sys.qualifier.KfTestQualifier;

@Service(KfTestQualifier.LOCK_WATCHER)
public class KfTestLockWatcher extends AbsAcsLockWatcher<FancyAgvMsgBean> {
	@Override
	public void watch(IotClientBean iotClientBean, AgvInfoBean agvInfo) throws Exception {

	}
}
