package com.ly.base.crawlproxy.proxy.bean;

/**
 * Creation Date:2013-7-17
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

public class ProxyIP {

	private String ip;
	private String area;
	private int port;
	private String state;
	private String proxyType;
	private String responseTime;
	private String lastCheckDateTime;

	public ProxyIP() {
		// TODO Auto-generated constructor stub
	}

	public ProxyIP(String ip, int port, String area, String state, String proxytype, String responsetime, String lastcheckdatetime) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.area = area;
		this.port = port;
		this.state = state;
		this.proxyType = proxytype;
		this.responseTime = responsetime;
		this.lastCheckDateTime = lastcheckdatetime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getLastCheckDateTime() {
		return lastCheckDateTime;
	}

	public void setLastCheckDateTime(String lastCheckDateTime) {
		this.lastCheckDateTime = lastCheckDateTime;
	}

}
