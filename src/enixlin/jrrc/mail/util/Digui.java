package enixlin.jrrc.mail.util;

import javax.swing.tree.DefaultMutableTreeNode;

public class Digui {
	public static void main(String[] args) {
		
		DefaultMutableTreeNode root=new DefaultMutableTreeNode("root");
		DefaultMutableTreeNode c1=new DefaultMutableTreeNode("c1");
		DefaultMutableTreeNode c2=new DefaultMutableTreeNode("c2");
		DefaultMutableTreeNode c3=new DefaultMutableTreeNode("c3");
		DefaultMutableTreeNode d1=new DefaultMutableTreeNode("d1");
		DefaultMutableTreeNode d2=new DefaultMutableTreeNode("d2");
		DefaultMutableTreeNode d3=new DefaultMutableTreeNode("d3");
		DefaultMutableTreeNode d4=new DefaultMutableTreeNode("d4");
		
		
		root.add(c1);
		root.add(c2);
		root.add(c3);
		c3.add(d1);
		c3.add(d2);
		c3.add(d3);
		c3.add(d4);
		System.out.println(root.children().toString());
		
		//lipianroot(root);

	}
	
	
	public static void lipianroot(DefaultMutableTreeNode root){
		if(root.getLastChild()!=null){
			lipianroot((DefaultMutableTreeNode) root.getLastChild());
		}else{
			root.toString();
		}
	}
}
