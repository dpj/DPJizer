/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * A simple pair type.
 * @author Robert L. Bocchino Jr.
 */

package DPJRuntime;

public class DPJPair<type T1, T2, region R> {
	public T1 first in R;
	public T2 second in R;

	public DPJPair(T1 first, T2 second) 
	//	pure 
	{
		this.first = first;
		this.second = second;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		sb.append(first.toString());
		sb.append(",");
		sb.append(second.toString());
		sb.append(")");
		return sb.toString();
	}
}
