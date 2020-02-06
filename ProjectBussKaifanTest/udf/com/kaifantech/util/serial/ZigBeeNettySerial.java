package com.kaifantech.util.serial;

import java.util.Enumeration;

import org.springframework.scheduling.annotation.Async;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.init.sys.params.AppSysParameters;
import com.kaifantech.util.hex.AppByteUtil;
import com.kaifantech.util.log.AppFileLogger;
import com.kaifantech.util.serial.purejavacomm.PureJavaCommChannel;
import com.kaifantech.util.serial.purejavacomm.PureJavaCommDeviceAddress;
import com.kaifantech.util.socket.IConnect;
import com.kaifantech.util.thread.ThreadTool;
import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.msg.AppMsg;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import purejavacomm.CommPortIdentifier;

@Sharable
public class ZigBeeNettySerial extends SimpleChannelInboundHandler<ByteBuf> implements IConnect {
	private EventLoopGroup group = null;
	private boolean isRunning = false;
	private boolean isConnected = false;
	private ChannelHandlerContext ctx;
	private IotClientBean iotClientBean;

	public int seq = 0;

	public IotClientBean getIotClientBean() {
		return iotClientBean;
	}

	public void setIotClientBean(IotClientBean iotClientBean) {
		this.iotClientBean = iotClientBean;
	}

	public AppMsg sendCmd(String innerCmd, IotClientBean iotClientBean) {
		return null;
	}

	public static final String ZIGBEE_MSG_HEADER = "FD00";
	public static final String ZIGBEE_NULL_ADDRESS = "0000";

	public static final String ZIGBEE_RECEIVE_DATA_HEADER = "FC";
	public static final String ZIGBEE_SEND_DATA_HEADER = "FB";
	public static final String ZIGBEE_RTN_DATA_HEADER = "04";
	public static final String ZIGBEE_SEND_DATA_TAIL = "FE";

	public static final int ZIGBEE_VALID_LENGTH = 14;

	public String getClientHeadStr() {
		return ZIGBEE_MSG_HEADER + getIotClientBean().getIp();
	}

	public String getClientHeadStr(IotClientBean iotClientBean) {
		return ZIGBEE_MSG_HEADER + iotClientBean.getIp();
	}

	public String getServerHeadStr() {
		return ZIGBEE_MSG_HEADER + ZIGBEE_NULL_ADDRESS;
	}

	public String getDataHeadStr(IotClientBean iotClientBean) {
		return ZIGBEE_RECEIVE_DATA_HEADER + iotClientBean.getIp();
	}

	public String getDataHeadStr2(IotClientBean iotClientBean) {
		return ZIGBEE_RTN_DATA_HEADER + iotClientBean.getIp();
	}

	public ZigBeeNettySerial getNettySerialClient() {
		return this;
	}

	public ZigBeeNettySerial(IotClientBean iotClientBean) {
		this.iotClientBean = iotClientBean;
	}

	public ZigBeeNettySerial(IotClientBean iotClientBean, boolean flag) {
		this.iotClientBean = iotClientBean;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void dealData(ChannelHandlerContext ctx, ByteBuf in) {
		String msgStr = AppByteUtil.getHexStrFrom(in);
		if (AppTool.isNull(msgStr)) {
			return;
		}
	}

	public AppMsg sendCmdLao(String msg, IotClientBean iotClientBean) {
		if (!AppTool.isNull(msg) && msg.length() % 2 == 0) {
			msg = iotClientBean.getIp() + AppByteUtil.intToHex2(msg.length() / 2) + msg;
			msg = msg + AppByteUtil.xorStep2(msg).toUpperCase();
			msg = ZIGBEE_SEND_DATA_HEADER + msg + ZIGBEE_SEND_DATA_TAIL;
		}
		AppMsg appMsg = sendCmd(msg);
		appMsg.setMsg(msg);
		return appMsg;
	}

	public AppMsg sendCmd(String msg) {
		AppMsg appMsg = new AppMsg();
		try {
			byte[] tosend = AppByteUtil.hexToBytes(msg);
			send(Unpooled.copiedBuffer(tosend));
			appMsg.setSuccess(true);
		} catch (Exception e) {
			appMsg.setSuccess(false);
		}
		return appMsg;
	}

	public void send(ByteBuf msg) {
		if (isRunning) {
			ctx.writeAndFlush(msg);
		}
	}

	public static void scanPort() {
		CommPortIdentifier portid = null;
		Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();
		while (e.hasMoreElements()) {
			portid = (CommPortIdentifier) e.nextElement();
			AppFileLogger.warning("found " + portid.getName());
		}
	}

	@Async
	public synchronized void init() {
		ThreadTool.run(() -> {
			loopInit();
		});
	}

	@Async
	private void loopInit() {
		while (true) {
			ThreadTool.sleep(1500);
			if (!AppSysParameters.isConnectIotClient()) {
				continue;
			}
			if (isRunning) {
				continue;
			}
			try {
				doInit();
			} catch (Exception e) {
				isRunning = false;
			}
		}
	}

	public void doInit() throws Exception {
		try {
			group = new OioEventLoopGroup();
			isRunning = true;
			Bootstrap b = new Bootstrap();
			b.group(group).channel(PureJavaCommChannel.class).handler(new InitSerialClientHandlerInitializer(this));
			ChannelFuture f = b.connect(new PureJavaCommDeviceAddress(iotClientBean.getPort())).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e1) {
			isRunning = false;
			e1.printStackTrace();
		} finally {
			closeResource();
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		isConnected = true;
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
		try {
			dealData(ctx, in);
		} catch (Exception e) {
			isRunning = false;
			ctx.close();
			e.printStackTrace();
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		isRunning = false;
		isConnected = false;
		super.channelInactive(ctx);
		ctx.close();
	}

	@Override
	public void closeResource() {
		isRunning = false;
		try {
			if (!AppTool.isNull(group)) {
				group.shutdownGracefully().sync();
				group = null;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
