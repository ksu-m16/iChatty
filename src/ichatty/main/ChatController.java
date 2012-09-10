package ichatty.main;
import ichatty.common.IMessage;
import ichatty.common.IMessageListener;
import ichatty.common.IUser;
import ichatty.common.impl.Message;
import ichatty.common.impl.User;
import ichatty.history.IHistory;
import ichatty.history.IHistoryProvider;
import ichatty.net.INetwork;
import ichatty.net.INetworkProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class ChatController {
	
	public ChatController(IUser me) {
		this.me = me;
	}
	
	private IHistoryProvider hp;
	private INetworkProvider np;
		
	private IUser me;
	private IUser user;	
	IHistory history;
	INetwork network;
			
	public void setHistoryProvider(IHistoryProvider hp) {		
		this.hp = hp;
	}
	
	public void setNetworkProvider(INetworkProvider np) {
		this.np = np;
	}

	IMessageListener userListener; 
	private IMessageListener listener = new IMessageListener() {		
		@Override
		public void onMessage(IMessage m) {
			try {
				history.addMessage(m);
				notifyUserListener(m);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};		
	
	public void setUser(IUser u) {
		
		System.out.println("set user: " + u.getName());
		
		user = u;
		history = hp.getHistory(u);		
		if (network != null) {
			network.removeMessageListener(listener);
		}
		network = np.getNetwork(u);
		network.addMessageListener(listener);
	}
	
	public IUser getUser() {
		return user;				
	}	
	
	private File getNetworkSettingsFile() {
		return new File("./chatty/" + me.getId() + "/" + user.getId() + "/" + np.getName() + ".settings");
	}
	
	private void storeNetworkSettings(String s) throws IOException {
		File f = getNetworkSettingsFile();		
		f.getParentFile().mkdirs();		
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f));
		try {
			osw.write(s);
		} finally {
			osw.close();
		}		
	}
	
	private String readNetworkSettings() throws IOException {
		File f = getNetworkSettingsFile();
		if (!f.exists()) {
			return null;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String res = null;
		try {
			res = br.readLine();
		} finally {
			br.close();
		}		
		return res;
	}
	
	public void setNetworkSettings(String s) {
		np.setUserSettings(user.getId(), s);
		try {
			storeNetworkSettings(s);
		} catch(IOException ex) {			
		}
	}
	
	public String getNetworkSettings() {
		try {
			String settings = readNetworkSettings();
			if (settings != null) {
				np.setUserSettings(user.getId(), settings);
			}
			return settings;
		} catch(IOException ex) {
			return null;
		}		
	}
	
	public Set<IUser> getAllUsers() {
		File f = new File("./chatty/" + me.getId());
		String[] items = f.list();
		if (items == null) {
			return new TreeSet<IUser>();
		}
		
		Set<IUser> us = new TreeSet<IUser>();
		for (String item : items) {
			if (item.startsWith(".")) {
				continue;
			}			
			if (!new File(f.getPath() + "/" + item).isDirectory()) {
				continue;
			}
			us.add(new User(item, item));
		}
		return us;
	}
	
	private void notifyUserListener(IMessage m) {
		if (userListener != null) {
			userListener.onMessage(m);
		}
	}
		
	public void setMessageListener(IMessageListener l) {
		userListener = l;
	}
	
	public void clearMessageListener() {
		userListener = null;
	}
	
	public void sendMessage(String text) throws IOException {
		if (network == null) {
			return;
		}
		IMessage m = new Message(me, System.currentTimeMillis(), text);
		network.sendMessage(m);
		history.addMessage(m);
		notifyUserListener(m);
	}
	
	public List<IMessage> getHistory() throws IOException {
		if (history != null) {
			return history.getMessages();
		}
		return new LinkedList<IMessage>();
	}
}
