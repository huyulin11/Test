package com.kaifantech.component.comm.worker.client.light;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.calculatedfun.util.AppTool;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.component.comm.worker.client.IConnectWorker;
import com.kaifantech.component.service.iot.client.IIotClientService;
import com.kaifantech.init.sys.qualifier.UdfQualifier;
import com.kaifantech.init.sys.qualifier.KfTestQualifier;
import com.kaifantech.util.constant.taskexe.ctrl.IotDevType;
import com.kaifantech.util.socket.IConnect;
import com.kaifantech.util.socket.factory.client.KfTestClientFactory;

@Service(KfTestQualifier.LIGHT_CLIENT_WORKER)
public class KfTestLightClientWorker implements IConnectWorker {
	private Map<Integer, IConnect> map = new HashMap<Integer, IConnect>();

	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_IOT_CLIENT_SERVICE)
	private IIotClientService iotClientService;

	public synchronized void init() {
		if (AppTool.isNull(map)) {
			for (IotClientBean iotClientBean : iotClientService.getList()) {
				if (!IotDevType.ZIGBEE_LIGHT.equals(iotClientBean.getDevtype())) {
					continue;
				}
				IConnect client = KfTestClientFactory.create(iotClientBean);
				map.put(iotClientBean.getId(), client);
			}
		}
	}

	public synchronized Map<Integer, IConnect> getMap() {
		init();
		return map;
	}
}
