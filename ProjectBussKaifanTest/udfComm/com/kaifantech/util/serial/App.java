package com.kaifantech.util.serial;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.util.thread.ThreadTool;

public class App {
	public static void main(String[] args) throws Exception {
		ZigBeeNettyClient client = new LaoZigBeeNettySerialClient(new IotClientBean("", "COM55"));
		ThreadTool.run(() -> {
			try {
				client.doInit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		ThreadTool.sleep(5000);
		while (true) {
			client.sendCmd("0908");
			ThreadTool.sleep(1000);
		}
	}
}