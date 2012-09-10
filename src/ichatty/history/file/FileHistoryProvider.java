package ichatty.history.file;

import ichatty.common.IUser;
import ichatty.history.IHistory;
import ichatty.history.IHistoryProvider;
import ichatty.serialize.IMessageSerializer;
import ichatty.serialize.json.JsonMessageSerializer;

public class FileHistoryProvider implements IHistoryProvider {
	
	static public class Settings {
		public String basePath = "./history/";
		public IMessageSerializer serializer = JsonMessageSerializer.getInstance();
		
		@Override
		protected Object clone() {
			Settings s = new Settings();
			s.basePath = this.basePath;
			s.serializer = this.serializer;
			return s;
		}
	}			
		
	public static Settings getDefaultSettings() {
		return new Settings();
	}
	
	public static IHistoryProvider getInstance(IUser u, Settings s) {
		return new FileHistoryProvider(u, s);
	}
		
	private IUser user;
	private Settings settings;

	private FileHistoryProvider(IUser u, Settings s) {
		user = u;
		settings = (Settings)s.clone();
	}	
		
	@Override
	public IHistory getHistory(IUser u) {
		String fileName = settings.basePath + "/" + user.getId() + "/" + u.getId() + ".history";
		return new FileHistory(fileName, settings.serializer);
	}

	@Override
	public String getName() {
		return "file";
	}

}
