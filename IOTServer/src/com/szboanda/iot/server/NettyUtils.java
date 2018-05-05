package com.szboanda.iot.server;

import java.nio.charset.Charset;

import io.netty.channel.Channel;

public class NettyUtils {

	public static final Charset UTF_8 = Charset.forName("UTF-8");
	public static String channelInfo(Channel channel){ 
		return "Channel [id:" + channel.id() + ",event loop:" + channel.eventLoop().toString() + ",thread:" + Thread.currentThread().getId() + "]";
	}
	
	public static int CRC16(byte[] data){
		int reg,check;
		reg = 0xFFFF;
		for(int i=0;i<data.length;i++){
			reg = (reg>>8)^data[i];
			for(int j=0;j<8;j++){
				check = reg & 0x0001;
				reg >>= 1;
				if(check == 0x0001){
					reg ^= 0xA001;
				}
			}
		}
		return reg;
	}
	
	public static String crc16ForStr(String data){
		return Integer.toHexString(CRC16(data.getBytes()));
	}
	
	public static void main(String[] args){
		String data = "QN=20160801085857223;ST=32;CN=1062;PW=100000;MN=010000A8900016F000169DC0;Flag=5;CP=&&RtdInterval=30&&";
		System.out.println(crc16ForStr(data));
	}
	
}
