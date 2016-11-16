package enixlin.jrrc.mail.dao;

import java.sql.DriverManager;
import org.nutz.dao.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Jdbc_mysql_access {
	
	private String sHost;
	private String sUser;
	private String sPassword;
	private String sDBName;
	private Connection conn;
	private Statement stm;
	private ResultSet rs;
	
	
	
	
	public Jdbc_mysql_access(String sHost, String sUser, String sPassword, String sDBName) {
	
		
		this.sHost = sHost;
		this.sUser = sUser;
		this.sPassword = sPassword;
		this.sDBName = sDBName;
		
		try {
			this.conn=(Connection) DriverManager.getConnection("jdbc:mysql://"+sHost+"/"+sDBName+"?" +
					"user="+sUser+"&password="+sPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	
	
	public ResultSet execute_Request(String sql){
		try {
			stm=(Statement) conn.createStatement();
			rs=stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;		
	}
	
	
	public int execute_insert(ArrayList<String> columns,ArrayList<Object> values,String sql){
		int successCount=0;
		String columnsString="( ";
		String valuesString="( ";
		for(String colmn:columns){
			columnsString=columnsString+",";
		}
		columnsString=columnsString.substring(0, columnsString.length()-2)+")";
		
		 sql = sql+columnsString+valuesString;
		
		
		return successCount;
	}
	
	public String getsHost() {
		return sHost;
	}
	public void setsHost(String sHost) {
		this.sHost = sHost;
	}
	public String getsUser() {
		return sUser;
	}
	public void setsUser(String sUser) {
		this.sUser = sUser;
	}
	public String getsPassword() {
		return sPassword;
	}
	public void setsPassword(String sPassword) {
		this.sPassword = sPassword;
	}
	public String getsDBName() {
		return sDBName;
	}
	public void setsDBName(String sDBName) {
		this.sDBName = sDBName;
	}


	public ResultSet getRs() {
		return rs;
	}

	
	
	
	
	
	
	

}
