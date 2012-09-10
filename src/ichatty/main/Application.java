package ichatty.main;
import ichatty.common.IMessage;
import ichatty.common.IMessageListener;
import ichatty.common.IUser;
import ichatty.common.impl.User;
import ichatty.history.IHistoryProvider;
import ichatty.history.file.FileHistoryProvider;
import ichatty.net.INetworkProvider;
import ichatty.net.udp.UdpNetworkProvider;


public class Application {
	public static void main(String[] args) {
		
		try {
			IUser me = new User("kernel", "kernel");
			IUser xu = new User("xu", "xu");					
		
			FileHistoryProvider.Settings hps = FileHistoryProvider.getDefaultSettings();
			hps.basePath = "./chatty";
			IHistoryProvider hp = FileHistoryProvider.getInstance(me, hps);
		
			UdpNetworkProvider.Settings nps = UdpNetworkProvider.getDefaultSettings();
			nps.port = 1234;		
			INetworkProvider np = UdpNetworkProvider.getInstance(nps);
			
			Controller c = new Controller(me);			
			c.setHistoryProvider(hp);
			c.setNetworkProvider(np);
			
			c.setMessageListener(new IMessageListener() {				
				@Override
				public void onMessage(IMessage m) {
					System.out.println(m);					
				}
			});
			
			c.setUser(me);
			c.setNetworkSettings("localhost:1234");
			c.sendMessage("hello!!!");						
			
			Thread.sleep(1000);
			
			np.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
