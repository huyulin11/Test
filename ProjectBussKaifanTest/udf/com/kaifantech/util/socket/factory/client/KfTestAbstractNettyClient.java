package com.kaifantech.util.socket.factory.client;

import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.util.hex.AppByteUtil;
import com.kaifantech.util.socket.netty.client.AbstractNettyByteClient;

public abstract class KfTestAbstractNettyClient extends AbstractNettyByteClient {
	public static final String ZIGBEE_SEND_DATA_TAIL = "FE";
	public static final String ZIGBEE_RECEIVE_DATA_HEADER = "FC";

	public KfTestAbstractNettyClient(IotClientBean iotClientBean) {
		super(iotClientBean);
	}

	public AppMsg sendCmdLao(String msg) {
		if (!AppTool.isNull(msg) && msg.length() % 2 == 0) {
			msg = AppByteUtil.intToHex2(msg.length() / 2) + msg;
			msg = msg + AppByteUtil.xorStep2(msg).toUpperCase();
			msg = ZIGBEE_RECEIVE_DATA_HEADER + msg + ZIGBEE_SEND_DATA_TAIL;
		}
		AppMsg appMsg = super.sendCmd(msg);
		appMsg.setMsg(msg);
		return appMsg;
	}
}
