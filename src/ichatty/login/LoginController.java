package ichatty.login;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class LoginController implements ILoginController {
	String dataPath = ".";
	String userName = "";
	short userPort = 6666;
	
	public void setDataPath(String path) {
		dataPath = path;
	}
	
	/* (non-Javadoc)
	 * @see ichatty.login.ILoginController#getUserNames()
	 */
	@Override
	public List<String> getUserNames() {
		File f = new File(dataPath);
		String[] flist = f.list();
		List<String> names = new LinkedList<String>();
		
		for (String s : flist) {
			File uf = new File(dataPath + "/" + s);
			if (s.startsWith(".")) {
				continue;
			}
			if (!uf.isDirectory()) {
				continue;				
			}
			names.add(s);
		}
		return names;
	}
	
	/* (non-Javadoc)
	 * @see ichatty.login.ILoginController#getUserName()
	 */
	@Override
	public String getUserName() {
		return userName;
	}
	
	/* (non-Javadoc)
	 * @see ichatty.login.ILoginController#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(String name) {
		userName = name;
	}
	
	/* (non-Javadoc)
	 * @see ichatty.login.ILoginController#getUserPort()
	 */
	@Override
	public short getUserPort() {
		return userPort;
	}
	
	/* (non-Javadoc)
	 * @see ichatty.login.ILoginController#setUserPort(short)
	 */
	@Override
	public void setUserPort(short port) {
		userPort = port;				
	}

}
