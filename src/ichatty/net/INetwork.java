package ichatty.net;

import java.io.IOException;

import ichatty.common.IMessage;
import ichatty.common.IMessageListener;

public interface INetwork {
	void sendMessage(IMessage m) throws IOException;
	void addMessageListener(IMessageListener l);
	void removeMessageListener(IMessageListener l);
}
