package ichatty.history;

import ichatty.common.IUser;

public interface IHistoryProvider {
	IHistory getHistory(IUser user);
	String getName();
}
