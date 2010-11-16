/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import java.util.ArrayList;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.RPLElement.RPLCaptureParameter;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RPLNomalizer {
	private static final int RPL_MAX_LENGTH = 3;

	/**
	 * If the length of the given RPL is greater than a certain limit, this
	 * method, truncates the RPL at that limit and appends it by a star. This
	 * method also makes sure that there are no two consecutive stars in the
	 * returned RPL.
	 */
	public static RPL canonicalForm(RPL rpl) {
		if (rpl == null)
			return null;
		return removeConsecutiveStars(truncate(removeConsecutiveStars(uncapture(rpl))));
	}

	private static RPL uncaptureRPLElement(RPLCaptureParameter rplCaptureParameter) {
		return rplCaptureParameter.includedIn;
	}

	private static RPL uncapture(RPL rpl) {
		ListBuffer<RPLElement> uncapturedRPL = ListBuffer.<RPLElement> lb();
		for (RPLElement e : rpl.elts) {
			if (e instanceof RPLCaptureParameter) {
				uncapturedRPL.appendList(uncaptureRPLElement((RPLCaptureParameter) e).elts);
			} else
				uncapturedRPL.append(e);
		}
		return new RPL(uncapturedRPL.toList());
	}

	private static RPL removeConsecutiveStars(RPL rpl) {
		RPLElement[] rplElements = new RPLElement[rpl.elts.size()];
		rpl.elts.toArray(rplElements);
		ArrayList<RPLElement> canonicalElements = new ArrayList<RPLElement>();
		canonicalElements.add(rplElements[0]);
		for (int i = 1; i < rplElements.length; i++) {
			if (!(rplElements[i] == RPLElement.STAR && rplElements[i - 1] == RPLElement.STAR))
				canonicalElements.add(rplElements[i]);
		}
		return new RPL(List.from(canonicalElements.toArray(new RPLElement[canonicalElements.size()])));
	}

	public static RPL truncate(RPL rpl) {
		if (rpl.size() <= RPL_MAX_LENGTH)
			return rpl;
		else {
			RPLElement[] rplElts = new RPLElement[rpl.size()];
			rplElts = rpl.elts.toArray(rplElts);

			ListBuffer<RPLElement> truncatedRPLBuffer = ListBuffer.lb();

			int i = 0;
			for (; i < RPL_MAX_LENGTH - 1; ++i) {
				truncatedRPLBuffer.append(rplElts[i]);
			}

			truncatedRPLBuffer.append(RPLElement.STAR);

			return new RPL(truncatedRPLBuffer.toList());
		}
	}

	public static List<RPL> canonicalForm(List<RPL> rpls) {
		if (rpls == null)
			return List.nil();
		if (rpls.isEmpty())
			return rpls;
		RPL[] rplArray = new RPL[rpls.size()];
		rpls.toArray(rplArray);
		for (int i = 0; i < rplArray.length; i++) {
			rplArray[i] = canonicalForm(rplArray[i]);
		}
		return List.from(rplArray);
	}

}
