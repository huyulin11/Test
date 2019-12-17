package com.kaifantech.util.serial;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.util.hex.AppByteUtil;
import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.msg.AppMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

@Sharable
public class ZigBeeNettyServer extends ZigBeeNettySerial implements java.util.Observer {
	private static Map<String, ZigBeeNettyServer> serialClientMap = new HashMap<>();
	private static Map<String, ZigBeeClientMsgObservable> observableMap = new HashMap<>();

	private static Object lock = new Object();

	public static ZigBeeNettyServer getCommObj(IotClientBean iotClientBean) {
		synchronized (lock) {
			ZigBeeNettyServer obj = serialClientMap.get(iotClientBean.getPort());
			if (!AppTool.isNull(obj)) {
				return obj;
			}
			obj = new ZigBeeNettyServer(iotClientBean, true);
			serialClientMap.put(iotClientBean.getPort(), obj);
			obj.init();
			return obj;
		}
	}

	private static ZigBeeClientMsgObservable getObservable(IotClientBean iotClientBean) {
		synchronized (lock) {
			ZigBeeClientMsgObservable obj = observableMap.get(iotClientBean.getPort());
			if (!AppTool.isNull(obj)) {
				return obj;
			}
			obj = new ZigBeeClientMsgObservable();
			observableMap.put(iotClientBean.getPort(), obj);
			return obj;
		}
	}

	public AppMsg sendCmd(String innerCmd, String host) {
		String finalCmd = innerCmd;
		AppMsg appMsg = sendCmd(finalCmd);
		appMsg.setMsg(innerCmd);
		return appMsg;
	}

	public ZigBeeNettyServer(IotClientBean iotClientBean) {
		super(iotClientBean);
		getCommObj(iotClientBean);
		getObservable(getIotClientBean()).addObserver(this);
	}

	public ZigBeeNettyServer(IotClientBean iotClientBean, boolean flag) {
		super(iotClientBean, flag);
	}

	public void dealData(ChannelHandlerContext ctx, ByteBuf in) {
		String msgStr = AppByteUtil.getHexStrFrom(in);
		if (AppTool.isNull(msgStr)) {
			return;
		}
		msgStr = msgStr.replaceAll(ZIGBEE_MSG_HEADER, "," + ZIGBEE_MSG_HEADER);
		String[] msgs = msgStr.split(",");
		for (String msg : msgs) {
			if (AppTool.isNull(msg)) {
				continue;
			}
			getObservable(getIotClientBean()).msg(msg);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		String msg = AppTool.isNull(arg) ? "" : arg.toString().toUpperCase();
		dealData(msg);
	}

	public void dealData(String msg) {
	}
}
