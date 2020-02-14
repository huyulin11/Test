package com.kaifantech.util.socket.factory.client;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.util.constant.taskexe.ctrl.IotDevType;
import com.kaifantech.util.socket.base.IConnect;
import com.kaifantech.util.socket.netty.client.fancy.FancyAgvNettyClient;

public class KfTestClientFactory {
	public static IConnect create(IotClientBean bean) {
		if (IotDevType.AGV_FANCY_IP.equals(bean.getDevtype())) {
			return new FancyAgvNettyClient(bean);
		}
		return null;
	}
}
