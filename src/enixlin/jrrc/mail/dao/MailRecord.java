package enixlin.jrrc.mail.dao;

public class MailRecord {
private String sender;
private String receier;
private String title;
private String timestamp;
private Long messageid;


public Long getMessageid() {
	return messageid;
}
public void setMessageid(Long messageid) {
	this.messageid = messageid;
}
public String getSender() {
	return sender;
}
public void setSender(String sender) {
	this.sender = sender;
}
public String getReceier() {
	return receier;
}
public void setReceier(String receier) {
	this.receier = receier;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getTimestamp() {
	return timestamp;
}
public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}
}
