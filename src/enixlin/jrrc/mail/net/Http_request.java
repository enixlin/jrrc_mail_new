package enixlin.jrrc.mail.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.http.message.BasicNameValuePair;

public class Http_request implements Runnable {

	private String urlsString;

	private ArrayList<BasicNameValuePair> list;
	private String method;
	private Object result;
	private String cookie;

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public Object getResult() {
		return this.result;
	}

	public String getUrlsString() {
		return urlsString;
	}

	public void setUrlsString(String urlsString) {
		this.urlsString = urlsString;
	}

	public ArrayList<BasicNameValuePair> getList() {
		return list;
	}

	public void setList(ArrayList<BasicNameValuePair> list) {
		this.list = list;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

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

	private void deletecookie() {
		this.setCookie("");
	}

	@Override
	public void run() {
		try {
			URL url = new URL(this.getUrlsString());

			// 必须在联接之前设定cookie

			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			System.out.println("==============start=============");
			System.out.println("this.cookie: " + this.cookie);
			
			if (this.cookie != null) {
				System.out.println("proses..add cookie");
				httpURLConnection.addRequestProperty("Cookie", this.cookie);
			} else {
				System.out.println("getting.. cookie");
				this.cookie = httpURLConnection.getHeaderField("set-Cookie");
			
			}
			System.out.println("header cookie: " + httpURLConnection.getRequestProperty("Cookie"));

			// 读取返回的数据
			BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String line = "";
			String content = "";
			while ((line = br.readLine()) != null) {
				content = content + line;
			}
			this.result = content;
			

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}