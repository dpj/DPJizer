/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.visitors;

import com.google.inject.Inject;
import com.sun.tools.javac.code.Effect;
import com.sun.tools.javac.code.Effect.InvocationEffect;
import com.sun.tools.javac.code.Effect.ReadEffect;
import com.sun.tools.javac.code.Effect.WriteEffect;
import com.sun.tools.javac.code.Effects;
import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.dpjizer.constraints.CompositeConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.ConjunctiveConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.Constraint;
import com.sun.tools.javac.code.dpjizer.constraints.ConstraintRepository;
import com.sun.tools.javac.code.dpjizer.constraints.Constraints;
import com.sun.tools.javac.code.dpjizer.constraints.ConstraintsSet;
import com.sun.tools.javac.comp.EnvScanner;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.DPJCobegin;
import com.sun.tools.javac.tree.JCTree.DPJForLoop;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

import edu.illinois.dpjizer.utils.Logger;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintCollector extends EnvScanner {

	ConstraintRepository constraintRepository;
	TreeMaker treeMaker;

	@Inject
	public ConstraintCollector(Context context, ConstraintRepository constraintRepository) {
		super(context);
		this.constraintRepository = constraintRepository;
		treeMaker = TreeMaker.instance(context);
	}

	public ConstraintRepository getConstraintRepository() {
		return constraintRepository;
	}

	// @Override
	// public void visitApply(JCMethodInvocation tree) {
	// super.visitApply(tree);
	// List<VarSymbol> formalParameters =
	// tree.getMethodSymbol().getParameters();
	// List<JCExpression> actualArguments = tree.getArguments();
	// List<JCExpression> remainingActualArguments = actualArguments;
	// List<VarSymbol> remainingFormalParameters = formalParameters;
	//
	// while (remainingFormalParameters.nonEmpty()) {
	// constraintRepository.add(new
	// SubtypingConstraint(remainingFormalParameters.head.type,
	// remainingActualArguments.head.type));
	// remainingActualArguments = remainingActualArguments.tail;
	// remainingFormalParameters = remainingFormalParameters.tail;
	// }
	// }

	// @Override
	// public void visitAssign(JCAssign tree) {
	// super.visitAssign(tree);
	// constraintRepository.add(new SubtypingConstraint(tree.lhs.type,
	// tree.rhs.type));
	// }

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
		// Turn the loop index variable v into an RPL element [v].
		RPLElement rplElement = getLoopIndexVarAsRPLElement(tree);
		Constraint constraintsToMakeWriteEffectsContainLoopIndexVariable = makeWriteEffectsContainRPLElement(tree.effects, rplElement);
		Logger.log("Constraints to make the write effects contain the loop index variable are:\n"
				+ constraintsToMakeWriteEffectsContainLoopIndexVariable.toString());
		Constraint constraintsToMakeReadEffectsContainLoopIndexVariable = tryToMakeReadEffectsContainRPLElement(tree.effects, rplElement);
		Logger.log("Constraints to make the read effects contain the loop index variable are:\n"
				+ constraintsToMakeReadEffectsContainLoopIndexVariable.toString());
	}

	private RPLElement getLoopIndexVarAsRPLElement(DPJForLoop tree) {
		JCVariableDecl loopVariable = tree.var;
		JCTree.JCIdent indexIdentifier = treeMaker.Ident(loopVariable.name);
		indexIdentifier.sym = loopVariable.sym;
		indexIdentifier.setPos(loopVariable.sym.pos);
		indexIdentifier.setType(loopVariable.sym.type);
		RPLElement rplElement = new RPLElement.ArrayIndexRPLElement(indexIdentifier);
		return rplElement;
	}

	private Constraint makeWriteEffectsContainRPLElement(Effects effects, RPLElement rplElement) {
		Constraints result = new ConstraintsSet();
		for (Effect effect : effects) {
			if (effect instanceof WriteEffect) {
				result.add(((WriteEffect) effect).rpl.shouldContainRPLElement(rplElement));
			} else if (effect instanceof InvocationEffect) {
				result.add(makeWriteEffectsContainRPLElement(((InvocationEffect) effect).withEffects, rplElement));
			}
		}
		return ConjunctiveConstraint.newConjunctiveConstraint(result);
	}

	private Constraint tryToMakeReadEffectsContainRPLElement(Effects effects, RPLElement rplElement) {
		Constraints result = new ConstraintsSet();
		for (Effect effect : effects) {
			if (effect instanceof ReadEffect) {
				Constraint readEffectConstraint = ((ReadEffect) effect).rpl.shouldContainRPLElement(rplElement);
				if (readEffectConstraint instanceof CompositeConstraint && ((CompositeConstraint) readEffectConstraint).isAlwaysFalse()) {
					//If it the read effect does not accept the loop index variable, we have to move under a region dedicated to reads.
				} else {
					result.add(readEffectConstraint);
				}
			} else if (effect instanceof InvocationEffect) {
				Constraint invocationEffectConstraint = tryToMakeReadEffectsContainRPLElement(((InvocationEffect) effect).withEffects, rplElement);
				if (invocationEffectConstraint instanceof CompositeConstraint && ((CompositeConstraint) invocationEffectConstraint).isAlwaysFalse()) {
					//If it the read effect does not accept the loop index variable, we have to move under a region dedicated to reads.
				} else {
					result.add(invocationEffectConstraint);
				}
			}
		}
		return ConjunctiveConstraint.newConjunctiveConstraint(result);
	}

	// @Override
	// public void visitReturn(JCReturn tree) {
	// super.visitReturn(tree);
	// constraintRepository.add(new SubtypingConstraint(
	// parentEnv.enclMethod.restype.type, tree.expr.type));
	// }

}
