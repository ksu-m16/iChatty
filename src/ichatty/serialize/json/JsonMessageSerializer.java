package ichatty.serialize.json;

import ichatty.common.IMessage;
import ichatty.common.IUser;
import ichatty.common.impl.Message;
import ichatty.common.impl.User;
import ichatty.serialize.IMessageSerializer;

import com.google.gson.Gson;

public class JsonMessageSerializer implements IMessageSerializer {		
	private static class Loader {
		private static final JsonMessageSerializer instance = new JsonMessageSerializer();
	}
	
	public static JsonMessageSerializer getInstance() {
		return Loader.instance;
	}	
	
	private static class JsonMessage {		
		private static class JsonUser {			
			public JsonUser(IUser u) {
				id = u.getId();
				name = u.getName();
			}
			
			public IUser toUser() {
				return new User(id, name);
			}
			
			String id;
			String name;			
		}
		
		public JsonMessage() {			
		}
		
		public JsonMessage(IMessage m) {
			user = new JsonUser(m.getUser());
			text = m.getText();
			time = m.getTimestamp();
		}
		
		public IMessage toMessage() {
			return new Message(user.toUser(), time, text);
		}
		
		JsonUser user;		
		String text;
		long time;
	}	
	
	Gson gson = new Gson();

	@Override
	public String serialize(IMessage m) {
		return gson.toJson(new JsonMessage(m));
	}

	@Override
	public IMessage deserialize(String s) { 
		return gson.fromJson(s, JsonMessage.class).toMessage();
	}
}
