package com.kaifantech.component.comm.worker.server.agv;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.component.comm.worker.server.IServerWorker;
import com.kaifantech.component.service.iot.client.IIotClientService;
import com.kaifantech.init.sys.params.AppSysParameters;
import com.kaifantech.init.sys.qualifier.KfTestQualifier;
import com.kaifantech.init.sys.qualifier.UdfQualifier;
import com.kaifantech.util.constant.taskexe.ctrl.IotDevType;
import com.kaifantech.util.socket.base.IConnect;
import com.kaifantech.util.socket.netty.server.fancy.FancyAgvNettyServer;

@Service(KfTestQualifier.AGV_SERVER_WORKER)
public class KfTestAgvSimulatorWorker implements IServerWorker {
	private Map<Integer, IConnect> map = new HashMap<>();

	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_IOT_CLIENT_SERVICE)
	private IIotClientService iotClientService;

	public synchronized void init() {
		if (map == null || map.size() == 0) {
			for (IotClientBean iotClientBean : iotClientService.getAgvCacheList()) {
				if (!AppSysParameters.isLocalTest()) {
					return;
				}
				if (!IotDevType.AGV_FANCY_IP.equals(iotClientBean.getDevtype())
						&& !IotDevType.AGV_FANCY_ZIGBEE.equals(iotClientBean.getDevtype())) {
					continue;
				}
				try {
					IConnect server = null;
					if (IotDevType.AGV_FANCY_IP.equals(iotClientBean.getDevtype())) {
						server = new FancyAgvNettyServer(iotClientBean);
						try {
							server.init();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					map.put(iotClientBean.getId(), server);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized Map<Integer, IConnect> getMap() {
		return map;
	}
}
