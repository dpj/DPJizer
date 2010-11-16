/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * DPJSetWrapper
 * June 2008
 * @author Robert L. Bocchino Jr.
 *
 * This is a wrapper class that turns ordinary Java sets into DPJSets.
 * It provides a "quick and dirty" implementation of a DPJ set where
 * actual parallel execution is not required.  It is useful for
 * testing and sequential performance modeling, but not actual
 * parallel runs.
 */

package DPJRuntime;

public class DPJSetWrapper<type E, region R> implements DPJSet<E,R> {
	private java.util.Set<E> elts in R;

	public DPJSetWrapper(java.util.Set<E> elts) {
		this.elts = elts;
	}

	public commutative boolean add(E o) 
	//	writes R 
	{
		return elts.add(o);
	}

	public void clear() 
	//	writes R 
	{
		elts.clear();
	}

	public int size() 
	//	reads R 
	{
		return elts.size();
	}

	public DPJIterator<E,R> iterator() 
	//	reads R 
	{
		return new DPJSetWrapperIterator();
	}

	private class DPJSetWrapperIterator implements DPJIterator<E,R> {
		private java.util.Iterator<E> iter in R;
		private int currentIdx in R= 0;

		public DPJSetWrapperIterator() {
			this.iter = elts.iterator();
		}

		public boolean hasNext() 
		//		reads R 
		{
			return iter.hasNext();
		}

		public E next() 
		//		reads R 
		{
			++currentIdx;
			return iter.next();
		}

		public int size() 
		//		reads R 
		{
			return elts.size();
		}

		public void remove() 
		//		writes R 
		{
			iter.remove();
		}

		public E get(int idx) 
		//		reads R 
		{
			if (idx == currentIdx) {
				return next();
			} else {
				System.err.println("This is broken!");
				System.exit(1);
			}
			return null;
		}
	}

	public static void main(String[] args) {
		DPJSet<Integer> S = new DPJSetWrapper<Integer>(new java.util.HashSet<Integer>());
		S.add(0);
		S.add(1);
		S.add(2);
		foreach (Integer I in S.iterator()) {
			System.out.println(I);
		}
	}
}