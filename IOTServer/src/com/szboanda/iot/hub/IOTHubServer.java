package com.szboanda.iot.hub;

import com.szboanda.iot.hub.tools.ServerContext;
import com.szboanda.iot.hub.handler.GB211ServerChannelInitializer;
import com.szboanda.iot.hub.listener.ServerStatusPrinter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 
 * @company 深圳市博安达信息技术股份有限公司
 * @author 康庆
 * @version 2018-05-05
 * @since V0.1
 */
public class IOTHubServer {
	private static final int MAX_PORT = 66536;
	/**event loop group 中的线程数量，默认0 则由netty自行设置，实际值为java.lang.Runtime.availableProcessors()*2 */
	private int threads 			= 0;
	private int port 				= 8219;
	private EventLoopGroup group 	= null;
	private ServerBootstrap strap 	= null;
	private ServerContext context 	= null;
	
	public IOTHubServer(){
	}
	public IOTHubServer(int port){
		this.port = port;
		if(this.port > MAX_PORT){
			throw new IllegalArgumentException("无效的端口号：" + port);
		}
	}
	/**
	 * 初始化上下文环境和运行参数
	 */
	private void initContext(){
		if(this.threads < 0){
			throw new IllegalArgumentException("非法的event loop group threads参数：" + this.threads);
		}
		this.group = new NioEventLoopGroup(this.threads);
		this.strap = new ServerBootstrap();
		this.strap.group(group);
		this.strap.channel(NioServerSocketChannel.class);
		this.strap.localAddress(this.port); 
		this.context = new ServerContext(); 
		this.context.setServerStatusListener(new ServerStatusPrinter(this.context));
		ChannelHandler initializer = new GB211ServerChannelInitializer(this.context);
		this.strap.childHandler(initializer);   
	}
	
	
	private void destoryContext(){
		try{
			this.context.destory();
			this.context = null;
			this.group.shutdownGracefully().sync();
		}catch(Exception ff){
			System.out.println("destoryContext:" + ff.getMessage());
		}finally{
			this.group = null;
			this.strap = null;
		}
	}
	
	/**
	 * 启动Server
	 */
	public void start(){
		try{
			this.initContext();
			ChannelFuture f = this.strap.bind(this.port).sync();
			f.channel().closeFuture().sync();
		}catch(Exception ff){
			System.out.println("GB211Server.start:" + ff.getMessage());
			ff.printStackTrace();
		}finally{
			this.group.shutdownGracefully();
		}
	}

	/**
	 * 启动GB211Server
	 * @param args
	 */
	public static void main(String[] args) {
		IOTHubServer server = new IOTHubServer();
		server.start();
		
	}
	
	

}
