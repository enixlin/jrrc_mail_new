package enixlin.jrrc.mail.ui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import enixlin.jrrc.mail.dao.StructionDataNode;
import enixlin.jrrc.mail.dao.dataNode;
import enixlin.jrrc.mail.net.Http_request;


public class TreeDemo {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TreeDemo window = new TreeDemo();
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
	public TreeDemo() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 832, 629);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(213, 25, 575, 542);
		frame.getContentPane().add(textField);
		textField.setColumns(100);
		ArrayList<dataNode> list = makeArraylist(getString());
		//DefaultTreeModel model = list2tree(list);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 25, 193, 536);
		frame.getContentPane().add(scrollPane);

		//JTree tree = new JTree(model);
//		tree.setVisibleRowCount(100);
//		scrollPane.setColumnHeaderView(tree);

	}

	private String getString() {
		String result = null;

		// http_request
		String urlString = "http://192.168.31.165/struction.html";
		Http_request http_request = new Http_request();
		http_request.setUrlsString(urlString);
	//	http_request.execute();
//		try {
//	//		result = (String) http_request.get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return result;

	}

	private ArrayList<dataNode> makeArraylist(String html) {
		ArrayList<dataNode> list = new ArrayList<dataNode>();

		String reordString = "";
		String regex_record = "var d[0-9]{1,}.+?;";

		java.util.regex.Pattern p_record = java.util.regex.Pattern.compile(regex_record);
		Matcher m_record = p_record.matcher(html);

		while (m_record.find()) {

			reordString = m_record.group();
			dataNode node = new dataNode();

			String regex_id = "'[0-9]{1,}'";
			String regex_name = "'[^0-9]{1,}'";
			String regex_parent = ",d[0-9]{1,}.+?;";
			String regex_xuhao = " d[0-9]{0,}=";

			java.util.regex.Pattern p_id = java.util.regex.Pattern.compile(regex_id);
			java.util.regex.Pattern p_name = java.util.regex.Pattern.compile(regex_name);
			java.util.regex.Pattern p_parent = java.util.regex.Pattern.compile(regex_parent);
			java.util.regex.Pattern p_xuhao = java.util.regex.Pattern.compile(regex_xuhao);

			Matcher m_id = p_id.matcher(reordString);
			Matcher m_name = p_name.matcher(reordString);
			Matcher m_parent = p_parent.matcher(reordString);
			Matcher m_xuhao = p_xuhao.matcher(reordString);

			if (m_id.find()) {
				node.setId(m_id.group().replace("'", ""));
			}
			if (m_name.find()) {
				node.setName(m_name.group().replace("'", ""));
			}
			if (m_parent.find()) {
				node.setParentId(m_parent.group().replace(",", "").replace(");", ""));
			}
			if (m_xuhao.find()) {
				node.setXuhao(m_xuhao.group().replace(" ", "").replace("=", ""));
			}

			list.add(node);

		}

		return list;

	}


}
