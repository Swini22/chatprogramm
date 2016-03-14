package model;

import java.sql.Timestamp;

public class ChatroomDTO {
	
	private int userId;
	private String user;
	private int chatroomId;
	private String topic;
	private String lastmessage;
	private Timestamp dateFromLastMessage;
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	public int getChatroomId() {
		return chatroomId;
	}
	public void setChatroomId(int chatroomId) {
		this.chatroomId = chatroomId;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getLastmessage() {
		return lastmessage;
	}
	public void setLastmessage(String lastmessage) {
		this.lastmessage = lastmessage;
	}
	public Timestamp getDateFromLastMessage() {
		return dateFromLastMessage;
	}
	public void setDateFromLastMessage(Timestamp timestamp) {
		this.dateFromLastMessage = timestamp;
	}

}
