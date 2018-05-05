package com.szboanda.iot.hub.model;

import java.io.Serializable;

public class Packet implements Serializable {

	private static final long serialVersionUID = -894733822317942438L;
	private static final String HEAD = "##";
	private static final String TAIL = "\r\n";
	private int length = 0;
	private String data;
	private String crc;
//	private 
	
	@Override
	public String toString(){
		return HEAD + String.format("", length) + data + crc + TAIL;
	}

}
