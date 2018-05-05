package com.szboanda.iot.hub.listener;

import com.szboanda.iot.hub.tools.ServerContext;

public class ServerStatusPrinter implements IServerStatusListener {

	private ServerContext context;
	public ServerStatusPrinter(ServerContext context){
		this.context = context; 
	}
	
	@Override
	public void channelsChange(Event status,String channelId, int count) {  
		System.out.println("channelsChange[" + status + "->" + count + ",message->" + this.context.messages() + "]");
		
	}
	
}
