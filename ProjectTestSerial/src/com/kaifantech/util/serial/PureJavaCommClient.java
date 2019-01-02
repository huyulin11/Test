package com.kaifantech.util.serial;

import java.util.Enumeration;

import com.kaifantech.util.serial.purejavacomm.PureJavaCommChannel;
import com.kaifantech.util.serial.purejavacomm.PureJavaCommDeviceAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import purejavacomm.CommPortIdentifier;

/**
 * Sends one message to a serial device
 */
public final class PureJavaCommClient {

	public static void scan() {
		CommPortIdentifier portid = null;
		Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();
		while (e.hasMoreElements()) {
			portid = (CommPortIdentifier) e.nextElement();
			System.out.println("found " + portid.getName());
		}
	}

	public static void main(String[] args) {
		EventLoopGroup group = new OioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(PureJavaCommChannel.class).handler(new ChannelInitializer<PureJavaCommChannel>() {
				@Override
				public void initChannel(PureJavaCommChannel ch) throws Exception {
					ch.pipeline().addLast(new LineBasedFrameDecoder(32768), new StringEncoder(), new StringDecoder(),
							new PureJavaCommClientHandler());
				}
			});

			ChannelFuture f;
			try {
				f = b.connect(new PureJavaCommDeviceAddress("COM55")).sync();
				f.channel().closeFuture().sync();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} finally {
			group.shutdownGracefully();
		}
	}

	private PureJavaCommClient() {
	}
}