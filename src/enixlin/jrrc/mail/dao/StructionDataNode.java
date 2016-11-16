package enixlin.jrrc.mail.dao;

import javax.swing.tree.DefaultMutableTreeNode;

public class StructionDataNode extends DefaultMutableTreeNode {
	
	private dataNode node;

	public StructionDataNode(dataNode node) {
		setNode(node);
	}

	public StructionDataNode() {
		// TODO Auto-generated constructor stub
	}


	public dataNode getNode() {
		return node;
	}

	public void setNode(dataNode node) {
		this.node = node;
	}
	

	

}
