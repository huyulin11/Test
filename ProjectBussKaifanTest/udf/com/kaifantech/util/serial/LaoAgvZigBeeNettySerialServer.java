package com.kaifantech.util.serial;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvCmdBean;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvCommBean;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvMsgBean;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.init.sys.params.CacheKeys;
import com.kaifantech.init.sys.params.AppSysParameters;
import com.kaifantech.util.constants.cmd.FancyAgvCmdConstant;
import com.kaifantech.util.hex.AppByteUtil;
import com.kaifantech.util.iot.FancyAgvStatus;
import com.kaifantech.util.thread.ThreadTool;
import com.calculatedfun.util.AppTool;

import io.netty.channel.ChannelHandlerContext;

public class LaoAgvZigBeeNettySerialServer extends ZigBeeNettyServer {
	public LaoAgvZigBeeNettySerialServer(IotClientBean iotClientBean) {
		super(iotClientBean);
	}

	private String getCurrentSite(Integer agvId) {
		int siteid = Integer.parseInt(AppCache.worker().getOrInit(CacheKeys.simulatorCurrentSite(), "" + agvId, "1"));
		return AppByteUtil.intToStr4(siteid);
	}

	private String getCurrentSpeed(Integer agvId) {
		return AppCache.worker().getOrInit(CacheKeys.simulatorCurrentSpeed(), "" + agvId, "00");
	}

	private String getCurrentBattery(Integer agvId) {
		return AppCache.worker().getOrInit(CacheKeys.simulatorCurrentBattery(), "" + agvId, "1803");
	}

	private String getMsgByCmd(String cmdReceived) {
		String msgSend = "";
		if (FancyAgvCommBean.isValid(cmdReceived)) {
			FancyAgvCmdBean cmdBean = new FancyAgvCmdBean(cmdReceived);
			String agvIdStr = cmdBean.getAgvId();
			Integer agvId = Integer.parseInt(agvIdStr);
			String currentCmdId = cmdBean.getMsgId();
			String cmdType = cmdBean.getCmdType();
			if (cmdType.equals(FancyAgvCommBean.TASK)) {
				msgSend = FancyAgvMsgBean.MSG_GENERAL_TYPE + FancyAgvCommBean.TASK + FancyAgvCommBean.SUCCESS
						+ getCurrentSite(agvId);
			} else if (cmdType.equals(FancyAgvCommBean.CONTINUE)) {
				setPause(false);
				msgSend = FancyAgvMsgBean.MSG_GENERAL_TYPE + FancyAgvCommBean.CONTINUE + FancyAgvCommBean.SUCCESS
						+ getCurrentSite(agvId);
			} else if (cmdType.equals(FancyAgvCommBean.STOP)) {
				setPause(true);
				msgSend = FancyAgvMsgBean.MSG_GENERAL_TYPE + FancyAgvCommBean.STOP + FancyAgvCommBean.SUCCESS
						+ getCurrentSite(agvId);
			} else if (cmdType.equals(FancyAgvCommBean.CACHE)) {
				msgSend = FancyAgvMsgBean.MSG_GENERAL_TYPE + FancyAgvCommBean.CACHE + FancyAgvCommBean.SUCCESS
						+ getCurrentSite(agvId);
			} else {
				msgSend = FancyAgvMsgBean.MSG_STATUS_SEARCH_TYPE
						+ (isPause() ? FancyAgvStatus.STOPPING.get() : FancyAgvStatus.DRIVING.get())
						+ getCurrentSite(agvId);
			}
			msgSend = String.format("%-40s", msgSend).replaceAll("\\s", "0");
			msgSend = FancyAgvCmdConstant.PREFIX + agvIdStr + currentCmdId + msgSend;
			msgSend = msgSend.substring(0, 32) + getCurrentSpeed(agvId) + msgSend.substring(34, msgSend.length() - 8)
					+ getCurrentBattery(agvId) + "0000";
			msgSend = msgSend + FancyAgvCommBean.getFancyAGVCheckNumStr(msgSend) + FancyAgvCmdConstant.SUFFIX;
		}
		return msgSend.toUpperCase();
	}

	public void dealData(String msg) {
		if (!msg.startsWith(getClientHeadStr())) {
			return;
		}
		msg = msg.replace(getClientHeadStr(), "");
		if (!AppTool.isNull(msg)) {
			setCmd(msg);
			String msgSend = getMsgByCmd(msg);
			if (!AppTool.isNull(msgSend)) {
				getCommObj(getIotClientBean()).sendCmd(msgSend, getIotClientBean().getIp());
			}
		}
	}

	public void loopSend(ChannelHandlerContext ctx) {
		while (true) {
			ThreadTool.sleep(2000);
			if (!AppSysParameters.isConnectIotServer()) {
				ctx.close();
				setRunning(false);
				break;
			}
			try {
				String cmd = getCmd();
				if (!AppTool.isNull(cmd)) {
					getCommObj(getIotClientBean()).sendCmd(cmd, getIotClientBean());
				}
			} catch (Exception e) {
				closeResource();
				setRunning(false);
				try {
					init();
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		}
	}

	public void doSendToClient(ChannelHandlerContext ctx) {
		ThreadTool.run(() -> {
			loopSend(ctx);
		});
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		doSendToClient(ctx);
		super.channelActive(ctx);
	}

	private boolean isRunning = false;

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	private boolean isPause = false;

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

}