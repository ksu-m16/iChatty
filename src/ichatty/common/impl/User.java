package ichatty.common.impl;

import ichatty.common.IUser;

public class User implements IUser {
	
	private String id;
	private String name;
	
	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(IUser o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IUser) {
			IUser u = (IUser)obj;
			return u.getId().equals(this.getId());
		}
		return super.equals(obj);
	}	
}
