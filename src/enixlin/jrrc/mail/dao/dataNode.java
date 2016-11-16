package enixlin.jrrc.mail.dao;

import java.util.ArrayList;

public class dataNode {

	private String id;
	private String name;
	private String parentId;
	private String xuhao;

	public String getXuhao() {
		return xuhao;
	}

	public void setXuhao(String xuhao) {
		this.xuhao = xuhao;
	}

	private ArrayList<dataNode> chilren;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public ArrayList<dataNode> getChilren() {
		return chilren;
	}

	public void setChilren(ArrayList<dataNode> chilren) {
		this.chilren = chilren;
	}
	
	public String toString(){
		return name;
	}

}
