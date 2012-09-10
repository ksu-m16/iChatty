package ichatty.net;

import ichatty.common.IUser;

import java.util.Map;

public interface INetworkProvider {
	INetwork getNetwork(IUser u);
	Map<String, String> getAllUserSettings();
	void setUserSettings(String userId, String settings);
	void close();
	String getName();
}
