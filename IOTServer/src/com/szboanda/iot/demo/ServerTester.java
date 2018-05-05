package com.szboanda.iot.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerTester {
	
	public static void main(String[] args)throws Exception{
		new ServerTester(20000).start();
	}
	
	private int port = -1;
	public ServerTester(int port){
		this.port = port;
	}
	public void start()throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(group);
			b.channel(NioServerSocketChannel.class);
			b.localAddress(this.port);
			b.childHandler(new ChannelInitializerTester());
			ChannelFuture f = b.bind().sync();
			System.out.println(ServerTester.class.getName() + " started and listen on :" + f.channel().localAddress());
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully().sync();
		}
	}
	
	

	private static class ChannelInitializerTester extends ChannelInitializer{

		@Override
		protected void initChannel(Channel ch) throws Exception {
			ch.pipeline().addLast(new ChannelHandlerAdapterTester());
		}
		
	}
	
	private static class ChannelHandlerAdapterTester extends ChannelInboundHandlerAdapter{
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("Server " + Thread.currentThread().getId() + " Recived:" + msg);
			ctx.write(msg);
		}
		
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}
	}
	
	private static class ChannelHandlerTester implements ChannelHandler{

		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			// TODO Auto-generated method stub
			
		}
		
	}
}
