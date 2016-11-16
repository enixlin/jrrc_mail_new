package enixlin.jrrc.mail.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;

/**
 * 这是一个JDBC的实例类，专门用于通过JDBC驱动来读写本地或远程数据库
 * 
 * @author linzhenhuan
 *
 */

public class DateBaseAccess {

	private String URL;
	private String DBName;
	private String UserName;
	private String PassWord;
	private Connection conn;
	private Statement sql_request;
	private ResultSet result_set;

	/**
	 * 构造函数，生成一个DateBaseAccess实例对象，该实例对象包括一个读写数据库的连接conn
	 * conn是用来后续读和写数据 的连接句柄
	 * @param uRL  数据库的网络地址
	 * @param dBName  数据库名称
	 * @param userName  数据库用户名称
	 * @param passWord  数据库用户密码
	 * 
	 * 
	 */
	public DateBaseAccess(String uRL, String dBName, String userName, String passWord) {
		super();
		URL = uRL;
		DBName = dBName;
		UserName = userName;
		PassWord = passWord;
		try {
			conn = (Connection) DriverManager.getConnection(
					"jdbc:mysql://localhost/" + DBName + "?" + "user=" + UserName + "&password=" + PassWord);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 数据库SQL请求方法
	 * @param conn
	 * @param sql_request
	 * @return
	 */
	public ResultSet sql_reuqest(Connection conn,Statement sql_request){
		
		return result_set;
		
	}
	
	

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getDBName() {
		return DBName;
	}

	public void setDBName(String dBName) {
		DBName = dBName;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassWord() {
		return PassWord;
	}

	public void setPassWord(String passWord) {
		PassWord = passWord;
	}

}
