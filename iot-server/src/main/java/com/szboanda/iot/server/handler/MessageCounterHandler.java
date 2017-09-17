package com.szboanda.iot.server.handler;

import com.szboanda.iot.server.ServerContext;

import io.netty.buffer.Unpooled; 
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class MessageCounterHandler extends ChannelInboundHandlerAdapter{
	private ServerContext context = null;
	private int count = 0;
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
		String id = ctx.channel().id().asShortText(); 
		this.context.countPlus();
		this.context.countPlus(id);   
		this.count++;
		super.channelRead(ctx, msg);
	} 
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("local var id->" + ctx.channel().id().asShortText() + "->" + count); 
		super.channelInactive(ctx);  
	}

	@Override 
	public void channelActive(ChannelHandlerContext ctx) throws Exception { 
		super.channelActive(ctx);
		ctx.writeAndFlush(Unpooled.copiedBuffer("start",CharsetUtil.UTF_8));  
	}

	 @Override 
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
