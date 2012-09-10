package ichatty.net.udp;

import ichatty.common.IMessage;
import ichatty.common.IUser;
import ichatty.net.INetwork;
import ichatty.net.INetworkProvider;
import ichatty.serialize.IMessageSerializer;
import ichatty.serialize.json.JsonMessageSerializer;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class UdpNetworkProvider implements INetworkProvider {
	public static class Settings implements Cloneable {
		public short port = 1234;
		public IMessageSerializer serializer = JsonMessageSerializer.getInstance();
		
		@Override
		protected Object clone() {
			Settings s = new Settings();
			s.port = this.port;
			return s;
		}
	}
	
	public UdpNetworkProvider(Settings s) {
		this.settings = (Settings)s.clone();
	}
	
	public static Settings getDefaultSettings() {
		return new Settings();
	}
	
	public static INetworkProvider getInstance(Settings s) throws SocketException {
		UdpNetworkProvider np = new UdpNetworkProvider(s);
		np.start();
		return np;
	}	
	
	private Map<String, UdpUserSettings> users = new TreeMap<String, UdpUserSettings>();
	private Settings settings = new Settings();
	private DatagramSocket socket;
	private DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
	private Map<IUser, WeakReference<UdpNetwork> > networks = new TreeMap<IUser, WeakReference<UdpNetwork> >();
		
	@Override
	public Map<String, String> getAllUserSettings() {
		Map<String, String> us = new TreeMap<String, String>();		
		for (Entry<String, UdpUserSettings> u : users.entrySet()) {
			us.put(u.getKey(), u.getValue().toString());
		}
		return us;
	}
	
	public void setAllUserSettings(Map<String, String> us) {
		for (Entry<String, String> u : us.entrySet()) {
			setUserSettings(u.getKey(), u.getValue());
		}
	}
	
	@Override
	public void setUserSettings(String userId, String settings) {
		users.put(userId, UdpUserSettings.fromString(settings));		
	}	
	
	private void start() throws SocketException {
		socket = new DatagramSocket(settings.port);
		new Thread(
		    new Runnable() {				
				@Override
				public void run() {
					while (!socket.isClosed()) {
						try {
							socket.receive(packet);							
							process();
							
						} catch (IOException e) {
						}
					}
				}
			} 
		).start();
	}
	
	private void process() {						
		IMessage m = settings.serializer.deserialize(new String(packet.getData(), 0, packet.getLength()));		
		WeakReference<UdpNetwork> wnw = networks.get(m.getUser());
		if (wnw == null) {			
			return;
		}
		UdpNetwork nw = wnw.get();
		if (nw == null) {			
			networks.remove(m.getUser());
			return;
		}
		nw.notifyListeners(m);
	}
	
	void send(IUser u, IMessage m) throws IOException {		
		byte[] buf = settings.serializer.serialize(m).getBytes();		
		DatagramPacket p = new DatagramPacket(buf, buf.length);
		
		UdpUserSettings us = users.get(u.getId());						
		if (us == null) {
			throw new IllegalStateException("attempt to send data to non-existing user");
		}				
		p.setSocketAddress(new InetSocketAddress(us.host, us.port));
				
		socket.send(p);
	}
	
	public void close() {
		socket.close();
	}
	
	@Override
	public INetwork getNetwork(IUser u) {				
		UdpNetwork network = new UdpNetwork(u, this);
		networks.put(u, new WeakReference<UdpNetwork>(network));
		return network;	
	}	
			
	@Override
	public String getName() {	
		return "udp";
	}
}
