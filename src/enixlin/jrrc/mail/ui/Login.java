package enixlin.jrrc.mail.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.apache.http.message.BasicNameValuePair;

import com.sun.jna.Library.Handler;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

import enixlin.jrrc.mail.dao.Struction;
import enixlin.jrrc.mail.net.Http_request;
import sun.misc.BASE64Encoder;
import sun.net.www.protocol.http.HttpURLConnection;
import sun.nio.ch.ThreadPool;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.awt.event.ActionEvent;

/**
 * 程序入口，主要是用于校验用户的身份 在身份校验成功后： 1.生成一个用户机构的树状数据结构 2.记录当前网络连接使用的cookie
 * 3.跳转到MainWindow类
 * 
 * @author linzhenhuan
 *
 */
public class Login {

	private JFrame frmjava;
	private JTextField txtLinzhenhuan;
	private JPasswordField pwdlyp;
	private String cookie = "";
	private ArrayList<Struction> allStruction;
	private DefaultMutableTreeNode root;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmjava.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmjava = new JFrame();
		frmjava.getContentPane().setFocusable(false);
		frmjava.setTitle("\u90AE\u4EF6\u5BA2\u6237\u7AEF-java\u7248");
		frmjava.setBounds(100, 100, 346, 300);
		frmjava.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmjava.getContentPane().setLayout(null);

		JLabel label = new JLabel("\u6237\u540D");
		label.setBounds(33, 50, 32, 15);
		frmjava.getContentPane().add(label);

		txtLinzhenhuan = new JTextField();
		txtLinzhenhuan.setText("linzhenhuan");
		txtLinzhenhuan.setBounds(77, 47, 205, 21);
		frmjava.getContentPane().add(txtLinzhenhuan);
		txtLinzhenhuan.setColumns(10);

		JLabel label_1 = new JLabel("\u5BC6\u7801");
		label_1.setBounds(33, 111, 32, 15);
		frmjava.getContentPane().add(label_1);

		pwdlyp = new JPasswordField();
		pwdlyp.setText("39453945lyp");
		pwdlyp.setBounds(77, 108, 205, 21);
		frmjava.getContentPane().add(pwdlyp);

		JButton btnNewButton = new JButton("退    出");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnNewButton.setBounds(75, 163, 93, 46);
		frmjava.getContentPane().add(btnNewButton);

		JButton button = new JButton("登    录");
		button.setFocusCycleRoot(true);
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getInitialCookie();
				handle_login();
				// 取得所有的机构用户信息
				allStruction = getAllStructions();
				// 将所有的机构用户信息生成一棵用户树，用于tree控件
				makeTree(allStruction);

				 MainPanel mainpanel = new MainPanel(root, cookie);
				

			}
		});
		button.setBounds(189, 163, 93, 46);
		frmjava.getContentPane().add(button);
	}

	/**
	 * //处理登录事件
	 */
	protected void handle_login() {

		// 取得用户名
		String name = txtLinzhenhuan.getText();
		// 取得用户密码
		String password = formatpassword(String.valueOf(pwdlyp.getPassword()));

		String urlsString = "http://96.0.32.11/oa/login.do?";
		// request.setUrlsString(urlsString);
		ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("loginName", name));
		list.add(new BasicNameValuePair("passWord", password));
		list.add(new BasicNameValuePair("engine", "96.0.31.11:1093"));

		String s = serializeParams(list);
		URL url;
		try {
			url = new URL(urlsString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Cookie", cookie);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			bw.write(s);
			bw.flush();

			bw.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String line = "";
			String content = "";
			while ((line = br.readLine()) != null) {
				content = content + line + "\n";
			}
			// System.out.println(content);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 格式化密码
	 * 
	 * @param valueOf
	 * @return
	 */

	private String formatpassword(String valueOf) {
		String miwen = "";
		try {
			miwen = new BASE64Encoder().encode(valueOf.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return miwen;
	}

	/**
	 * 串行化请求参数
	 * 
	 * @param list
	 * @return
	 */
	private String serializeParams(ArrayList<BasicNameValuePair> list) {
		String resultString = "";
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				resultString = resultString + list.get(i).getName() + "=" + list.get(i).getValue();
			} else {
				resultString = resultString + "&" + list.get(i).getName() + "=" + list.get(i).getValue();
			}
		}

		return resultString;
	}

	/**
	 * 取得初始cookie;
	 * 
	 * @return
	 */
	private String getInitialCookie() {
		// 请求oa网址，取得cookie,之后一直使用这个cookie

		String urlsString = "http://96.0.32.11/oa/login.do";

		try {
			URL url = new URL(urlsString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			Map<String, List<String>> heads = conn.getHeaderFields();
			for (Entry<String, List<String>> header : heads.entrySet()) {
				// System.out.println("cookie is "+header.getKey());
				if (header.getKey() != null && header.getKey().equals("Set-Cookie")) {
					// System.out.println(header.getKey()+" is
					// "+header.getValue());
					cookie = cookie + header.getValue().toString();
				}
			}

			cookie = cookie.replace("[", "");
			cookie = cookie.replace("path=/, ", "");
			cookie = cookie.replace("; path=/; HttpOnly=]", "");
			// System.out.println("cookie is " + cookie);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cookie;
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

	private ArrayList<Struction> getAllStructions() {
		ArrayList<Struction> aAllStructions = new ArrayList<>();

		SwingWorker<String, Void> sw = new SwingWorker<String, Void>() {

			@Override
			protected String doInBackground() throws Exception {
				String result = "";
				// 请求网址
				String strUrl = "http://96.0.32.11/oa/groupSelect.do?cmd=left&selectType='3'&orgId=0&showType=3&selectGroupId=null";
				URL url = new URL(strUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Cookie", cookie);

				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String line = "";

				while ((line = br.readLine()) != null) {
					result = result + line + "\n";
				}

				return result;
			}

		};

		sw.execute();

		try {
			String html = sw.get();
			aAllStructions = getStructionLine(html);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return aAllStructions;
	}

	private ArrayList<Struction> getStructionLine(String html) {
		ArrayList<Struction> aStruction = new ArrayList<>();

		String regex = "var d[0-9]{1,}.+?;";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
		Matcher m = p.matcher(html);

		while (m.find()) {
			aStruction.add(getDataNode(m.group()));

		}

		return aStruction;

	}

	private DefaultMutableTreeNode makeTree(ArrayList<Struction> aAllStructions) {
		root = new DefaultMutableTreeNode(aAllStructions.get(0));
		for (Struction ListNode : aAllStructions) {
			Enumeration em = root.postorderEnumeration();
			DefaultMutableTreeNode TreeNode = null;
			while ((TreeNode = (DefaultMutableTreeNode) em.nextElement()) != null) {
				Struction s = (Struction) TreeNode.getUserObject();
				if (ListNode.getParentcode() != null && ListNode.getParentcode().equals(s.getSeria())) {
					TreeNode.add(new DefaultMutableTreeNode(ListNode));
				}
			}
		}

		return root;

	}
}
