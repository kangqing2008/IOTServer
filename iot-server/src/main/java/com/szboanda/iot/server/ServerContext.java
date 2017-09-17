package com.szboanda.iot.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.szboanda.iot.server.listener.IServerStatusListener;
 
public class ServerContext {
	private ServerStatus serverStatus = null;
	private ExecutorService es = null;
	public ServerContext(){
		this.serverStatus = new ServerStatus(this);
		this.es = Executors.newSingleThreadExecutor();
	}
	
	public void setServerStatusListener(IServerStatusListener listener){
		this.serverStatus.setListener(listener);
	}
	 
	/***
	 * 发布channel active状态
	 * @return 当前活跃的channel数量
	 */
	public int fireChannelActive(){
		return this.serverStatus.channelActive();
	}
	
	public long countPlus(){
		return this.serverStatus.countPlus();       
	}
	
	public long countPlus(String channelId){ 
		return this.serverStatus.countPlus(channelId);       
	}
	
	/***
	 * 发布channel inactive状态
	 * @return 当前活跃的channel数量
	 */
	public int fireChannelInactive(){  
		return this.serverStatus.channelInactive();  
	}

	public void destory() {
		this.serverStatus.destory();
		this.serverStatus = null; 
	}
	
	
	protected void runTask(Runnable command){
		this.es.execute(command); 
	}

	public long messageCount() {
		return this.serverStatus.messageCount();
	}
	
	public long messageCount(String channelId) {
		return this.serverStatus.messageCount(channelId);
	}
	
	public long channelMessageCount() {
		return this.serverStatus.channelMessageCount();
	}

	public String listChannelMessageDetail() { 
		return this.serverStatus.listChannelMessageDetail();
	}

}
