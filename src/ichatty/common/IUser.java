package ichatty.common;

public interface IUser extends Comparable<IUser> {
	String getId();
	String getName();
}
