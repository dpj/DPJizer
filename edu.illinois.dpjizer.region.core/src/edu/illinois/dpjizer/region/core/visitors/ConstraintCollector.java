/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.visitors;

import com.google.inject.Inject;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.comp.EnvScanner;
import com.sun.tools.javac.tree.JCTree.DPJCobegin;
import com.sun.tools.javac.tree.JCTree.DPJForLoop;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.region.core.constraints.ConstraintRepository;
import edu.illinois.dpjizer.region.core.constraints.SubtypingConstraint;
import edu.illinois.dpjizer.utils.Logger;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintCollector extends EnvScanner {

	ConstraintRepository constraintRepository;

	@Inject
	public ConstraintCollector(Context context, ConstraintRepository constraintRepository) {
		super(context);
		this.constraintRepository = constraintRepository;
	}

	public ConstraintRepository getConstraintRepository() {
		return constraintRepository;
	}

//	@Override
//	public void visitApply(JCMethodInvocation tree) {
//		super.visitApply(tree);
//		List<VarSymbol> formalParameters = tree.getMethodSymbol().getParameters();
//		List<JCExpression> actualArguments = tree.getArguments();
//		List<JCExpression> remainingActualArguments = actualArguments;
//		List<VarSymbol> remainingFormalParameters = formalParameters;
//
//		while (remainingFormalParameters.nonEmpty()) {
//			constraintRepository.add(new SubtypingConstraint(remainingFormalParameters.head.type, remainingActualArguments.head.type));
//			remainingActualArguments = remainingActualArguments.tail;
//			remainingFormalParameters = remainingFormalParameters.tail;
//		}
//	}

	@Override
	public void visitAssign(JCAssign tree) {
		super.visitAssign(tree);
		constraintRepository.add(new SubtypingConstraint(tree.lhs.type, tree.rhs.type));
	}

	@Override
	public void visitCobegin(DPJCobegin tree) {
		super.visitCobegin(tree);
		// TODO: Generate the disjointness constraints.
		Logger.log("Effects of the body of Cobegin:" + tree.effects);
	}

	@Override
	public void visitDPJForLoop(DPJForLoop tree) {
		super.visitDPJForLoop(tree);
		// TODO: Generate the disjointness constraints.
		Logger.log("Effects of DPJ for loop:" + tree.effects);
	}

	@Override
	public void visitReturn(JCReturn tree) {
		super.visitReturn(tree);
		constraintRepository.add(new SubtypingConstraint(parentEnv.enclMethod.restype.type, tree.expr.type));
	}

}
