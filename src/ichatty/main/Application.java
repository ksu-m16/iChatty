package ichatty.main;
import java.net.SocketException;

import javax.security.auth.login.LoginContext;

import ichatty.common.IMessage;
import ichatty.common.IMessageListener;
import ichatty.common.IUser;
import ichatty.common.impl.User;
import ichatty.history.IHistoryProvider;
import ichatty.history.file.FileHistoryProvider;
import ichatty.login.LoginController;
import ichatty.login.LoginView;
import ichatty.net.INetworkProvider;
import ichatty.net.udp.UdpNetworkProvider;


public class Application {
	
	
	LoginController loginController = new LoginController();
	LoginView loginView;	
	
	ChatController chatController;
	IHistoryProvider historyProvider;
	INetworkProvider netProvider;
	ChatView chatView;
	
	String dataPath = "./chatty";
	
	
	protected boolean login() {
		loginView = new LoginView();
		loginController.setDataPath(dataPath);
		loginView.setController(loginController);				
		loginView.run();		
		return loginView.getExitStatus();
	}
	
	protected boolean start() {
		IUser me = new User(loginController.getUserName(), loginController.getUserName());
		try {
			chatView = new ChatView();
			chatController = new ChatController(me);		
			historyProvider = FileHistoryProvider.getInstance(me, 
				FileHistoryProvider.getDefaultSettings()
				.setDataPath(dataPath)
			);			
			netProvider = UdpNetworkProvider.getInstance(
				UdpNetworkProvider.getDefaultSettings()
				.setPort(loginController.getUserPort())
			);
			chatController.setHistoryProvider(historyProvider);
			chatController.setNetworkProvider(netProvider);
			
			chatView.setController(chatController);
			chatView.run();
		} catch (Exception e) {
			System.out.println("failed to start chat");
			return false;
		}				
		
		if (netProvider != null) {
			netProvider.close();
		}		
		return chatView.getExitStatus();
	}
	
	public void run() {		
		while (login()) {
			System.out.println("login as: '" + loginController.getUserName() 
					+ "' at '" + loginController.getUserPort() + "'");
			if (!start()) {
				break;
			}
		}	
	}
	
	public static void main(String[] args) {		
		new Application().run();
		System.exit(0);		
	}
}
