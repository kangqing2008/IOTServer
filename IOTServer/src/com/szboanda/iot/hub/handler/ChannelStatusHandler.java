package com.szboanda.iot.hub.handler;

import io.netty.channel.ChannelHandler.*;

import com.szboanda.iot.hub.tools.ServerContext;

import io.netty.channel.ChannelHandlerContext; 
import io.netty.channel.ChannelInboundHandlerAdapter;
/**
 * Server运行时channel连接和断开事件捕获
 * @author 康庆
 *
 */
@Sharable 
public class ChannelStatusHandler extends ChannelInboundHandlerAdapter {
	private ServerContext context;
	public ChannelStatusHandler(ServerContext context){ 
		this.context = context;
	}
	
	/**
	 * 捕获channelActive事件并将消息扩散到ServerContext中
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.context.fireChannelActive(ctx.channel().id().asShortText());
		ctx.fireChannelActive();  
	}
	 
	/**
	 * 捕获channelInactive事件并将消息扩散到ServerContext中
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.context.fireChannelInactive(ctx.channel().id().asShortText());
		ctx.fireChannelInactive();
	}
	
	/**
	 * 捕获消息读取事件
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		this.context.msgCounterPlus(ctx.channel().id().asShortText());
		ctx.fireChannelRead(msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}
	
}
