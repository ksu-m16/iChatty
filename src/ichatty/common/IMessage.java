package ichatty.common;

public interface IMessage {
	IUser getUser();
	long getTimestamp();
	String getText();	
}
