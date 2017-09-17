package com.szboanda.iot.server.handler;

import io.netty.channel.ChannelHandler.*;

import com.szboanda.iot.server.ServerContext;

import io.netty.channel.ChannelHandlerContext; 
import io.netty.channel.ChannelInboundHandlerAdapter;
/**
 * Server运行时channel连接和断开事件捕获
 * @author 康庆
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
		this.context.fireChannelActive();
		ctx.fireChannelActive();  
	}
	 
	/**
	 * 捕获channelInactive事件并将消息扩散到ServerContext中
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.context.fireChannelInactive();
		ctx.fireChannelInactive();
	}
	
}
