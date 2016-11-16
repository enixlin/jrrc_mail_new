package enixlin.jrrc.mail.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.gdbeim.oa.applet.DownloadTask;
import com.gdbeim.oa.applet.HttpGet;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import enixlin.jrrc.mail.dao.Struction;
import enixlin.jrrc.mail.ui.MainWindow;
import sun.net.www.protocol.http.HttpURLConnection;

public class Syn_MailToDB implements Runnable {
	
	private ArrayList<Struction> user_inDB;
	private MainWindow frm;
	

	public Syn_MailToDB(ArrayList<Struction> user_inDB,MainWindow frm) {
		super();
		this.user_inDB = user_inDB;
		this.frm=frm;
	}


	@Override
	public void  run()  {
		for(Struction user:user_inDB){
			DownloadTask dt=getDownLoadTask(user.getCode());
			updateDataBase(dt, user);
		}
	
	}
	
	
	private DownloadTask getDownLoadTask(String strUserCode){
		HttpGet hg = new HttpGet();
		String startDate = "2000-01-01";
		String endDate = "2020-01-01";
		String url = "http://96.0.32.11/oa/messageDownload?method=getDownList" + "&startDate=" + startDate + "&endDate="
				+ endDate + "&userId=" + strUserCode;
		DownloadTask dt = hg.getDownloadTask(url);
		return dt;
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
	protected  void updateDataBase(DownloadTask dt,Struction user) {
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
			frm.getLblNewLabel().setText(rec_msg_id);

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
	
	
	/**
	 * 取得数据库中附件表中的所有记录
	 * @return
	 */
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
			String sql = "select a_id    from jrrc_mail_attach  group by a_id  ";
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
}
