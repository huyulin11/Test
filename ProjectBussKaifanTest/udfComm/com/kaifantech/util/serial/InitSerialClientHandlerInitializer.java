package com.kaifantech.util.serial;

import java.util.concurrent.TimeUnit;

import com.kaifantech.util.serial.purejavacomm.PureJavaCommChannel;
import com.kaifantech.util.socket.netty.test.HeartbeatServerHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class InitSerialClientHandlerInitializer extends ChannelInitializer<PureJavaCommChannel> {
	private SimpleChannelInboundHandler<ByteBuf> handler = null;
	// // TODO 参考Message
	// // body(4)+zip(1)+cmdId(2)+type(1)=8
	// // 最大长度
	// private static final int MAX_FRAME_LENGTH = 1024 * 1024;
	// // 这个值就是MessageEncoder body.length(4)
	// private static final int LENGTH_FIELD_LENGTH = 4;
	// // 这个值就是MessageEncoder zip(1)+cmdId(2)+type(1)
	// private static final int LENGTH_FIELD_OFFSET = 4;
	// private static final int LENGTH_ADJUSTMENT = 0;
	// private static final int INITIAL_BYTES_TO_STRIP = 0;

	public InitSerialClientHandlerInitializer(SimpleChannelInboundHandler<ByteBuf> handler) {
		this.handler = handler;
	}

	@Override
	public void initChannel(PureJavaCommChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(handler);
		pipeline.addLast(new LineBasedFrameDecoder(32768));
		pipeline.addLast("idleStateHandler", new IdleStateHandler(2, 2, 60, TimeUnit.SECONDS));
		pipeline.addLast(new HeartbeatServerHandler());

		// pipeline.addLast(new MessageEncoder());
		// pipeline.addLast(new MessageDecoder());
	}

}