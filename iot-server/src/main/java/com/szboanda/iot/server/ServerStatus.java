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
	private IServerStatusListener listener = null; 
	private ServerContext context = null; 
	private Map<String,AtomicLong> channelCounter = null;
	
	public ServerStatus(ServerContext context){
		this(context,null); 
	} 
	
	public long countPlus(){
		return this.counter.incrementAndGet();
	}
	
	public long countPlus(String channelId){
		synchronized(channelId.intern()) { 
			if(!this.channelCounter.containsKey(channelId)) {
				this.channelCounter.put(channelId, new AtomicLong(1));
				return 1;
			}else {
				return this.channelCounter.get(channelId).incrementAndGet();
			}
		}
	}
	
	private void init(ServerContext context,IServerStatusListener listener){
		this.context = context;
		this.running = new AtomicBoolean(false);
		this.waiting = new AtomicInteger(0);
		this.channels = new AtomicInteger(0);  
		this.channelCounter = new ConcurrentHashMap<String,AtomicLong>(); 
		this.listener = listener; 
	}
	
	public ServerStatus(ServerContext context,IServerStatusListener listener){
		this.init(context,listener);
	}
	
	
	public int channelActive(){
		int count = channels.incrementAndGet();
		if(this.listener != null){
			this.context.runTask(new FireChannelActiveRunner(this.listener,count));
		}
		return count;
	}
	
	public int channelInactive(){
		int count = channels.decrementAndGet();
		if(this.listener != null){
			this.context.runTask(new FireChannelInactiveRunner(this.listener,count)); 
		}
		return count;
	}
	
	public int channels(){
		return channels.get(); 
	}

	public void setListener(IServerStatusListener listener) {
		this.listener = listener;
	}

	public void destory() { 
		this.context = null;
		this.running = null;
		this.waiting = null;
		this.channels = null;
		this.listener = null; 
	}
	
	private static final class FireChannelActiveRunner implements Runnable{
		private IServerStatusListener listener = null;
		private int count = -1;
		
		protected FireChannelActiveRunner(IServerStatusListener listener,int count){
			this.listener = listener;
			this.count = count;
		}
		@Override
		public void run() {
			this.listener.channelsChange(Event.ACTIVE, this.count);
		}
	}
	
	private static final class FireChannelInactiveRunner implements Runnable{
		private IServerStatusListener listener = null;
		private int count = -1;
		
		protected FireChannelInactiveRunner(IServerStatusListener listener,int count){
			this.listener = listener;
			this.count = count;
		}
		
		@Override
		public void run() {
			this.listener.channelsChange(Event.INACTIVE, this.count);
		}
	}

	public long messageCount() {
		return this.counter.get();   
	}

	public long messageCount(String channelId) {
		return this.channelCounter.get(channelId).get();
	}

	public long channelMessageCount() {
		long count = 0;
		for(AtomicLong c : this.channelCounter.values()) {
			count += c.get();
		}
		return count;
	}

	public String listChannelMessageDetail() {
		StringBuilder buffer = new StringBuilder(1024);
		for(String id : this.channelCounter.keySet()) {
			buffer.append("[id->").append(id).append(",count->").append(this.channelCounter.get(id).get()).append("]\n");
		}
		return buffer.toString(); 
	}
}
