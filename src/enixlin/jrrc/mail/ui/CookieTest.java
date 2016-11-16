package enixlin.jrrc.mail.ui;

import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import enixlin.jrrc.mail.net.Http_request;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class CookieTest {

	private JFrame frame;
	private JTextField textField;
	public Http_request http_request;
	private JButton btnNew;
	private JButton btnDelete;
	private JButton btnCheck;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CookieTest window = new CookieTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CookieTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 911, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(83, 42, 235, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		http_request = new Http_request();

		// 新增会话cookie
		btnNew = new JButton("new");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Http_request,Void> sw=new SwingWorker<Http_request, Void>(){

					@Override
					protected Http_request doInBackground() throws Exception {
						String name=textField.getText();
						String urlstring = "http://localhost/jrrc_server_php/home/index/makesession/name/"+name;
						http_request.setUrlsString(urlstring);
						Thread t = new Thread(http_request);
						t.start();
						return http_request;
					}
					
				};
				sw.execute();
				sw.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
					//	lblNewLabel.setText(evt.getNewValue().toString());
						if (evt.getNewValue().toString()=="DONE") {
							System.out.println("done");
							JOptionPane p = new JOptionPane();
							p.showMessageDialog(frame, http_request.getResult());

						}
					}
				});	

			}
		});
		btnNew.setBounds(83, 87, 93, 23);
		frame.getContentPane().add(btnNew);

		// 删除会话cookie
		btnDelete = new JButton("delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SwingWorker<Http_request,Void> sw=new SwingWorker<Http_request, Void>(){

					@Override
					protected Http_request doInBackground() throws Exception {
						String name=textField.getText();
						String urlstring = "http://localhost/jrrc_server_php/home/index/logout";
						http_request.setUrlsString(urlstring);
						Thread t = new Thread(http_request);
						t.start();
						return http_request;
					}
					
				};
				sw.execute();
				sw.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
				//		lblNewLabel.setText(evt.getNewValue().toString());
						if (evt.getNewValue().toString()=="DONE") {
							http_request=new Http_request();
							System.out.println("================end=================");
							JOptionPane p = new JOptionPane();
							p.showMessageDialog(frame, http_request.getResult());

						}
					}
				});	

			}
		});

		btnDelete.setBounds(83, 129, 93, 23);
		frame.getContentPane().add(btnDelete);

		// 检查会话cookie
		btnCheck = new JButton("check");
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SwingWorker<Http_request, Void> sw = new SwingWorker<Http_request, Void>() {

					@Override
					protected Http_request doInBackground() throws Exception {
						String urlstring = "http://localhost/jrrc_server_php/home/index/checksession";
						http_request.setUrlsString(urlstring);
						Thread t = new Thread(http_request);
						t.start();
						return http_request;
					}

				};

				sw.execute();
				sw.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
//						lblNewLabel.setText(evt.getNewValue().toString());
						if (evt.getNewValue().toString()=="DONE") {
							System.out.println("================end=================");
							JOptionPane p = new JOptionPane();
							p.showMessageDialog(frame, http_request.getResult());
//							lblNewLabel.setText(http_request.getCookie());
						}

					}
				});

			}

		});
		btnCheck.setBounds(83, 188, 93, 23);
		frame.getContentPane().add(btnCheck);
		
		JButton btnNewCookie = new JButton("new cookie");
		btnNewCookie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SwingWorker<Http_request,Void> sw=new SwingWorker<Http_request, Void>(){

					@Override
					protected Http_request doInBackground() throws Exception {
						if(http_request==null){
							http_request=new Http_request();
						}
						String name=textField.getText();
						String urlstring = "http://localhost/jrrc_server_php/home/index/makecookie/name/"+name;
						http_request.setUrlsString(urlstring);
						Thread t = new Thread(http_request);
						t.start();
						return http_request;
					}
					
				};
				sw.execute();
				sw.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
					//	lblNewLabel.setText(evt.getNewValue().toString());
						if (evt.getNewValue().toString()=="DONE") {
							System.out.println("================end=================");
							JOptionPane p = new JOptionPane();
							p.showMessageDialog(frame, http_request.getResult());

						}
					}
				});	
				
			}
		});
		btnNewCookie.setBounds(334, 87, 93, 23);
		frame.getContentPane().add(btnNewCookie);
		
		JButton btnDelCookie = new JButton("del cookie");
		btnDelCookie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Http_request,Void> sw=new SwingWorker<Http_request, Void>(){

					@Override
					protected Http_request doInBackground() throws Exception {
						String name=textField.getText();
						String urlstring = "http://localhost/jrrc_server_php/home/index/cookielogout";
						http_request.setUrlsString(urlstring);
						Thread t = new Thread(http_request);
						t.start();
						return http_request;
					}
					
				};
				sw.execute();
				sw.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
				//		lblNewLabel.setText(evt.getNewValue().toString());
						if (evt.getNewValue().toString()=="DONE") {
							http_request=new Http_request();
							System.out.println("================end=================");
							JOptionPane p = new JOptionPane();
							p.showMessageDialog(frame, http_request.getResult());
							http_request=null;

						}
					}
				});	

			}
		});
		btnDelCookie.setBounds(334, 129, 93, 23);
		frame.getContentPane().add(btnDelCookie);
		
		JButton btnCheckCookie = new JButton("check cookie");
		btnCheckCookie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Http_request, Void> sw = new SwingWorker<Http_request, Void>() {

					@Override
					protected Http_request doInBackground() throws Exception {
						if(http_request==null){
							http_request=new Http_request();
						}
						String urlstring = "http://localhost/jrrc_server_php/home/index/checkcookie";
						http_request.setUrlsString(urlstring);
						Thread t = new Thread(http_request);
						t.start();
						return http_request;
					}

				};

				sw.execute();
				sw.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
//						lblNewLabel.setText(evt.getNewValue().toString());
						if (evt.getNewValue().toString()=="DONE") {
							System.out.println("================end=================");
							JOptionPane p = new JOptionPane();
							p.showMessageDialog(frame, http_request.getResult()+"==="+http_request.getCookie());
							
//							lblNewLabel.setText(http_request.getCookie());

						}

					}
				});

				
			}
		});
		btnCheckCookie.setBounds(334, 188, 125, 23);
		frame.getContentPane().add(btnCheckCookie);
	}
}
