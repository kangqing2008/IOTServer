package com.szboanda.iot.server.listener;

import com.szboanda.iot.server.ServerContext; 

public class ServerStatusPrinter implements IServerStatusListener {

	private ServerContext context;
	public ServerStatusPrinter(ServerContext context){
		this.context = context; 
	}
	  
	@Override
	public void channelsChange(Event status, int count) {
		System.out.println("channelsChange[" + status + "->" + count + ",message->" + this.context.messageCount() + ",all->" + this.context.channelMessageCount() + "]");
		if(count == 0) { 
			System.out.println(this.context.listChannelMessageDetail());   
		}
	} 
	
}
