package com.kaifantech.util.serial;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import com.kaifantech.util.hex.AppByteUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @ClassName:MessageEncoder
 * @author lujiafa
 * @email lujiafayx@163.com
 * @date 2017年1月12日
 * @Description: 自定义报文编码器
 */
public class MessageEncoder extends MessageToMessageEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
		if (msg == null)
			return;
		ByteBuf tb = null;
		if (msg instanceof byte[]) {
			tb = Unpooled.copiedBuffer((byte[]) msg);
		} else if (msg instanceof ByteBuf) {
			tb = (ByteBuf) msg;
		} else if (msg instanceof ByteBuffer) {
			tb = Unpooled.copiedBuffer((ByteBuffer) msg);
		} else {
			String ostr = msg.toString();
			tb = Unpooled.copiedBuffer(ostr, Charset.forName("UTF-8"));
		}
		byte[] pkg = new byte[4 + tb.readableBytes()];// 数据包
		byte[] header = AppByteUtil.intToBytes(tb.readableBytes());// 报文包头
		byte[] body = new byte[tb.readableBytes()];// 包体
		tb.readBytes(body);
		System.arraycopy(header, 0, pkg, 0, header.length);
		System.arraycopy(body, 0, pkg, header.length, body.length);
		out.add(Unpooled.copiedBuffer(pkg));
	}

}