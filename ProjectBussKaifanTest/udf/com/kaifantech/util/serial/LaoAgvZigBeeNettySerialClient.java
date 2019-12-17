package com.kaifantech.util.serial;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvCmdBean;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvMsgBean;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.cache.manager.AppConf;
import com.kaifantech.init.sys.params.FancyCacheKeys;
import com.kaifantech.util.constants.cmd.FancyAgvCmdConstant;
import com.kaifantech.util.log.AppFileLogger;
import com.kaifantech.util.thread.ThreadTool;
import com.calculatedfun.util.msg.AppMsg;

import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class LaoAgvZigBeeNettySerialClient extends ZigBeeNettyClient {
	public LaoAgvZigBeeNettySerialClient(IotClientBean iotClientBean) {
		super(iotClientBean);
	}

	private void dealMsgRegular(String agvMsgStr) {
		FancyAgvMsgBean agvMsg = new FancyAgvMsgBean(agvMsgStr);
		int numId = agvMsg.getMsgNumId();

		AppCache.worker().hset(FancyCacheKeys.agvMsgList(getIotClientBean().getId(), numId), "" + numId, agvMsgStr);
		AppCache.worker().hset(FancyCacheKeys.agvMsgType(getIotClientBean().getId()), "" + agvMsg.getMsgType(),
				agvMsgStr);
		if (FancyAgvCmdConstant.CMD_TASK_CACHE.equals(agvMsg.getMsgCacheType())
				|| FancyAgvCmdConstant.CMD_TASK_DELETE_ONE_CACHE.equals(agvMsg.getMsgCacheType())
				|| FancyAgvCmdConstant.CMD_TASK_CLEAR_ALL_CACHE.equals(agvMsg.getMsgCacheType())) {
			AppCache.worker().hset(FancyCacheKeys.agvMsgCache(getIotClientBean().getId()), agvMsg.getMsgId(),
					agvMsgStr);
		}
	}

	@SuppressWarnings("unused")
	private void dealMsgUnRegular(String agvMsgStr) {
		if (agvMsgStr.length() % FancyAgvMsgBean.VALID_LENGTH == 0) {
			int start = 0;
			int ii = agvMsgStr.length() / FancyAgvMsgBean.VALID_LENGTH;
			while (start < ii - 1) {
				String ss = agvMsgStr.substring(FancyAgvMsgBean.VALID_LENGTH * start,
						FancyAgvMsgBean.VALID_LENGTH * (start + 1));
				dealMsgRegular(ss);
				start++;
			}
			return;
		}

		String reduce = agvMsgStr;
		if (agvMsgStr.length() % FancyAgvMsgBean.VALID_LENGTH != 0) {
			while (true) {
				int index = reduce.indexOf(FancyAgvCmdConstant.PREFIX);
				if (index < 0) {
					break;
				}
				reduce = reduce.substring(index);
				if (reduce.length() < FancyAgvMsgBean.VALID_LENGTH) {
					break;
				}
				String ss = reduce.substring(0, FancyAgvMsgBean.VALID_LENGTH);
				if (FancyAgvMsgBean.isValid(ss)) {
					reduce = reduce.substring(FancyAgvMsgBean.VALID_LENGTH);
					dealMsgRegular(ss);
				} else if (index == 0) {
					reduce = reduce.substring(1);
				}
			}
		}
		AppFileLogger.warning(0 + "号AGV返回非法数据：ORG:" + agvMsgStr + ";DEAL:" + reduce);
	}

	private int i = 0;

	public AppMsg sendCmd(String innerCmd) {
		if (!getCommObj(getIotClientBean()).isConnected()) {
			return AppMsg.fail();
		}
		ThreadTool.sleep(200);
		FancyAgvCmdBean agvCmd = new FancyAgvCmdBean(innerCmd);
		if (agvCmd.isValid()) {
			int numId = agvCmd.getMsgNumId();
			String key = FancyCacheKeys.agvCmdList(getIotClientBean().getId(), numId);
			try {
				AppCache.worker().hset(key, "" + numId, innerCmd);
				AppCache.worker().hset(FancyCacheKeys.agvCmdType(getIotClientBean().getId()), "" + agvCmd.getCmdType(),
						innerCmd);
				if (FancyAgvCmdConstant.CMD_TASK_CACHE.equals(agvCmd.getCmdCacheType())
						|| FancyAgvCmdConstant.CMD_TASK_DELETE_ONE_CACHE.equals(agvCmd.getCmdCacheType())
						|| FancyAgvCmdConstant.CMD_TASK_CLEAR_ALL_CACHE.equals(agvCmd.getCmdCacheType())) {
					i = FancyAgvCmdConstant.CMD_TASK_CLEAR_ALL_CACHE.equals(agvCmd.getCmdCacheType()) ? 0 : i + 1;
					AppCache.worker().hset(FancyCacheKeys.agvCmdCache(getIotClientBean().getId()),
							i + "-" + agvCmd.getSiteId(), innerCmd);
				}
			} catch (Exception e) {
				System.err.println("key:" + key + ",numId" + numId);
				e.printStackTrace();
			}
		}
		AppConf.worker().hset("lao_cache_send_" + getIotClientBean().getId() + "_" + "1", innerCmd, innerCmd);
		AppMsg appMsg = getCommObj(getIotClientBean()).sendCmd(innerCmd, getIotClientBean());
		AppConf.worker().hset("lao_cache_send_" + getIotClientBean().getId() + "_" + "2", innerCmd, innerCmd);
		return appMsg;
	}

	public void dealData(String msg) {
		super.dealData(msg);
		if (FancyAgvMsgBean.isValid(msg)) {
			FancyAgvMsgBean agvMsgBean = new FancyAgvMsgBean(msg);
			if (!agvMsgBean.agvId().equals(getIotClientBean().getId())) {
				return;
			}
			// System.err.println(DateFactory.getCurrTime(null) + "4-AGV-CLIENT
			// R:" + msg);
			dealMsgRegular(msg);
			return;
		}
	}
}
