package com.ly.base.crawlproxy.proxy.ui;

/**
 * Creation Date:2012-1-31
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import javax.swing.JTextArea;

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2012-1-31
 * 
 */
public class ShowInfo {
	public static void appendContent(JTextArea info, String contentText) {
		if (!contentText.equals("")) {
			// info.append(contentText + "\n");
			info.append(contentText);
			info.setCaretPosition(info.getText().length());
		}
		// content.setText("");
		// try {
		// Rectangle rec = info.modelToView(info.getCaretPosition());
		// System.out.println(rec.y / rec.height + 1);
		// label.setText("行数:" + (rec.y / rec.height + 1));
		// } catch (BadLocationException e) {
		// e.printStackTrace();
		// }
	}
}
