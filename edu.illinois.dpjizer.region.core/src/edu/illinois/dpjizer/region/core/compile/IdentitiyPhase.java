/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.compile;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class IdentitiyPhase extends Phase {

	@Override
	public List<Env<AttrContext>> analyze(List<Env<AttrContext>> envs, Context context) {
		return envs;
	}

}
