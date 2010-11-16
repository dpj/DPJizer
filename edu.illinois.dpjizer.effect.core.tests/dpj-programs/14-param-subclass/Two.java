/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class Universal<region P1> {
	public Universal() {}
}

class PathId<region P2> extends Universal<P2> {
	private String name in P2;

	public PathId() {}

	public String get_name() { 
		return this.name;
	}
}

class ReturnPath<region P6> extends PathId<P6> {
	public ReturnPath() {}
}

class ToInitAllTasks {
	public ToInitAllTasks() {}

	public void ToInitAllTasks(ReturnPath<Root> obj) {
		obj.get_name();
	}
}