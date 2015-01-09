package com.ly.base.crawlproxy.proxy.ui;

/**
 * Creation Date:2012-1-31
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2012-1-31
 * 
 */
/** */
/**
 * project：study package：swingstudy file：LimitativeDocument.java date：2007-10-24
 */

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/** */
/**
 * description:自定义的Document 可以控制最大行数 默认最大为10行 超过最大行时，上面的一行将被截取
 * 
 * 
 * Copyright(c) Surfilter Technology Co.,Ltd
 * 
 * @author PurpleMaple －－zhangyixuan(张逸轩)
 * @date 2007-10-24 上午10:24:34
 */
public class LimitativeDocument extends PlainDocument {
	private static final long serialVersionUID = -7130991111045567194L;

	private JTextComponent textComponent;

	private int lineMax = 10;

	public LimitativeDocument(JTextComponent tc, int lineMax) {
		textComponent = tc;
		this.lineMax = lineMax;
	}

	public LimitativeDocument(JTextComponent tc) {
		textComponent = tc;
	}

	public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException {

		String value = textComponent.getText();
		int overrun = 0;
		if (value != null && value.indexOf('\n') >= 0 && value.split("\n").length >= lineMax) {
			overrun = value.indexOf('\n') + 1;
			super.remove(0, overrun);
		}
		super.insertString(offset - overrun, s, attributeSet);
	}
}
