package enixlin.jrrc.mail.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;

import org.nutz.el.Parse;

import com.gdbeim.oa.applet.DownloadTask;
import com.gdbeim.oa.applet.HttpGet;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

import enixlin.jrrc.mail.dao.MailRecord;
import enixlin.jrrc.mail.dao.Struction;
import enixlin.jrrc.mail.net.Http_request;
import sun.font.CreatedFontTracker;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.List;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JProgressBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainPanel {
	private static JFrame frame;
	private static JPanel panel;
	private static DefaultMutableTreeNode root;
	private static String cookie;
	private static ArrayList<Struction> userList;
	private static DefaultComboBoxModel<Struction> userComboBox;
	private static JComboBox comboBox;
	private static JTabbedPane tabbedPane;
	private static JTable table;
	private static JComboBox comboBox_1;
	private static JTextField textField;
	private static JTextField textField_1;
	private static JTextField textField_2;
	private static DefaultTableModel tm;
	private static ArrayList<String> tabList;
	private static Struction user;
	private static JProgressBar progressBar;
	private static JLabel lblNewLabel;
	private static DownloadTask dt;

	public MainPanel(DefaultMutableTreeNode tree, String cookie) {
		this.root = tree;
		this.cookie = cookie;
		UIManager um = new UIManager();
		try {
			um.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initial();

	}

	private static void initial() {
		// TODO Auto-generated method stub
		frame = new JFrame();
		frame.setTitle("主功能面板");
		frame.setSize(1193, 720);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 184, 335);
		frame.getContentPane().add(scrollPane);

		final JTree tree = new JTree(root);
		scrollPane.setViewportView(tree);
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				Struction nodedate = (Struction) node.getUserObject();
				refreshUserList(makeuserlist(nodedate.getCode()));
			}
		});

		comboBox = new JComboBox();
		comboBox.setBounds(63, 368, 131, 21);

		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					// 取得下拉表的用户
					user = (Struction) comboBox.getSelectedItem();
					 dt = makeMailList(user.getCode());
					String sBoxType = (String) comboBox_1.getSelectedItem();
					showMailList(dt, sBoxType);
				}
			}
		});
		frame.getContentPane().add(comboBox);

		JLabel label = new JLabel("用户：");
		label.setBounds(10, 371, 54, 15);
		frame.getContentPane().add(label);

		comboBox_1 = new JComboBox();
		comboBox_1.addItem("收件箱");
		comboBox_1.addItem("发件箱");
		comboBox_1.addItem("(归档)收件箱");
		comboBox_1.addItem("(归档)发件箱");
		comboBox_1.setBounds(10, 396, 184, 21);
		comboBox_1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// 切换收件箱和发件箱
				if (ItemEvent.SELECTED == e.getStateChange()) {
					// 取得下拉表的用户
					String sBoxType = (String) comboBox_1.getSelectedItem();					
						showMailList(dt, sBoxType);										
				}
			
				

			}
		});
		frame.getContentPane().add(comboBox_1);

		JButton btnNewButton = new JButton("批量保存");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNewLabel.setText("开始更新。。。" + makeMailList(user.getCode()).getReceiveList().size());
				SwingWorker<String, Void> sw = new SwingWorker<String, Void>() {

					@Override
					protected String doInBackground() throws Exception {
						// TODO Auto-generated method stub
						updateDataBase(makeMailList(user.getCode()));
						return null;
					}

					@Override
					protected void process(java.util.List<Void> chunks) {
						// TODO Auto-generated method stub
						super.process(chunks);
					}

					@Override
					protected void done() {
						lblNewLabel.setText("更新完成");
						super.done();
					}

				};
				sw.execute();

			}
		});
		btnNewButton.setBounds(10, 533, 184, 23);
		frame.getContentPane().add(btnNewButton);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(211, 10, 964, 666);
		frame.getContentPane().add(scrollPane_1);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 900, 500);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		scrollPane_1.setViewportView(tabbedPane);
		tabList = new ArrayList<>();

		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane.addTab("邮件列表", null, scrollPane_2, null);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// System.out.println(tm.getValueAt(table.getSelectedRow(),
				// table.getSelectedColumn()));

				makeNewTab();

			}
		});

		table.setBorder(new LineBorder(new Color(192, 192, 192), 0));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setAutoCreateRowSorter(true);
		table.setFont(new Font("宋体", Font.PLAIN, 12));
		scrollPane_2.setViewportView(table);

		textField = new JTextField();
		textField.setBounds(57, 427, 137, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(57, 458, 137, 21);
		frame.getContentPane().add(textField_1);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(57, 491, 137, 21);
		frame.getContentPane().add(textField_2);

		JLabel label_1 = new JLabel("起始日");
		label_1.setBounds(10, 430, 37, 15);
		frame.getContentPane().add(label_1);

		JLabel label_2 = new JLabel("终止日");
		label_2.setBounds(10, 461, 37, 15);
		frame.getContentPane().add(label_2);

		JLabel label_3 = new JLabel("关键字");
		label_3.setBounds(10, 494, 54, 15);
		frame.getContentPane().add(label_3);

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 576, 184, 19);
		frame.getContentPane().add(progressBar);

		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(10, 605, 75, 15);
		frame.getContentPane().add(lblNewLabel);
		frame.setVisible(true);
	}
	
	
	
	
	
	
	

	/**
	 * 更新邮件列表数据库步骤</br>
	 * </br>
	 * 
	 * 一、传入一个DownloadTask对象，从DownloadTask对象中取得以下6个列表：</br>
	 * </br>
	 * 1 收件箱列表</br>
	 * 2收件箱附件列表</br>
	 * 3发件箱列表</br>
	 * 4发件箱附件列表</br>
	 * 5草稿箱列表</br>
	 * 6草稿箱附件列表</br>
	 * </br>
	 * 
	 * 二、根据userCode，从数据库表jrrc_mail_list中取出所有归属该用户的MessageID， 生成一个ArrayList
	 * msgList_db</br>
	 * </br>
	 * 
	 * 二、分别历遍收件、发件、草稿三个列表，取得所有MessageID与msgList_db里的MessageID比较,
	 * 如果没有在msgList_db里的就将该Message插入数据,因为每一条messageid都是唯一的，插入 后就可以从msgList_db
	 * remove 减少历编的次数提高更新的效率</br>
	 * </br>
	 * 
	 * 四、将列表数据写入数据库表的同时，下载邮件正文和附件
	 * 
	 * @param dt
	 */
	protected static void updateDataBase(DownloadTask dt) {
		// 传入一个DownloadTask对象，从DownloadTask对象中取得以下6个列表
		java.util.List rec_list = dt.getReceiveList();
		java.util.List rec_attch_list = dt.getReceiveAttachList();
		java.util.List send_list = dt.getSendList();
		java.util.List send_attch_list = dt.getSendAttachList();
		java.util.List draft_list = dt.getDraftList();
		java.util.List draft_attch_list = dt.getDraftAttachList();

		// 根据userCode，从数据库表jrrc_mail_list中取出所有归属该用户的MessageID，
		ArrayList<String> msgList_db = get_msgList_db(user.getCode());

		// 开始编历上述的收件箱、发件箱、草稿箱中的邮件编号，如果在msgList_db中没有
		// 该编号的邮件，则将该邮件信息下载，并写入数据库中
		long count = 0;
		for (int n = 0; n < rec_list.size(); n++) {
			Object[] rec_msg_info = (Object[]) rec_list.get(n);
			String rec_msg_id = String.valueOf(rec_msg_info[0]);

			String flag = "null";
			for (int m = 0; m < msgList_db.size(); m++) {
				String db_msg_id = msgList_db.get(m);

				if (db_msg_id != null && rec_msg_id.equals(db_msg_id)) {
					flag = "exist";
					msgList_db.remove(m);
				}
			}
			if ("null".equals(flag)) {
				insertData(rec_msg_info, user, "1");
			}

		}

		msgList_db = get_msgList_db(user.getCode());
		for (int n = 0; n < send_list.size(); n++) {
			Object[] send_msg_info = (Object[]) send_list.get(n);
			String send_msg_id = String.valueOf(send_msg_info[0]);

			String flag = "null";
			for (int m = 0; m < msgList_db.size(); m++) {
				String db_msg_id = msgList_db.get(m);
				if (db_msg_id != null && send_msg_id.equals(db_msg_id)) {
					flag = "exist";
					msgList_db.remove(m);
				}
			}
			if ("null".equals(flag)) {
				insertData(send_msg_info, user, "2");
			}

		}

		ArrayList<String> AttchList_db = get_AttchList_db();
		for (int n = 0; n < rec_attch_list.size(); n++) {
			Object[] rec_attch_info = (Object[]) rec_attch_list.get(n);
			String rec_attch_id = String.valueOf(rec_attch_info[0]);
			String rec_attch_name = String.valueOf(rec_attch_info[1]);
			String flag = "null";
			for (int m = 0; m < AttchList_db.size(); m++) {
				String db_rec_attch_id = AttchList_db.get(m);
				if (db_rec_attch_id != null && rec_attch_id.equals(db_rec_attch_id)) {
					flag = "exist";
					AttchList_db.remove(m);
				}
			}
			if ("null".equals(flag)) {
				insertAttchFileData(rec_attch_info);
				downloadAttchFile(rec_attch_id, rec_attch_name);
			}
		}
		
		AttchList_db = get_AttchList_db();
		for (int n = 0; n < send_attch_list.size(); n++) {
			Object[] send_attch_info = (Object[]) send_attch_list.get(n);
			String send_attch_id = String.valueOf(send_attch_info[0]);
			String send_attch_name = String.valueOf(send_attch_info[1]);
			String flag = "null";
			for (int m = 0; m < AttchList_db.size(); m++) {
				String db_send_attch_id = AttchList_db.get(m);
				if (db_send_attch_id != null && send_attch_id.equals(db_send_attch_id)) {
					flag = "exist";
					AttchList_db.remove(m);
				}
			}
			if ("null".equals(flag)) {
				insertAttchFileData(send_attch_info);
				downloadAttchFile(send_attch_id, send_attch_name);
			}
		}

	}

	private static void insertData(Object[] rec_msg_info, Struction user, String type) {
		String sHost = "localhost";
		String sDBName = "jrrc";
		String sUser = "root";
		String sPassword = "root";
		Connection conn = null;
		String url = "jdbc:mysql://" + sHost + "/" + sDBName + "?" + "user=" + sUser + "&password=" + sPassword;
		try {
			String sMsg_Content = getMailContent((long) rec_msg_info[0]);
			String sMsg_type = type;
			String sMsg_id = String.valueOf(rec_msg_info[0]);
			String sMsg_title = String.valueOf(rec_msg_info[1]);
			String sMsg_timestamp = String.valueOf(rec_msg_info[2]);
			String sMsg_user = user.getCode();

			conn = (Connection) DriverManager.getConnection(url);
			String sql = "insert into jrrc_mail_list (list_type,m_id,m_title,m_datetime,u_id,m_content) values(?,?,?,?,?,?)";
			PreparedStatement smt = (PreparedStatement) conn.prepareStatement(sql);
			smt.setString(1, sMsg_type);
			smt.setString(2, sMsg_id);
			smt.setString(3, sMsg_title);
			smt.setString(4, sMsg_timestamp);
			smt.setString(5, sMsg_user);
			smt.setString(6, sMsg_Content);
			int rs = smt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void insertAttchFileData(Object[] Attch_info) {
		// System.out.println("insertAttchFileData start:--------");
		String sHost = "localhost";
		String sDBName = "jrrc";
		String sUser = "root";
		String sPassword = "root";
		Connection conn = null;
		String url = "jdbc:mysql://" + sHost + "/" + sDBName + "?" + "user=" + sUser + "&password=" + sPassword;
		try {

			String sAttch_id = String.valueOf(Attch_info[0]);
			String sAttch_name = (String) Attch_info[1];
			String sAttch_datetime = String.valueOf(Attch_info[2]);
			String sAttch_size = String.valueOf(Attch_info[3]);
			String sAttch_msg_id = String.valueOf(Attch_info[4]);

			conn = (Connection) DriverManager.getConnection(url);
			String sql = "insert into jrrc_mail_attach (a_id,a_title,a_datetime,a_size,m_id) values(?,?,?,?,?)";
			PreparedStatement smt = (PreparedStatement) conn.prepareStatement(sql);
			smt.setString(1, sAttch_id);
			smt.setString(2, sAttch_name);
			smt.setString(3, sAttch_datetime);
			smt.setString(4, sAttch_size);
			smt.setString(5, sAttch_msg_id);
			int rs = smt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 取得数据库中所有的邮件编号，生成一个列表msgList_db
	 * 
	 * @param code
	 * @return
	 */
	private static ArrayList<String> get_msgList_db(String code) {
		ArrayList<String> msgList_db = new ArrayList<String>();
		// 用JDBC方法联接Mysql数据库
		String sHost = "localhost";
		String sDBName = "jrrc";
		String sUser = "root";
		String sPassword = "root";
		Connection conn = null;
		String url = "jdbc:mysql://" + sHost + "/" + sDBName + "?" + "user=" + sUser + "&password=" + sPassword;
		try {
			conn = (Connection) DriverManager.getConnection(url);
			Statement smt = (Statement) conn.createStatement();
			String sql = "select m_id     from jrrc_mail_list  group by m_id";
			ResultSet rs = smt.executeQuery(sql);
			while (rs.next()) {
				msgList_db.add(rs.getString("m_id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msgList_db;
	}

	private static ArrayList<Struction> makeuserlist(final String strStructionCode) {
		userList = new ArrayList<>();
		SwingWorker<ArrayList<Struction>, Void> sw = new SwingWorker<ArrayList<Struction>, Void>() {

			@Override
			protected ArrayList<Struction> doInBackground() throws Exception {
				String strUrl = "http://96.0.32.11/oa/groupSelect.do?cmd=left&selectType='3'&orgId=" + strStructionCode
						+ "&showType=null&selectGroupId=null";
				String result = "";
				URL url = new URL(strUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Cookie", cookie);
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf8"));
				String line = "";
				String content = "";
				while ((line = br.readLine()) != null) {
					content = content + line + "\n";
				}
				result = content;
				// System.out.println(result);

				String regexString = "var d[0-9]{1,}.+?;";

				java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regexString);
				java.util.regex.Matcher matcher = pattern.matcher(result);
				long count = 0;
				while (matcher.find()) {
					userList.add(getDataNode(matcher.group()));
				}
				return userList;

			}

		};
		sw.execute();
		try {
			userList = sw.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userList;

	}

	private static Struction getDataNode(String line) {
		Struction dataNode = new Struction();
		String regex_seria = " d[0-9]{0,}="; // 节点号
		String regex_name = "'[^0-9]{1,}'"; // 机构名称
		String regex_code = "'[0-9]{1,}'"; // 机构号
		String regex_parent = ",d[0-9]{1,}.+?;"; // 上级节点号

		java.util.regex.Pattern pattern_seria = java.util.regex.Pattern.compile(regex_seria);
		java.util.regex.Pattern pattern_name = java.util.regex.Pattern.compile(regex_name);
		java.util.regex.Pattern pattern_code = java.util.regex.Pattern.compile(regex_code);
		java.util.regex.Pattern pattern_parent = java.util.regex.Pattern.compile(regex_parent);

		java.util.regex.Matcher matcher_seria = pattern_seria.matcher(line);
		java.util.regex.Matcher matcher_name = pattern_name.matcher(line);
		java.util.regex.Matcher matcher_code = pattern_code.matcher(line);
		java.util.regex.Matcher matcher_parent = pattern_parent.matcher(line);

		if (matcher_code.find()) {

			dataNode.setCode(matcher_code.group().replace("'", ""));

		}
		if (matcher_name.find()) {

			dataNode.setName(matcher_name.group().replace("'", "").replace(",d,", ""));
			// System.out.println(matcher_name.group());
		}
		if (matcher_seria.find()) {

			dataNode.setSeria(matcher_seria.group().replace(" ", "").replace("=", ""));
		}
		if (matcher_parent.find()) {

			dataNode.setParentcode(matcher_parent.group().replace(",", "").replace(");", ""));
			// System.out.println(matcher_parent.group());
		}

		return dataNode;
	}

	private static void refreshUserList(ArrayList<Struction> userlist) {
		Struction[] list = new Struction[userlist.size()];
		for (int i = 0; i < userlist.size(); i++) {
			if (userlist.get(i) != null) {
				list[i] = userlist.get(i);
			}
		}
		userComboBox = new DefaultComboBoxModel(list);
		comboBox.setModel(userComboBox);
	}

	/**
	 * 点击邮件记录后，生成一个新的TAB来显示该封邮件
	 */
	private static void makeNewTab() {

		int row = table.convertRowIndexToModel(table.getSelectedRow());
		String title = (String) tm.getValueAt(row, 2);
		String MsgID = (String) String.valueOf(tm.getValueAt(row, 0));
		long lgMsgID = (long) tm.getValueAt(row, 0);
		JPanel jp_tab = new JPanel();
		JPanel jp_content = new JPanel();
		JLabel jl = new JLabel(title);
		jp_content.add(jl);

		if (tabList.isEmpty() || tabList.contains(MsgID) != true) {
			tabList.add(MsgID);
			try {
				jp_content = CreateTabContent(lgMsgID);
				jp_tab = CreateTabLabel(title);
				tabbedPane.addTab("", null, jp_content, title);

				tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(jp_content), jp_tab);
				// JLabel jlabel_content=new JLabel(title);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			tabbedPane.setSelectedComponent(tabbedPane.getComponentAt(tabList.indexOf(MsgID) + 1));
		}
	}

	/**
	 * 生成用户邮件列表
	 * 
	 * @param strUserCode
	 *            用户代码
	 * 
	 */
	private static DownloadTask makeMailList(String strUserCode) {
		HttpGet hg = new HttpGet();
		String startDate = "2000-01-01";
		String endDate = "2020-01-01";
		String url = "http://96.0.32.11/oa/messageDownload?method=getDownList" + "&startDate=" + startDate + "&endDate="
				+ endDate + "&userId=" + strUserCode;
		DownloadTask dt = hg.getDownloadTask(url);
		return dt;

	}

	/**
	 * 显示用户的邮件列表
	 * 
	 * @param dt
	 */
	private static void showMailList(DownloadTask dt, String sBoxType) {

		if (sBoxType == "收件箱") {
			java.util.List list = dt.getReceiveList();
			Object[][] data = new Object[list.size()][4];
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[]) list.get(i);
				data[i][0] = o[0];
				data[i][1] = getSender((String) o[1]);
				data[i][2] = getTitle((String) o[1]);
				data[i][3] = o[2];
			}
			Object[] title = new Object[4];
			title[0] = "邮件编号";
			title[1] = "发件人";
			title[2] = "标题";
			title[3] = "收件时间";
			tm = new DefaultTableModel();

			tm.setDataVector(data, title);
			table.setModel(tm);
			table.getColumnModel().getColumn(0).setPreferredWidth(60);
			table.getColumnModel().getColumn(1).setPreferredWidth(60);
			table.getColumnModel().getColumn(2).setPreferredWidth(560);
			table.getColumnModel().getColumn(3).setPreferredWidth(150);
		} else {
			java.util.List list = dt.getSendList();
			Object[][] data = new Object[list.size()][4];
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[]) list.get(i);
				data[i][0] = o[0];
				data[i][1] = getSender((String) o[1]);
				data[i][2] = getTitle((String) o[1]);
				data[i][3] = o[2];
			}

			Object[] title = new Object[4];
			title[0] = "邮件编号";
			title[1] = "发件人";
			title[2]="标题";
			title[3] = "发件时间";
			tm = new DefaultTableModel();
			tm.setDataVector(data, title);
 
			table.setModel(tm);
			//设定表格的列宽
			table.getColumnModel().getColumn(0).setPreferredWidth(60);
			table.getColumnModel().getColumn(1).setPreferredWidth(60);
			table.getColumnModel().getColumn(2).setPreferredWidth(560);
			table.getColumnModel().getColumn(3).setPreferredWidth(150);
		}
	}

	/**
	 * 取得邮件标题里面的发件人名字
	 * 
	 * @param sTitle
	 * @return
	 */
	private static String getSender(String sTitle) {
		//
		String sSender = "";

		String regex = "\\[.+?\\s";

		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
		Matcher m = pattern.matcher(sTitle);

		while (m.find()) {
			sSender = m.group();
			// System.out.println(sSender);
			sSender = sSender.replace("[", "");
			// System.out.println(sSender);
		}

		return sSender;
	}

	/**
	 * 取得邮件标题里面的标题
	 * 
	 * @param sTitle
	 * @return
	 */
	private static String getTitle(String sTitle) {
		//
		String Title = "";

		String regex = "\\].*";

		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
		Matcher m = pattern.matcher(sTitle);

		while (m.find()) {
			Title = m.group();
			// System.out.println(Title);
			Title = Title.replace("]", "");

			Title = Title.replaceAll("\\&nbsp.*", "");
			// Title=Title.replace("&nbsp;.*", "");
			// Title=Title.replace(".html", "");
			// System.out.println(sSender);
		}
		return Title;
	}

	/**
	 * 添加TAB控件的标签卡
	 * 
	 * @return
	 */
	protected static JPanel CreateTabLabel(String title) {

		final JPanel jp = new JPanel();
		// 以邮件的名称做标题
		String tab_sub_title = "";

		if (title.length() > 10) {
			tab_sub_title = title.substring(0, 10) + "...";
		} else {
			tab_sub_title = title;
		}
		JLabel jl_title = new JLabel(tab_sub_title);
		jl_title.setBackground(null);
		final JLabel jl_close = new JLabel(new ImageIcon("res/white_close.png"));
		jl_close.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				jl_close.setIcon(new ImageIcon("res/white_close.png"));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				jl_close.setIcon(new ImageIcon("res/black_close.png"));

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				tabList.remove(tabbedPane.indexOfTabComponent(jp) - 1);
				tabbedPane.remove(tabbedPane.indexOfTabComponent(jp));
				// //System.out.println("================== start
				// ====================");
				// for (int n = 0; n < tabList.size(); n++) {
				// //System.out.println(tabList.get(n));
				// }
				// //System.out.println("================== end
				// ====================");
			}
		});
		jp.add(jl_title);
		jp.add(jl_close);

		return jp;

	}

	/**
	 * 创建tab控件的子面板内容
	 */

	protected static JPanel CreateTabContent(long MsgID) throws IOException {
		// tab的内容页面，包含一个editpanel,一个list、一个按钮
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, 1));
	
		mainPanel.setSize(300, 480);
		mainPanel.setBounds(0, 0, 300, 480);
		mainPanel.setBackground(new Color(200, 200, 150));

		// 用一个editpanel 控件来显示html页面
		JScrollPane jsp_html = new JScrollPane();
		jsp_html.setSize(350, 380);
		jsp_html.setBounds(0, 0, 350, 380);
		JEditorPane htmlpanel = new JEditorPane();
		htmlpanel.setBounds(0, 0, 350, 380);

		// 设置html解释器，否则只会直接输出html原码
		htmlpanel.setEditorKit(new HTMLEditorKit());
		// 因为editorpane不支持meta,所以要先过滤
		CharSequence oldChar = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">	";
		htmlpanel.setText(getMailContent(MsgID).replace(oldChar, ""));
		//htmlpanel.setText(getMailContent(MsgID));

		jsp_html.setViewportView(htmlpanel);
		// 附件列表，用于显示该邮件的列表
		JScrollPane jsp_list = new JScrollPane();
		jsp_list.setSize(100, 500);
		jsp_list.setBounds(550, 0, 100, 500);
		
		
		final JList<List> attchList = new JList<>();
		
		String sboxType=comboBox_1.getSelectedItem().toString();
		final DefaultListModel lm=new DefaultListModel();
		
		final java.util.List list=get_attchlist_by_MsgId(MsgID,sboxType);
			
			for(int n=0;n<list.size();n++){
				Object[] ob=(Object[]) list.get(n);
				lm.addElement(ob[1]);
			}
		attchList.setModel(lm);
		attchList.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getClickCount()==2){
					int index=attchList.getSelectedIndex();
					Object [] item=(Object[]) list.get(index);
					insertAttchFileData(item);
					downloadAttchFile(   item[0].toString(), (String)item[1]);
					try {
						File dir=new File(".");
						String path=dir.getCanonicalPath();
						//System.out.println(dir.getCanonicalPath());
						Desktop.getDesktop().open( new File(path+"/attchfile/"+(String)item[1]));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace(); 
					}   
				}
				
			}
		});
//		attchList.addListSelectionListener(new ListSelectionListener() {
//			
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
//				
//			int index=attchList.getSelectedIndex();
//			Object [] item=(Object[]) list.get(index);
//			insertAttchFileData(item);
//			downloadAttchFile(   item[0].toString(), (String)item[1]);
//			try {
//				File dir=new File(".");
//				String path=dir.getCanonicalPath();
//				//System.out.println(dir.getCanonicalPath());
//				Desktop.getDesktop().open( new File(path+"/attchfile/"+(String)item[1]));
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			//Process process = run.exec("cmd.exe /k start " + path);
//
//
//			
//			}
//		});
		
	
		
		jsp_list.add(attchList);
		jsp_list.setViewportView(attchList);
		//
		mainPanel.add(jsp_html);
		mainPanel.add(jsp_list);

		return mainPanel;
	}
	
	/**
	 * 返回对应消息号的邮件附件列表
	 * @param MsgId
	 * @param sBoxType
	 * @return
	 */
	public static java.util.List get_attchlist_by_MsgId(long MsgId,String sBoxType){
		
		ArrayList list_attch = new ArrayList();
		
		if(sBoxType.equals("收件箱")){
			java.util.List rec_attch_list = dt.getReceiveAttachList();
			System.out.println("1 list_attch count:"+list_attch.size());
			for(int n=0;n<rec_attch_list.size();n++){
				Object [] r=(Object[]) rec_attch_list.get(n);
				long num=(long) r[4];
	
				if(num==MsgId){
				
					list_attch.add(r);
				}
			}
			System.out.println("2 list_attch count:"+list_attch.size());
		
		}
		
		if(sBoxType.equals("发件箱")){
			java.util.List send_attch_list = dt.getSendAttachList();
			System.out.println("1 list_attch count:"+list_attch.size());
			for(int n=0;n<send_attch_list.size();n++){
				Object [] r=(Object[]) send_attch_list.get(n);
				long num=(long) r[4];
				if(num==MsgId){
					list_attch.add(r);
				}
			}
			System.out.println("2 list_attch count:"+list_attch.size());
		}
		
		return list_attch;
		
	}
	
	

	/**
	 * 取得邮件的正文内容，以html返回
	 */
	protected static String getMailContent(long MsgID) {
		String html = "";
		String spec = "http://96.0.32.11/oa/messageDownload?method=getContent&messageid=" + String.valueOf(MsgID);
		// System.out.println(spec);
		try {
			URL url = new URL(spec);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();

			BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				html = html + line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(html);
		return html;
		// return spec;
	}

	private static void downloadAttchFile(String sMsg_id, String fileName) {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[100000];
		int size = 0;
		/*     */try
		/*     */ {
			/* 169 */String destUrl = "http://96.0.32.11/oa/messageFileDown?id=" + sMsg_id;
			url = new URL(destUrl);
			/* 170 */httpUrl = (HttpURLConnection) url.openConnection();
			/*     */
			/* 172 */httpUrl.connect();
			/*     */
			/* 174 */bis = new BufferedInputStream(httpUrl.getInputStream());
			/*     */
			/* 176 */fos = new FileOutputStream("attchfile\\" + fileName);

			/* 183 */while ((size = bis.read(buf)) != -1) {
				/* 184 */fos.write(buf, 0, size);
			}
			/* 188 */fos.close();
			/* 189 */bis.close();
			/* 190 */httpUrl.disconnect();

			/* 192 */try {
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
		}
	}

	private static ArrayList<String> get_AttchList_db() {
		ArrayList<String> AttchList_db = new ArrayList<>();
		// 用JDBC方法联接Mysql数据库
		String sHost = "localhost";
		String sDBName = "jrrc";
		String sUser = "root";
		String sPassword = "root";
		Connection conn = null;
		String url = "jdbc:mysql://" + sHost + "/" + sDBName + "?" + "user=" + sUser + "&password=" + sPassword;
		try {
			conn = (Connection) DriverManager.getConnection(url);
			Statement smt = (Statement) conn.createStatement();
			String sql = "select a_id     from jrrc_mail_attach  group by a_id";
			ResultSet rs = smt.executeQuery(sql);
			while (rs.next()) {
				AttchList_db.add(rs.getString("a_id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return AttchList_db;

	}

}
