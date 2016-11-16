package enixlin.jrrc.mail.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import enixlin.jrrc.mail.dao.Struction;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JProgressBar;

/**
 * 主界面类，主要界面显示时在后台进行两项操作 1.从数据库中读取所有的已有邮件用户信息，根据allStruction生成一个arraylist
 * <Struction> user_inDB 2.同步user_inDB列表里的每一个用户的邮件，同步使用Syn_MailToDB类
 * 
 * @author linzhenhuan
 *
 */

public class MainWindow {

	private JPanel contentPane;
	private ArrayList<Struction> allStruction;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTable table;
	private JLabel lblNewLabel;
	private JProgressBar progressBar;

	public JLabel getLblNewLabel() {
		return lblNewLabel;
	}

	public void setLblNewLabel(JLabel lblNewLabel) {
		this.lblNewLabel = lblNewLabel;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public MainWindow(ArrayList<Struction> allStruction) {

		this.allStruction = allStruction;
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

	/**
	 * Create the frame.
	 */
	public void initial() {

		// TODO Auto-generated method stub
		JFrame frame = new JFrame();
		frame.setTitle("主功能面板");
		frame.setSize(1193, 639);
		contentPane = new JPanel();
		

	
		contentPane.setLayout(null);

		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(10, 10, 103, 21);
		contentPane.add(comboBox);

		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(133, 10, 93, 21);
		contentPane.add(comboBox_1);

		JLabel label = new JLabel("日期");
		label.setBounds(247, 13, 33, 15);
		contentPane.add(label);

		textField = new JTextField();
		textField.setBounds(279, 10, 85, 21);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel label_1 = new JLabel("至");
		label_1.setBounds(374, 13, 24, 15);
		contentPane.add(label_1);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(400, 10, 93, 21);
		contentPane.add(textField_1);

		textField_2 = new JTextField();
		textField_2.setBounds(547, 10, 226, 21);
		contentPane.add(textField_2);
		textField_2.setColumns(10);

		JButton button = new JButton("查找");
		button.setBounds(795, 9, 93, 23);
		contentPane.add(button);

		JLabel label_2 = new JLabel("关键字");
		label_2.setBounds(503, 13, 45, 15);
		contentPane.add(label_2);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 41, 1165, 525);
		contentPane.add(scrollPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		scrollPane.setViewportView(tabbedPane);

		table = new JTable();
		tabbedPane.addTab("New tab", null, table, null);

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 576, 167, 19);
		contentPane.add(progressBar);

		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(187, 576, 988, 15);
		contentPane.add(lblNewLabel);
	}
	
	private void getUserListFromDB(){
		///
		
		//master
	}
}
