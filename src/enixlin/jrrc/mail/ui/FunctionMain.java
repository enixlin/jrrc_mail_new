package enixlin.jrrc.mail.ui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import enixlin.jrrc.mail.dao.Struction;
import enixlin.jrrc.mail.dao.Tablemodel;
import enixlin.jrrc.mail.dao.User;
import enixlin.jrrc.mail.net.Http_request;

public class FunctionMain {

	private JFrame frame;
	private ArrayList<Struction> arealist;
	private ArrayList<Struction> allStructions;
	private ArrayList<Struction> banklist;
	private ArrayList<Struction> departmentlist;
	private JScrollPane scrollPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager uiManager=new UIManager();
					//uiManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
					uiManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
					FunctionMain window = new FunctionMain();
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
	public FunctionMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1059, 588);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// 初始化全省机构列表，并显示地区级列表

		DefaultMutableTreeNode root = makeTree(getStructions());

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 205, 359);
		frame.getContentPane().add(scrollPane);

		JTree tree = new JTree(root);
	
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				TreePath treePath=arg0.getNewLeadSelectionPath();
				DefaultMutableTreeNode node=(DefaultMutableTreeNode) treePath.getLastPathComponent();
				Struction s=(Struction) node.getUserObject();
				System.out.println(s.getCode());
		
			}
		});
		scrollPane.setViewportView(tree);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(243, 10, 683, 335);
		frame.getContentPane().add(scrollPane_1);
		
		table = new JTable();
		scrollPane_1.setViewportView(table);

	}

	/**
	 * 取得全省所有的机构
	 * 
	 * @return
	 */
	private ArrayList<Struction> getStructions() {

		allStructions = new ArrayList();
		String spec = "http://192.168.31.165/struction.html";
		Http_request request = new Http_request();
		request.setUrlsString(spec);
//		request.execute();
		String result = "";
//		try {
//			result = (String) request.get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// textField.setText(result);

		String regexString = "var d[0-9]{1,}.+?;";

		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regexString);
		java.util.regex.Matcher matcher = pattern.matcher(result);
		long count = 0;
		while (matcher.find()) {
			allStructions.add(getDataNode(matcher.group()));
			//System.out.println(matcher.group());
		}

		return allStructions;

	}

	private Struction getDataNode(String line) {
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

			dataNode.setName(matcher_name.group().replace("'", ""));
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

	private DefaultMutableTreeNode makeTree(ArrayList<Struction> list) {
		DefaultMutableTreeNode root = null;
		for (Struction node : list) {
			if (node.getParentcode() == null) {
				root = new DefaultMutableTreeNode(node.getSeria());
				root.setUserObject(node);
			}
		}
		for (Struction node : list) {
			Enumeration em = root.postorderEnumeration();
			DefaultMutableTreeNode rootnode = null;
			while ((rootnode = (DefaultMutableTreeNode) em.nextElement()) != null) {

				Struction s = (Struction) rootnode.getUserObject();

				if (node.getParentcode() != null && node.getParentcode().equals(s.getSeria())) {
					rootnode.add(new DefaultMutableTreeNode(node));

				}
			}
		}

		return root;

	}

	private User getUser(String line) {
		User user = new User();
		String regex_id = " d[0-9]{0,}="; // 节点号
		String regex_name = "'[^0-9]{1,}'"; // 机构名称
		String regex_department = ",d[0-9]{1,}.+?;"; // 上级节点号

		java.util.regex.Pattern pattern_id = java.util.regex.Pattern.compile(regex_id);
		java.util.regex.Pattern pattern_name = java.util.regex.Pattern.compile(regex_name);
		java.util.regex.Pattern pattern_department = java.util.regex.Pattern.compile(regex_department);

		java.util.regex.Matcher matcher_id = pattern_id.matcher(line);
		java.util.regex.Matcher matcher_name = pattern_name.matcher(line);
		java.util.regex.Matcher matcher_department = pattern_department.matcher(line);

		if (matcher_id.find()) {

			user.setId(matcher_id.group().replace("'", ""));

		}
		if (matcher_name.find()) {

			user.setName(matcher_name.group().replace("'", ""));
			// System.out.println(matcher_name.group());
		}
		if (matcher_department.find()) {

			user.setDepartment(matcher_department.group().replace(" ", "").replace("=", ""));
		}

		return user;
	}
	
	private void initTable(ArrayList list){
		Tablemodel tm=new Tablemodel();
		
	}
}
