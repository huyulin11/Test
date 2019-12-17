package com.kaifantech.util.serial;

import com.kaifantech.util.log.AppFileLogger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PureJavaCommClientHandler extends SimpleChannelInboundHandler<String> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush("AT\n");
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		if ("OK".equals(msg)) {
			AppFileLogger.warning("Serial port responded to AT");
		} else {
			AppFileLogger.warning("Serial port responded with not-OK: " + msg);
		}
		ctx.close();
	}
}