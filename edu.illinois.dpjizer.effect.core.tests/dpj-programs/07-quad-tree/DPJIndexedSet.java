/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * Interface to DPJ Indexed Set
 * 
 * This class provides a set with add, remove, and iteration
 * operations.  The elements are indexed, so you can use a loop
 * iteration to iterate over them.  The elements are not guaranteed to
 * be in any particular order.  There is no find.
 *
 * June 2008
 * @author Robert L. Bocchino Jr.
 */

package DPJRuntime;

import java.util.*;

public class DPJIndexedSet<type E, region R> {
	private E[]<R> elts in R;
	int size in R = 0;
	int capacity in R = 0;
	public DPJIndexedSet(int capacity) {
		elts = (E[]<R>) new Object[capacity];
	}
	public commutative void add(E e) 
	//	writes R 
	{
		elts[size++] = e;
	}
	public void remove(int idx) {
		elts[idx] = elts[--size];
	}
	public void clear() 
	//	writes R 
	{
		size = 0;
	}
	public int size() 
	//	reads R 
	{
		return size;
	}
	public E get(int idx) 
	//	reads R 
	{
		return elts[idx];
	}
	public DPJIterator<E,R> iterator() 
	//	reads R 
	{
		return new DPJIndexedSetIterator();
	}
	private class DPJIndexedSetIterator implements DPJIterator<E,R> {
		private int currentIdx in R = 0;
		
		public DPJIndexedSetIterator() {}
		
		public boolean hasNext() 
		//		reads R 
		{
			return (currentIdx < size);
		}
		public E next() 
		//		reads R 
		{
			return elts[currentIdx++];
		}
		public int size() 
		//		reads R 
		{
			return size;
		}
		public void remove() 
		//		writes R 
		{
			elts[currentIdx] = elts[--size];
		}
		public E get(int idx) 
		//		reads R 
		{
			return elts[idx];
		}
	}

}
