package ichatty.net.udp;

import ichatty.common.IMessage;
import ichatty.common.IMessageListener;
import ichatty.common.IUser;
import ichatty.net.INetwork;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UdpNetwork implements INetwork {
	
	IUser user;
	UdpNetworkProvider provider;
	List<IMessageListener> listeners = new LinkedList<IMessageListener>();		
	
	public UdpNetwork(IUser u, UdpNetworkProvider p) {
		user = u;
		provider = p;
	}

	@Override
	public void sendMessage(IMessage m) throws IOException {		
		provider.send(user, m);
	}

	@Override
	public void addMessageListener(IMessageListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMessageListener(IMessageListener l) {
		listeners.remove(l);
	}
	
	void notifyListeners(IMessage m) {
		for (IMessageListener l : listeners) {
			l.onMessage(m);
		}
	}
}
