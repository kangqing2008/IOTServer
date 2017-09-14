package com.szboanda.iot.demo;

import com.szboanda.iot.server.NettyUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class InboundHandler1 extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { 
		super.channelActive(ctx);
		System.out.println(NettyUtils.channelInfo(ctx.channel()) + "--->InboundHandler1.channelActive:" );   
	}
	
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//super.channelRead(ctx, msg); 
		System.out.println(NettyUtils.channelInfo(ctx.channel()) + "InboundHandler1.channelRead...");
		ByteBuf buffer = (ByteBuf)msg;
		byte[] result = new byte[buffer.readableBytes()];
		buffer.readBytes(result);
		String message = new String(result);    
		//buffer.release(); 
		System.out.println(NettyUtils.channelInfo(ctx.channel()) + "InboundHandler1.channelRead..." + message); 
		ctx.fireChannelRead(msg); 
	} 
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//super.channelReadComplete(ctx);
		System.out.println(NettyUtils.channelInfo(ctx.channel()) +  "InboundHandler1.channelReadComplete...");
		ctx.flush();
	}
}
