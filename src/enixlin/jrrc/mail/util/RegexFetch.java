package enixlin.jrrc.mail.util;

import java.util.ArrayList;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

public class RegexFetch {
	
	
	private String src;
	private ArrayList<String> regexList;
	private Object result;
	
	
	public RegexFetch(String src,ArrayList<String> regexList) {
		super();
		this.src = src;
		this.regexList=regexList;
	}


}
