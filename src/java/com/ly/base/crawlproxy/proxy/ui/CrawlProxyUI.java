package com.ly.base.crawlproxy.proxy.ui;

/**
 * Creation Date:2012-1-31
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.ly.base.crawlproxy.proxy.crawl.CrawlControl;

public class CrawlProxyUI extends JFrame {

	private static final long serialVersionUID = -170028615738254665L;

	public static String TITLE = "taobao代理抓取提取入库程序 公共搜索组 20130717";

	public static JTextArea logInfo;

	private JButton execButton;

	private final int contentMax = 300;// 最多可以显示多少行

	public CrawlProxyUI() {
		initComponent();
	}

	private void initComponent() {

		Menu menu = new Menu();
		this.setJMenuBar(menu);

		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());

		logInfo = new JTextArea(22, 9);

		// 滚动条自动到底端
		logInfo.setDocument(new LimitativeDocument(logInfo, contentMax));
		pane.add(new JScrollPane(logInfo), BorderLayout.CENTER);

		execButton = new JButton("开始抓取>>");
		JPanel contentPanel = new JPanel();

		contentPanel.add(execButton);
		pane.add(contentPanel, BorderLayout.SOUTH);

		InputListener listener = new InputListener();

		execButton.addActionListener(listener);
	}

	public void showFrame() {

		setTitle(TITLE);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);// 是否允许最大化

		setSize(750, 550);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	/**
	 * 监听类
	 */
	class InputListener extends MouseAdapter implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			executeClick();
		}

		public void mouseClicked(MouseEvent event) {
			executeClick();
		}

		// 执行刷新
		private void executeClick() {
			Runnable waitRunner = new Runnable() {
				public void run() {
					execProgram();
				}
			};
			Thread t = new Thread(waitRunner);
			t.start();
		}
	}

	class Menu extends JMenuBar {
		private JDialog aboutDialog;

		public Menu() {
			JMenu fileMenu = new JMenu("文件");
			JMenuItem exitMenuItem = new JMenuItem("退出", KeyEvent.VK_E);
			JMenuItem aboutMenuItem = new JMenuItem("关于..", KeyEvent.VK_A);
			fileMenu.add(exitMenuItem);
			fileMenu.add(aboutMenuItem);
			this.add(fileMenu);
			aboutDialog = new JDialog();
			initAboutDialog();
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					System.exit(0);
				}
			});

			aboutMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					aboutDialog.show();
				}
			});
		}

		public JDialog get() {
			return aboutDialog;
		}

		public void initAboutDialog() {
			aboutDialog.setTitle("关于");
			Container con = aboutDialog.getContentPane();
			Icon icon = new ImageIcon("sdmile.gif");
			JLabel aboutLabel = new JLabel("<html><b><font color=red size=5>" + "<center>版权所有@同程旅游" + "<br>", icon, JLabel.CENTER);
			con.add(aboutLabel, BorderLayout.CENTER);
			aboutDialog.setSize(450, 225);
			aboutDialog.setLocation(300, 300);
			aboutDialog.addWindowListener(new WindowAdapter() {
				public void WindowClosing(WindowEvent e) {
					dispose();
				}
			});
		}
	}

	public void execProgram() {
		if (CrawlControl.isRunning) {
			new CrawlControl().crawlData();
			execButton.setText("执行中...");
		} else {
			System.out.println("程序已经执行...");
			return;
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e2) {
			}
		}

		OutputStream textAreaStream = new OutputStream() {
			public void write(int b) throws IOException {
				// logInfo.append(String.valueOf((char) b));
				ShowInfo.appendContent(logInfo, String.valueOf((char) b));
			}

			public void write(byte b[]) throws IOException {
				// logInfo.append(new String(b));
				ShowInfo.appendContent(logInfo, new String(b));
			}

			public void write(byte b[], int off, int len) throws IOException {
				// logInfo.append(new String(b, off, len));
				ShowInfo.appendContent(logInfo, new String(b, off, len));
			}
		};
		PrintStream myOut = new PrintStream(textAreaStream);
		System.setOut(myOut);
		System.setErr(myOut);

		new CrawlProxyUI().showFrame();
	}

}
