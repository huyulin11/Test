package com.kaifantech.component.service.taskexe.lock;

import org.springframework.stereotype.Service;

import com.kaifantech.bean.info.agv.AgvInfoBean;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.init.sys.qualifier.KfTestQualifier;

@Service(KfTestQualifier.LOCK_WATCHER)
public class KfTestLockWatcher extends AbsFancyLockWatcher {
	@Override
	public void watch(IotClientBean iotClientBean, AgvInfoBean agvInfo) throws Exception {

	}
}
