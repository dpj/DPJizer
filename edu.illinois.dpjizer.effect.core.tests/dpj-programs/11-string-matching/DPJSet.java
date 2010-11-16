/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * DPJ Set interface
 * June 2008
 * @author Robert L. Bocchino Jr.
 */

package DPJRuntime;

public interface DPJSet<type E, region R> {
	public commutative boolean add(E o) /* writes R */;
	public void clear() /* writes R */;
	public int size() /* reads R */;
	public DPJIterator<E,R> iterator() /* reads R */;
}