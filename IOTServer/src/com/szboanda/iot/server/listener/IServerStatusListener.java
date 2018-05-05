package com.szboanda.iot.server.listener;

public interface IServerStatusListener {
	enum Event{
		ACTIVE,
		INACTIVE
	};
	void channelsChange(Event status,String channelId,int count);
}
