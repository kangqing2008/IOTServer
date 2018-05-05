package com.szboanda.iot.server.handler;

import com.szboanda.iot.server.NettyUtils;
import com.szboanda.iot.server.ServerContext;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Channel初始化时需要执行的操作
 * 主要是执行往channel的handler pipeline中绑定必须要执行的handler操作
 * @author 康庆
 *
 */
public class GB211ServerChannelInitializer extends ChannelInitializer<SocketChannel>{
	private ServerContext context = null; 
	/**用于捕获channel active和inactive事件的handler*/
	private ChannelHandler statusHandler = null;
	private StringDecoder  stringDecoder = null;
	public GB211ServerChannelInitializer(ServerContext context) {
		this.context = context;
		this.statusHandler = new ChannelStatusHandler(this.context);   
		this.stringDecoder = new StringDecoder(NettyUtils.UTF_8);
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new LineBasedFrameDecoder(2048));
		ch.pipeline().addLast(this.stringDecoder);
		ch.pipeline().addLast(statusHandler);   
	}
	
}
