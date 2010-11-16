/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class Method implements Comparable<Method> {
	String name;
	String sourceFileName;
	int offset;

	public Method(String name, String sourceFileName, int offset) {
		super();
		this.name = name;
		this.sourceFileName = sourceFileName;
		this.offset = offset;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	@Override
	public String toString() {
		return name.toString() + ":" + offset;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Method))
			return false;
		Method otherMethod = (Method) other;
		return stringEquals(name, otherMethod.name);
		// return (offset == otherMethod.offset) && stringEquals(name,
		// otherMethod.name)
		// && stringEquals(sourceFileName, otherMethod.sourceFileName);
	}

	private boolean stringEquals(String first, String second) {
		return ((first == null) ? second == null : first.equals(second));
	}

	@Override
	public int hashCode() {
		return name.hashCode();
		// I can use the following template:
		// int hash = 1;
		// hash = hash * 31 + someNonNullField.hashCode();
		// hash = hash * 31 + (someOtherField == null ? 0 :
		// someOtherField.hashCode());
		// return hash;
	}

	@Override
	public int compareTo(Method o) {
		return name.compareTo(o.name);
	}

}
