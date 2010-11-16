/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.visitors;

import com.google.inject.Inject;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.comp.EnvScanner;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.region.core.constraints.ConstraintRepository;
import edu.illinois.dpjizer.region.core.constraints.SubtypingConstraint;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintCollector extends EnvScanner {

	// protected static final Context.Key<ConstraintCollector>
	// constraintCollectorKey = new Context.Key<ConstraintCollector>();

	// ConstraintRepository constraintRepository =
	// ConstraintRepository.instance();

	ConstraintRepository constraintRepository;

	//
	// public static ConstraintCollector instance(Context context,
	// ConstraintRepository constraintRepository) {
	// ConstraintCollector instance = context.get(constraintCollectorKey);
	// if (instance == null)
	// instance = new ConstraintCollector(context, constraintRepository);
	// return instance;
	// }

	@Inject
	public ConstraintCollector(Context context, ConstraintRepository constraintRepository) {
		super(context);
		this.constraintRepository = constraintRepository;
		// context.put(constraintCollectorKey, this);
	}

	public ConstraintRepository getConstraintRepository() {
		return constraintRepository;
	}

	@Override
	public void visitAssign(JCAssign tree) {
		super.visitAssign(tree);
		constraintRepository.add(new SubtypingConstraint(tree.lhs.type, tree.rhs.type));
	}

	@Override
	public void visitReturn(JCReturn tree) {
		super.visitReturn(tree);
		constraintRepository.add(new SubtypingConstraint(parentEnv.enclMethod.restype.type, tree.type));
	}

	@Override
	public void visitApply(JCMethodInvocation tree) {
		super.visitApply(tree);
		List<VarSymbol> formalParameters = tree.getMethodSymbol().getParameters();
		List<JCExpression> actualArguments = tree.getArguments();
		List<JCExpression> remainingActualArguments = actualArguments;
		for (List<VarSymbol> remainingFormalParameters = formalParameters; remainingFormalParameters.nonEmpty(); remainingFormalParameters = remainingFormalParameters.tail) {
			constraintRepository.add(new SubtypingConstraint(remainingFormalParameters.head.type, remainingActualArguments.head.type));

		}
	}
}
