package enixlin.jrrc.mail.ui;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import com.google.gson.Gson;

import enixlin.jrrc.mail.net.Http_request;

public class Report {

	private JFrame frame;
	private JTable table;
private ArrayList<reportdate> list;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Report window = new Report();
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
	public Report() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1101, 603);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 1065, 545);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setColumnHeaderView(table);
	}
	
	private void getData(){
		
		SwingWorker sw=new SwingWorker() {

			@Override
			protected ArrayList<reportdate> doInBackground() throws Exception {
				
				ArrayList<reportdate> list=new ArrayList<reportdate>();
				Http_request http_request=new Http_request();
				String url="http://localhost/jrrc_web/Home/report1/unit_report_json/start/20160101/end/20160331";
				http_request.setUrlsString(url);
				Thread thread =new Thread(http_request);
				thread.start();
				String result=(String) http_request.getResult();
				Gson g=new Gson();
				
	
				return list;
			}
	
		};
		sw.execute();
		sw.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				if(arg0.getNewValue().equals("DONE")){
					
				}
				
			}
		});
	}
	
}
