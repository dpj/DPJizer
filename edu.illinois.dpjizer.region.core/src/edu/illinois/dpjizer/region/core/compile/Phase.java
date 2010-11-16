/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.compile;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.EnvScanner;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.region.core.change.Changes;
import edu.illinois.dpjizer.region.core.transform.Transformer;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public abstract class Phase {

	Changes changes;

	public abstract List<Env<AttrContext>> analyze(List<Env<AttrContext>> envs, Context context);

	public Changes getChanges() {
		return changes;
	}

	protected List<Env<AttrContext>> visit(List<Env<AttrContext>> envs, EnvScanner visitor) {
		for (Env<AttrContext> env : envs) {
			visitor.scan(env.tree);
		}
		return envs;
	}

	protected void prettyPrint(List<Env<AttrContext>> envs, Transformer transformer) {
		for (Env<AttrContext> env : envs) {
			transformer.prettyPrint(env.toplevel);
		}
	}
}
