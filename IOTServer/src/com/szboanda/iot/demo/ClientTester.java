package com.szboanda.iot.demo;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class ClientTester {
	public static void main(String[] args)throws Exception{
		new ClientTester("localhost", 20000).start();;
	}
	
	private String host;
	private int    port;
	public ClientTester(String host,int port){
		this.host = host;
		this.port = port;
	}
	
	public void start()throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		
		
	}
	
	
	
}
