package ichatty.serialize;

import ichatty.common.IMessage;

public interface IMessageSerializer {
	String serialize(IMessage m);
	IMessage deserialize(String s);
}
