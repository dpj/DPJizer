/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.compile;

import java.util.ArrayList;
import java.util.Map;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.effect.core.constraint.ConstraintCollector;
import edu.illinois.dpjizer.effect.core.constraint.ConstraintRepository;
import edu.illinois.dpjizer.effect.core.constraint.Method;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
public class ConstraintSolverPhase {
	ArrayList<Map<Method, String>> solutions = new ArrayList<Map<Method, String>>();

	public void collectAndWriteConstraints(Context context) {
		getCollectedConstraints(context).collectAndWriteConstraints();
	}

	public List<Env<AttrContext>> collectAndWriteConstraints(List<Env<AttrContext>> envs, Context context) {
		scanByConstraintCollector(envs, context);
		collectAndWriteConstraints(context);
		return envs;
	}

	public void solveConstraints(Context context) {
		solutions.add(getCollectedConstraints(context).collectSolveAndWriteConstraints());
	}

	private ConstraintRepository getCollectedConstraints(Context context) {
		return (ConstraintCollector.instance(context)).getConstraints();
	}

	private void scanByConstraintCollector(List<Env<AttrContext>> envs, Context context) {
		for (Env<AttrContext> env : envs)
			ConstraintCollector.instance(context).scan(env.tree);
	}

	public List<Env<AttrContext>> collectSolveAndWriteConstraints(List<Env<AttrContext>> envs, Context context) {
		scanByConstraintCollector(envs, context);
		solveConstraints(context);
		return envs;
	}

	public ArrayList<Map<Method, String>> getSolutions() {
		return solutions;
	}
}
