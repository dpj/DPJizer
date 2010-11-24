/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.constraints;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.illinois.dpjizer.utils.Logger;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintsSet implements Constraints {

	Set<Constraint> constraints;

	public ConstraintsSet() {
		super();
		this.constraints = new HashSet<Constraint>();
	}

	@Override
	public boolean add(Constraint constraint) {
		Logger.log("Adding the constraint " + constraint.toString());
		return constraints.add(constraint);
	}

	@Override
	public boolean addAll(Collection<? extends Constraint> constraints) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> objects) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Constraint> iterator() {
		return constraints.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		throw new UnsupportedOperationException();
	}

}
