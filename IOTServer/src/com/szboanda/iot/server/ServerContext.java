package com.szboanda.iot.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.szboanda.iot.server.listener.IServerStatusListener;

/**
 * Server运行上下文环境
 * @author 康庆
 *
 */
public class ServerContext {
	/**用于记录server运行状态的对象*/
	private ServerStatus serverStatus = null;
	/**用于异步执行事件通知的线程池*/
	private ExecutorService es = null;
	public ServerContext(){
		this.serverStatus = new ServerStatus(this);
		this.es = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * 设置监听服务器运行状态的监听程序
	 * @param listener
	 */
	public void setServerStatusListener(IServerStatusListener listener){
		this.serverStatus.setListener(listener);
	}
	
	/***
	 * 发布channel active状态
	 * @return 当前活跃的channel数量
	 */
	public int fireChannelActive(String channelId){ 
		return this.serverStatus.channelActive(channelId);
	}
	/**
	 * 累加channelId对应的channel的消息计数器
	 * @param channelId
	 * @return channelId对应的channel的当前消息总数量
	 */
	public long msgCounterPlus(String channelId){
		return this.serverStatus.countPlus(channelId);      
	}
	
	/***
	 * 发布channel inactive状态
	 * @return 当前活跃的channel数量
	 */
	public int fireChannelInactive(String channelId){  
		return this.serverStatus.channelInactive(channelId);  
	}

	public void destory() {
		this.serverStatus.destory();
		this.serverStatus = null; 
	}
	
	protected void runTask(Runnable command){
		this.es.execute(command); 
	}
	
	/**
	 * 获得当前server自启动以来收到的所有message的数量
	 * @return 所有channel收到的消息总数量
	 */
	public long messages() { 
		return this.serverStatus.messageCount();
	}
	
	/**
	 * 获得channelId对应的channel的消息总数量
	 * @param channelId
	 * @return channelId对应的channel收到的消息总数量
	 */
	public long messages(String channelId) {
		return this.serverStatus.messageCount(channelId);
	}

}
