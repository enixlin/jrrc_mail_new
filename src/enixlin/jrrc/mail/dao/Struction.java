package enixlin.jrrc.mail.dao;

public class Struction  {

	private String name;
	private String code;
	private String seria;
	private String parentcode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSeria() {
		return seria;
	}
	public void setSeria(String seria) {
		this.seria = seria;
	}
	public String getParentcode() {
		return parentcode;
	}
	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName();
	}
	
	

	
	
	
	
}
