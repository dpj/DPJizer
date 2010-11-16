/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.compile;

import com.google.inject.Inject;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.region.core.transform.MethodParamInserter;
import edu.illinois.dpjizer.region.core.visitors.MethodParameterInserter;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
@Deprecated
public class MethodParameterInsertionPhase extends Phase {

	MethodParamInserter methodParamInserter;

	@Inject
	public MethodParameterInsertionPhase(MethodParamInserter methodParamInserter) {
		super();
		this.methodParamInserter = methodParamInserter;
	}

	@Override
	public List<Env<AttrContext>> analyze(List<Env<AttrContext>> envs, Context context) {
		// FIXME: Use injection.
		visit(envs, MethodParameterInserter.instance(context));
		prettyPrint(envs, methodParamInserter);
		return envs;
	}

}
