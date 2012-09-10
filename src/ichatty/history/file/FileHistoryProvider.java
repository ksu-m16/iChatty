package ichatty.history.file;

import ichatty.common.IUser;
import ichatty.history.IHistory;
import ichatty.history.IHistoryProvider;
import ichatty.serialize.IMessageSerializer;
import ichatty.serialize.json.JsonMessageSerializer;

public class FileHistoryProvider implements IHistoryProvider {
	
	static public class Settings {
		public String dataPath = "./history/";
		public IMessageSerializer serializer = JsonMessageSerializer.getInstance();
		
		public Settings setDataPath(String dataPath) {
			this.dataPath = dataPath;
			return this;
		}
		
		public Settings setSerializer(IMessageSerializer ms) {
			this.serializer = ms;
			return this;
		}
		
		@Override
		protected Object clone() {
			Settings s = new Settings();
			s.dataPath = this.dataPath;
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
		String fileName = settings.dataPath + "/" + user.getId() + "/" + u.getId() + "/history.json";
		return new FileHistory(fileName, settings.serializer);
	}

	@Override
	public String getName() {
		return "file";
	}

}
