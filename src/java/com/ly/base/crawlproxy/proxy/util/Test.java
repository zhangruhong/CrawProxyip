package com.ly.base.crawlproxy.proxy.util;

/**
 * Creation Date:2014-3-14
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2014-3-14
 * 
 */
public class Test {
	public static void main(String[] args) {
		String source = HttpClientExample.getGetResponseWithHttpClient("http://httpproxy.17usoft.com/tcproxy/getProxy.do?count=1&min=10", "GBK");
		System.out.println(source);

	}
}
