package ichatty.login;

import java.util.List;

public interface ILoginController {

	public abstract List<String> getUserNames();

	public abstract String getUserName();

	public abstract void setUserName(String name);

	public abstract short getUserPort();

	public abstract void setUserPort(short port);

}