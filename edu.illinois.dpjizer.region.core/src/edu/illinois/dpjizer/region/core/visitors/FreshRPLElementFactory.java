/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.visitors;

import static com.sun.tools.javac.code.Flags.STATIC;

import com.sun.tools.javac.code.RPLElement.NameRPLElement;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol.RegionNameSymbol;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;

/**
 * 
 * @author Mohsen Vakilian
 * @author Stephen Heumann
 * 
 */
public class FreshRPLElementFactory {

	static int id = 0;

	public static NameRPLElement getFreshNameRPLElement(Table names, Env<AttrContext> env) {
		++id;
		Scope originalScope = env.info.scope;
		Scope clonedScope = originalScope.clone();
		Env<AttrContext> localEnv = env.dup(env.tree, env.info.dup(clonedScope));
		RegionNameSymbol regionNameSym = new RegionNameSymbol(STATIC, Name.fromString(names, new String("fr" + id)), localEnv.info.scope.owner, false);

		return new NameRPLElement(regionNameSym);
	}

}
