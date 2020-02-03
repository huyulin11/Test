package com.kaifantech.util.socket.factory.client;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.util.constant.taskexe.ctrl.IotDevType;
import com.kaifantech.util.serial.LaoAgvZigBeeNettySerialClient;
import com.kaifantech.util.serial.LaoLightZigBeeNettySerialClient;
import com.kaifantech.util.socket.IConnect;
import com.kaifantech.util.socket.netty.client.fancy.FancyAgvNettyClient;

public class KfTestClientFactory {
	public static IConnect create(IotClientBean bean) {
		if (IotDevType.AGV_FANCY_IP.equals(bean.getDevtype())) {
			return new FancyAgvNettyClient(bean);
		} else if (IotDevType.AGV_FANCY_ZIGBEE.equals(bean.getDevtype())) {
			return new LaoAgvZigBeeNettySerialClient(bean);
		} else if (IotDevType.ZIGBEE_AUTODOOR.equals(bean.getDevtype())) {
			return new KfTestAutoDoorNettyClient(bean);
		} else if (IotDevType.ZIGBEE_LIFT.equals(bean.getDevtype())) {
			return new KfTestLiftNettyClient(bean);
		} else if (IotDevType.ZIGBEE_LIGHT.equals(bean.getDevtype())) {
			return new LaoLightZigBeeNettySerialClient(bean);
		}
		return null;
	}
}
