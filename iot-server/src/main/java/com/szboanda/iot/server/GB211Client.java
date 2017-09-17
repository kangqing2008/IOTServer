package com.szboanda.iot.server;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class GB211Client implements Runnable{
	private EventLoopGroup group = null;
	private String host;
	private int	port;
	private CountDownLatch latch = null;
	private static AtomicLong counter = new AtomicLong(0);
	public GB211Client(EventLoopGroup group,String host,int port,CountDownLatch latch){
		this.group = group;
		this.host = host;
		this.port = port;
		this.latch = latch; 
	}
	@Override
	public void run() {
		this.start();
	}
	
	
	private void start() {
		try{
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			b.remoteAddress(new InetSocketAddress(this.host, this.port));
			b.handler(new ChannelInitializer<Channel>(){
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new ClientTesterHandler(GB211Client.this.latch));     
				}
			});
			b.connect();
			ChannelFuture f = b.connect().sync();
			f.channel().closeFuture().sync(); 
		}catch(Exception ff){
			ff.printStackTrace();
		}finally{
//			try{
//				group.shutdownGracefully().sync();
//			}catch(Exception ff){
//				ff.printStackTrace(); 
//			}
		}
	}
	
	private static class ClientTesterHandler extends ChannelInboundHandlerAdapter {
		
		private CountDownLatch latch = null;
		public ClientTesterHandler(CountDownLatch latch){
			this.latch = latch;
		}
 
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
			System.out.println("Client " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0] 
					+ " " + Thread.currentThread().getId() + " read:" + msg); 
			try{
				System.out.println("Client " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0] 
						+ " " + Thread.currentThread().getId() + " channel actived!");
				int i = 0; 
				while(i < 100){ 
					ChannelFuture cf = ctx.writeAndFlush(Unpooled.copiedBuffer("1",CharsetUtil.UTF_8)).sync();
//					System.out.println("Client " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0] 
//							+ " " + Thread.currentThread().getId() + " count:" + (i+1));    
					if(cf.isSuccess()){   
						i++;  
						counter.incrementAndGet();
						if(i%50==0){      
							Thread.sleep(100);     
						}
					} 
				}
				System.out.println("Client " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0] 
							+ " " + Thread.currentThread().getId() + " Finished!  count:" + counter.get());
			}finally{
				try{
					ctx.close().sync();    
				}finally{
					latch.countDown(); 
				}
			}
		}
		
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			try{
				ctx.close().sync(); 
			}finally{ 
				latch.countDown();
			} 
		}
		
	}
	
	
	public static void main(String[] args)throws Exception{ 
		int count = 10;    
		EventLoopGroup group = new NioEventLoopGroup(count);   
		//ExecutorService es = Executors.newCachedThreadPool(); 
		String host = "localhost";
		CountDownLatch latch = new CountDownLatch(count);
		int port = 8219; 
		for(int i=0;i<count;i++){
			//es.execute(new GB211Client(group, host, port));   
			//new Thread().start(); 
			//new Thread(new GB211Client(new NioEventLoopGroup(1),host,port)).start();
			//new GB211Client(new NioEventLoopGroup(1),host,port).start(); 
			if(i%50==0){ 
				Thread.sleep(100);    
			} 
			//new Thread(new GB211Client(new NioEventLoopGroup(1),host,port)).start();   
			new GB211Client(group,host,port,latch).start();    
		}  
		latch.await();
		group.shutdownGracefully().sync();  
		System.out.println("运行结束..."); 
	}
}
