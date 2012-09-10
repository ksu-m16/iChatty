package ichatty.history;

import ichatty.common.IMessage;

import java.io.IOException;
import java.util.List;

public interface IHistory {
	List<IMessage> getMessages() throws IOException;
	void addMessage(IMessage m) throws IOException;
}
