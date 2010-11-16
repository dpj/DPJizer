/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.visitors;

import com.google.inject.Inject;
import com.sun.tools.javac.comp.EnvScanner;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.util.Context;

import edu.illinois.dpjizer.region.core.constraints.ConstraintRepository;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class EffectComputer extends EnvScanner {

	protected static final Context.Key<EffectComputer> effectComputerKey = new Context.Key<EffectComputer>();

	// ConstraintRepository constraintRepository =
	// ConstraintRepository.instance();
	@Inject
	ConstraintRepository constraintRepository;

	public static EffectComputer instance(Context context) {
		EffectComputer instance = context.get(effectComputerKey);
		if (instance == null)
			instance = new EffectComputer(context);
		return instance;
	}

	protected EffectComputer(Context context) {
		super(context);
		context.put(effectComputerKey, this);
	}

	// Constraints effects = new Constraints();

	@Override
	public void visitAssign(JCAssign tree) {
		super.visitAssign(tree);
	}

}
