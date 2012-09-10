package ichatty.net.udp;

public class UdpUserSettings {		
	String host;
	short port;
	
	@Override
	public String toString() {		
		return host + ":" + port;
	}
	
	public static UdpUserSettings fromString(String s) {
		String[] hp = s.split(":");
		if (hp.length != 2) {
			throw new IllegalArgumentException("invalid user settings, should be 'host:port'");
		}
		UdpUserSettings us = new UdpUserSettings();			
		try {
			us.host = hp[0];
			us.port = Short.parseShort(hp[1]);
		} catch (Exception ex) {
			throw new IllegalArgumentException("invalid user settings, should be 'host:port'");
		}
		return us;
	}
}