/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * DPJHashSet Implementation
 * June 2008
 * @author Robert L. Bocchino Jr.
 *
 * DPJHashSet implementation that uses a DPJSetWrapper to wrap a
 * java.util.HashSet.
 */

package DPJRuntime;

// TODO:  IMPLEMENT THIS WITH INHERITANCE, NOT COMPOSITION

public class DPJHashSet<type E, region R> implements DPJSet<E,R> {
	private DPJSetWrapper<E,R> elts in R =
		new DPJSetWrapper<E,R>(new java.util.HashSet<E>());

	public DPJHashSet() {}
	
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
		return elts.iterator();
	}

	public static void main(String[] args) {
		DPJSet<Integer> S = new DPJHashSet<Integer>();
		S.add(0);
		S.add(1);
		S.add(2);
		foreach (Integer I in S.iterator()) {
			System.out.println(I);
		}
	}
}