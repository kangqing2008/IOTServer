package com.szboanda.iot.server.handler;

import com.szboanda.iot.server.ServerContext;
import io.netty.channel.ChannelHandler.*;
import io.netty.channel.ChannelHandlerContext; 
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class MessageCounterHandler extends ChannelInboundHandlerAdapter {
	private ServerContext context = null;
	public MessageCounterHandler(ServerContext context){
		this.context = context;  
	} 
//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		this.context.countPlus(); 
//		super.channelReadComplete(ctx);
//	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		this.context.countPlus(); 
		super.channelRead(ctx, msg);
	}
}
