package com.kaifantech.util.socket.factory.client;

import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.init.sys.params.KfTestCacheKeys;
import com.kaifantech.util.hex.AppByteUtil;
import com.kaifantech.util.thread.ThreadTool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class KfTestLiftNettyClient extends KfTestAbstractNettyClient {
	private int seq = 0;

	public KfTestLiftNettyClient(IotClientBean iotClientBean) {
		super(iotClientBean);
		getHeartBeat().setSend(false);
	}

	public void dealData(ChannelHandlerContext ctx, ByteBuf in) {
		String msg = AppByteUtil.getHexStrFrom(in);
		if (!AppTool.isNull(msg) && msg.length() != 14) {
			AppCache.worker().hset(KfTestCacheKeys.liftMsgList(getIotClientBean().getId()), "" + seq++, msg);
			AppCache.worker().hset(KfTestCacheKeys.liftMsgKey(), "" + getIotClientBean().getId(), msg);
		}
	}

	public synchronized AppMsg sendCmd(String innerCmd) {
		if (!isConnected()) {
			return AppMsg.fail();
		}
		ThreadTool.sleep(200);
		AppMsg appMsg = super.sendCmdLao(innerCmd);
		AppCache.worker().hset(KfTestCacheKeys.liftCmd(getIotClientBean().getId()), "" + seq++, appMsg.getMsg());
		return appMsg;
	}
}
