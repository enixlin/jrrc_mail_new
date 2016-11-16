import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;

public class test {

	
	public static void main(String[] args) {
		Boy b=new Boy();
		System.out.println(String.valueOf(b.getAge()));
		
		chengzhang(b);
		System.out.println(String.valueOf(b.getAge()));
		
		
		
	}
	
	public static void chengzhang(Boy b){
		
		 b.setAge(b.getAge()+1);
		 
	}
	
	
	
	

}

class Boy{
	private String nameString="boy";
	private int age=10;

	public Boy() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNameString() {
		return nameString;
	}
	public void setNameString(String nameString) {
		this.nameString = nameString;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
