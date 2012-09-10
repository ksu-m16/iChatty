package ichatty.common.impl;

import java.text.DateFormat;
import java.util.Date;

import ichatty.common.IMessage;
import ichatty.common.IUser;

public class Message implements IMessage {

	
	private IUser user;
	private long timestamp;
	private String text;
	
	public Message(IUser user, long timestamp, String text) {
		this.user = user;
		this.timestamp = timestamp;
		this.text = text;
	}
	
	@Override
	public IUser getUser() {
		return user;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {				
		return "[" + DateFormat.getInstance().format(new Date(timestamp)) + "] "
				+ user.getName() + ": " + text;
	}
}
