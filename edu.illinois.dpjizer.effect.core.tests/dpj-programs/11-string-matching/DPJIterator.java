/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * DPJIterator Implementation
 * June 2008
 * @author Robert L. Bocchino Jr.
 *
 * A DPJ iterator.  This interface is almost identical to
 * java.util.Iterator, with three important exceptions:
 *
 * 1. There is a region parameter R, so we can track reads and writes
 * to the region of the object we are iterating over.  There are read
 * and write annotations on the methods.
 *
 * 2. There is a size() method, which is very useful for partitioning
 * loops in parallel iterations.  This implies that all objects
 * iterated over using this interface must know their size.
 *
 * 3. We can index the iterator using get(idx), which is useful in
 * parallel loops.
 */

package DPJRuntime;

public interface DPJIterator<type E, region R> {
	public boolean hasNext() /*reads R*/;
	public E next() /*reads R*/;
	public void remove() /*writes R*/;
	public int size() /*reads R*/;
	public E get(int idx) /*reads R*/;
}