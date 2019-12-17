package com.kaifantech.util.serial;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.util.thread.ThreadTool;
import com.calculatedfun.util.msg.AppMsg;

import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class LaoZigBeeNettySerialClient extends ZigBeeNettyClient {
	public LaoZigBeeNettySerialClient(IotClientBean iotClientBean) {
		super(iotClientBean);
	}

	public AppMsg sendCmd(String innerCmd) {
		if (!isConnected()) {
			return AppMsg.fail();
		}
		ThreadTool.sleep(200);
		AppMsg appMsg = super.sendCmd(innerCmd);
		return appMsg;
	}
}
