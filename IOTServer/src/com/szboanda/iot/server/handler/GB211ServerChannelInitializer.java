package com.szboanda.iot.server.handler;

import com.szboanda.iot.server.ServerContext;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Channel初始化时需要执行的操作
 * 主要是执行往channel的handler pipeline中绑定必须要执行的handler操作
 * @author 康庆
 *
 */
public class GB211ServerChannelInitializer extends ChannelInitializer<SocketChannel>{
	private ServerContext context = null; 
	/**用于捕获channel active和inactive事件的handler*/
	private ChannelHandler activeHandler = null;
	private ChannelHandler messageCounter = null;
	public GB211ServerChannelInitializer(ServerContext context) {
		this.context = context;
		this.activeHandler = new ChannelStatusHandler(this.context);   
		this.messageCounter = new MessageCounterHandler(this.context);
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(activeHandler);  
		ch.pipeline().addLast(messageCounter);  
	}
	
}
