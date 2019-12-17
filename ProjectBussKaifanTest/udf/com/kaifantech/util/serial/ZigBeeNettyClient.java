package com.kaifantech.util.serial;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvCommBean;
import com.kaifantech.cache.manager.AppConf;
import com.kaifantech.util.constants.cmd.FancyAgvCmdConstant;
import com.kaifantech.util.hex.AppByteUtil;
import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.DateFactory;
import com.calculatedfun.util.msg.AppMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

@Sharable
public class ZigBeeNettyClient extends ZigBeeNettySerial implements java.util.Observer {
	private static Map<String, ZigBeeNettyClient> serialClientMap = new HashMap<>();
	private static Map<String, ZigBeeClientMsgObservable> observableMap = new HashMap<>();

	private static Object lock = new Object();

	public static ZigBeeNettyClient getCommObj(IotClientBean iotClientBean) {
		synchronized (lock) {
			ZigBeeNettyClient obj = serialClientMap.get(iotClientBean.getPort());
			if (!AppTool.isNull(obj)) {
				return obj;
			}
			obj = new ZigBeeNettyClient(iotClientBean, true);
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

	public AppMsg sendCmd(String innerCmd, IotClientBean iotClientBean) {
		String finalCmd = getClientHeadStr(iotClientBean) + innerCmd;
		AppMsg appMsg = sendCmd(finalCmd);
		appMsg.setMsg(innerCmd);
		return appMsg;
	}

	public ZigBeeNettyClient(IotClientBean iotClientBean) {
		super(iotClientBean);
		getCommObj(iotClientBean);
		getObservable(iotClientBean).addObserver(this);
	}

	public ZigBeeNettyClient(IotClientBean iotClientBean, boolean flag) {
		super(iotClientBean, flag);
	}

	private String msg = "";

	public void dealData(ChannelHandlerContext ctx, ByteBuf in) {
		String msgStr = AppByteUtil.getHexStrFrom(in);
		if (AppTool.isNull(msgStr)) {
			return;
		}
		AppConf.worker().hset("LAO_CACHE_FULL", DateFactory.getCurrTime(null), msgStr);
		msg = msg + msgStr;
		AppConf.worker().hset("LAO_CACHE_ADDED", DateFactory.getCurrTime(null), msg);
		while (true) {
			String ss = "";
			int indexOfFC = msg.indexOf(ZIGBEE_RECEIVE_DATA_HEADER);
			indexOfFC = indexOfFC < 0 ? 999 : indexOfFC;
			String endOfFC = msg.length() < indexOfFC + ZIGBEE_VALID_LENGTH ? ""
					: msg.substring(indexOfFC + ZIGBEE_VALID_LENGTH - 2, indexOfFC + ZIGBEE_VALID_LENGTH);
			int indexOf04 = msg.indexOf(ZIGBEE_RTN_DATA_HEADER);
			indexOf04 = indexOf04 < 0 ? 999 : indexOf04;
			String endOf04 = msg.length() < indexOf04 + ZIGBEE_VALID_LENGTH ? ""
					: msg.substring(indexOf04 + ZIGBEE_VALID_LENGTH - 2, indexOf04 + ZIGBEE_VALID_LENGTH);
			int indexOf55 = msg.indexOf(FancyAgvCmdConstant.PREFIX);
			indexOf55 = indexOf55 < 0 ? 999 : indexOf55;
			String endOf55 = msg.length() < indexOf55 + FancyAgvCommBean.VALID_LENGTH ? ""
					: msg.substring(indexOf55 + FancyAgvCommBean.VALID_LENGTH - 2,
							indexOf55 + FancyAgvCommBean.VALID_LENGTH);
			int min = Math.min(indexOfFC, Math.min(indexOf04, indexOf55));
			if (indexOfFC <= min && ZIGBEE_SEND_DATA_TAIL.equals(endOfFC)) {
				ss = msg.substring(indexOfFC, indexOfFC + ZIGBEE_VALID_LENGTH);
				msg = msg.substring(indexOfFC + ZIGBEE_VALID_LENGTH);
			} else if (indexOf04 <= min && ZIGBEE_SEND_DATA_TAIL.equals(endOf04)) {
				ss = msg.substring(indexOf04, indexOf04 + ZIGBEE_VALID_LENGTH);
				msg = msg.substring(indexOf04 + ZIGBEE_VALID_LENGTH);
			} else if (indexOf55 <= min && FancyAgvCmdConstant.SUFFIX.equals(endOf55)) {
				ss = msg.substring(indexOf55, indexOf55 + FancyAgvCommBean.VALID_LENGTH);
				msg = msg.substring(indexOf55 + FancyAgvCommBean.VALID_LENGTH);
			} else {
				if (msg.length() > 150) {
					msg = "";
				}
				return;
			}

			// if ((msgStr.startsWith(ZIGBEE_RECEIVE_DATA_HEADER) ||
			// msgStr.startsWith(ZIGBEE_RTN_DATA_HEADER))
			// && msgStr.length() >= ZIGBEE_VALID_LENGTH) {
			// ss = msgStr.substring(0, ZIGBEE_VALID_LENGTH);
			// msgStr = msgStr.substring(ZIGBEE_VALID_LENGTH);
			// } else if (msgStr.startsWith(FancyAgvCmdConstant.PREFIX)
			// && msgStr.length() >= FancyAgvCommBean.VALID_LENGTH) {
			// ss = msgStr.substring(0, FancyAgvCommBean.VALID_LENGTH);
			// msgStr = msgStr.substring(FancyAgvCommBean.VALID_LENGTH);
			// } else {
			// break;
			// }
			AppConf.worker().hset("LAO_CACHE", DateFactory.getCurrTime(null), ss);
			getObservable(getIotClientBean()).msg(ss);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		String msg = AppTool.isNull(arg) ? "" : arg.toString().toUpperCase();
		// if (!msg.startsWith(getServerHeadStr())) {
		// return;
		// }
		msg = msg.replaceAll(getServerHeadStr(), "");
		dealData(msg);
	}

	public void dealData(String msg) {
	}
}
