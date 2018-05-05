package com.szboanda.iot.server;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.szboanda.iot.server.listener.IServerStatusListener;
import com.szboanda.iot.server.listener.IServerStatusListener.Event;

public class ServerStatus implements Serializable {
	private AtomicBoolean running = new AtomicBoolean(false); 
	private AtomicInteger waiting = new AtomicInteger(0);
	private AtomicInteger channels = new AtomicInteger(0);
	private AtomicLong	  counter  = new AtomicLong(0);
	private Map<String,AtomicLong> allCounter = new ConcurrentHashMap<String,AtomicLong>();
	private IServerStatusListener listener = null; 
	private ServerContext context = null; 
	
	/**
	 * 使用ServerContext初始化ServerStatus
	 * @param context
	 */
	public ServerStatus(ServerContext context){
		this(context,null); 
	}
	
	private void init(ServerContext context,IServerStatusListener listener){
		this.context = context;
		this.running = new AtomicBoolean(false);
		this.waiting = new AtomicInteger(0);
		this.listener = listener;
		this.channels = new AtomicInteger(0);
		this.counter = new AtomicLong(0);
		this.allCounter = new ConcurrentHashMap<String,AtomicLong>();
	}
	
	/**
	 * 使用ServerContext和IServerStatusListener创建ServerStatus 
	 * @param context
	 * @param listener
	 */
	public ServerStatus(ServerContext context,IServerStatusListener listener){
		this.init(context,listener);
	}
	
	
	/**
	 * 提供外部触发channelActive的入口方法
	 * @param channelId
	 * @return 当前连接的channel数量
	 */
	public int channelActive(String channelId){
		int count = channels.incrementAndGet();
		this.allCounter.put(channelId, new AtomicLong(0));
		if(this.listener != null){
			this.context.runTask(new FireChannelActiveRunner(this.listener,channelId,count));
		}
		return count;
	}
	
	/**
	 * 提供外部触发channelInactive的入口方法
	 * @param channelId
	 * @return 当前连接的channel数量
	 */
	public int channelInactive(String channelId){
		int count = channels.decrementAndGet();
		this.allCounter.remove(channelId);
		if(this.listener != null){
			this.context.runTask(new FireChannelInactiveRunner(this.listener,channelId,count)); 
		}
		return count;
	}
	
	/**
	 * 获得到当前连接的channel的数量
	 * @return
	 */
	public int channels(){
		return channels.get(); 
	}

	/**
	 * 设置server status的监听程序
	 * @param listener
	 */
	public void setListener(IServerStatusListener listener) {
		this.listener = listener;
	}

	public void destory() { 
		this.context = null;
		this.running = null;
		this.waiting = null;
		this.channels = null;
		this.listener = null;
		this.counter = null;
		this.allCounter = null;
	}
	
	/**
	 * 触发channelActive的触发器程序
	 * @author 康庆
	 *
	 */
	private static final class FireChannelActiveRunner implements Runnable{
		private IServerStatusListener listener = null;
		private String channelId = null;
		private int count = -1;
		
		protected FireChannelActiveRunner(IServerStatusListener listener,String channelId,int count){
			this.listener = listener;
			this.channelId = channelId;
			this.count = count;
		} 
		@Override
		public void run() {
			this.listener.channelsChange(Event.ACTIVE,channelId, this.count);
		}
	}
	
	/**
	 * 触发channelInactive的触发器程序
	 * @author 康庆
	 */
	private static final class FireChannelInactiveRunner implements Runnable{
		private IServerStatusListener listener = null;
		private String channelId = null;
		private int count = -1;
		
		protected FireChannelInactiveRunner(IServerStatusListener listener,String channelId,int count){
			this.listener = listener;
			this.channelId = channelId; 
			this.count = count;
		}
		
		@Override
		public void run() {
			this.listener.channelsChange(Event.INACTIVE,channelId,this.count);
		}
	}

	/**
	 * 返回channelId对应的channel自连接以来接收到的所有的消息数量
	 * @return
	 */
	public long messageCount(String channelId) {
		return this.allCounter.get(channelId).get();   
	}
	
	/**
	 * 返回server自启动以来接收到的所有的消息数量
	 * @return
	 */
	public long messageCount() {
		return this.counter.get();   
	}

	/**
	 * 记录某个channel的消息总数，在当前数量的基础上加一
	 * @param channelId
	 * @return
	 */
	public long countPlus(String channelId) {
		this.counter.incrementAndGet();
		if(this.allCounter.containsKey(channelId)){
			this.allCounter.put(channelId, new AtomicLong(1));
			return 1;
		}else{
			return this.allCounter.get(channelId).incrementAndGet();
		}
	}
}
