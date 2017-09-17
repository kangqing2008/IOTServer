package com.szboanda.iot.server;

import io.netty.channel.Channel;

public class NettyUtils {

	public static String channelInfo(Channel channel){ 
		return "Channel [id:" + channel.id() + ",event loop:" + channel.eventLoop().toString() + ",thread:" + Thread.currentThread().getId() + "]";
	} 
}
