/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.visitors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.google.inject.Inject;
import com.sun.tools.javac.code.Effect;
import com.sun.tools.javac.code.Effect.InvocationEffect;
import com.sun.tools.javac.code.Effect.ReadEffect;
import com.sun.tools.javac.code.Effect.WriteEffect;
import com.sun.tools.javac.code.Effects;
import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.dpjizer.FreshRPLElementFactory;
import com.sun.tools.javac.code.dpjizer.constraints.BeginWithConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.CompositeConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.ConjunctiveConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.Constraint;
import com.sun.tools.javac.code.dpjizer.constraints.ConstraintRepository;
import com.sun.tools.javac.code.dpjizer.constraints.Constraints;
import com.sun.tools.javac.code.dpjizer.constraints.ConstraintsSet;
import com.sun.tools.javac.code.dpjizer.constraints.DisjointnessConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.RPLElementDistinctnessConstraint;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.EnvScanner;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.DPJCobegin;
import com.sun.tools.javac.tree.JCTree.DPJForLoop;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;

import edu.illinois.dpjizer.utils.Logger;

/**
 * 
 * @author Mohsen Vakilian
 * @author Stephen Heumann
 * 
 */
public class ConstraintCollector extends EnvScanner {

	ConstraintRepository constraintRepository;
	TreeMaker treeMaker;

	protected final Name.Table names;

	@Inject
	public ConstraintCollector(Context context, ConstraintRepository constraintRepository) {
		super(context);
		this.constraintRepository = constraintRepository;
		treeMaker = TreeMaker.instance(context);
		names = Name.Table.instance(context);
	}

	public ConstraintRepository getConstraintRepository() {
		return constraintRepository;
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
		Logger.log("Effects of DPJ for loop:" + tree.effects);
		// Turn the loop index variable v into an RPL element [v].
		RPLElement indexVarRPLElement = getLoopIndexVarAsRPLElement(tree);
		Constraint constraintsToMakeWriteEffectsContainLoopIndexVariable = makeWriteEffectsContainRPLElement(tree.effects, indexVarRPLElement);
		Logger.log("Constraints to make the write effects contain the loop index variable are:\n" + constraintsToMakeWriteEffectsContainLoopIndexVariable.toString());
		constraintRepository.add(constraintsToMakeWriteEffectsContainLoopIndexVariable);
		Collection<WriteEffect> writeEffects = getWriteEffects(tree.effects);
		Collection<ReadEffect> readEffects = getReadEffects(tree.effects);
		Constraint constraintsToMakeReadEffectsDisjointFromWriteEffects = tryToMakeReadEffectsDisjointFromWriteEffects(readEffects, indexVarRPLElement, writeEffects);
		Logger.log("Constraints to make the read effects disjoint from the write effects are:\n" + constraintsToMakeReadEffectsDisjointFromWriteEffects.toString());
		//Since simpler constraints replace the following constraint, we don't add the big constraint to the repository for the time being.
		//constraintRepository.add(constraintsToMakeReadEffectsDisjointFromWriteEffects);
		boolean satisfiedDisjointnessConstraints = beginRPLsWithFreshRPLElements(constraintsToMakeReadEffectsDisjointFromWriteEffects);
		Logger.log((satisfiedDisjointnessConstraints ? "Succeeded" : "Failed") + " to satisfy disjointness constaints.");
		Collection<BeginWithConstraint> beginWithConstraints = constraintRepository.getBeginWithConstraints();
		for (BeginWithConstraint beginWithConstraint : beginWithConstraints) {
			constraintRepository.markAsReplacedConstraint(beginWithConstraint);
		}
		Constraint constraintsToSatisfyBeginWithConstraints = satisfyBeginWithConstraints(beginWithConstraints);
		Logger.log("Constraints to satisfy the begin-with constraints are:\n" + constraintsToSatisfyBeginWithConstraints.toString());
		constraintRepository.add(constraintsToSatisfyBeginWithConstraints);
	}

	private Constraint satisfyBeginWithConstraints(Collection<BeginWithConstraint> beginWithConstraints) {
		Constraints constraints = new ConstraintsSet();
		for (BeginWithConstraint constraint : beginWithConstraints) {
			Constraint shouldBeginWithRPLElement = constraint.getRPL().shouldBeginWithRPLElement(constraint.getBeginning());
			if (shouldBeginWithRPLElement instanceof CompositeConstraint && ((CompositeConstraint) shouldBeginWithRPLElement).isAlwaysFalse()) {
				Logger.log("Failed to satisfy the begin-with constraint: " + constraint);
			} else {
				constraints.add(shouldBeginWithRPLElement);
			}
		}
		return ConjunctiveConstraint.newConjunctiveConstraint(constraints);
	}

	private boolean beginRPLsWithFreshRPLElements(Constraint disjointnessConstraints) {
		if (disjointnessConstraints instanceof ConjunctiveConstraint) {
			Iterator<Constraint> iterator = ((ConjunctiveConstraint) disjointnessConstraints).iterator();
			boolean satisfiedDisjointnessConstraints = true;
			while (iterator.hasNext()) {
				satisfiedDisjointnessConstraints = satisfiedDisjointnessConstraints & beginRPLsWithFreshRPLElements(iterator.next());
			}
			return satisfiedDisjointnessConstraints;
		} else if (disjointnessConstraints instanceof DisjointnessConstraint) {
			DisjointnessConstraint disjointnessConstraint = (DisjointnessConstraint) disjointnessConstraints;

			RPLElement firstRPLElement = null;
			if (constraintRepository.doesBeginWithAnything(disjointnessConstraint.getFirstRPL())) {
				firstRPLElement = constraintRepository.getBeginning(disjointnessConstraint.getFirstRPL());
			} else {
				firstRPLElement = getFreshBeginningRPLElement();
				BeginWithConstraint beginWithConstraint = new BeginWithConstraint(disjointnessConstraint.getFirstRPL(), firstRPLElement);
				Logger.log("Added the following constraint to solve a disjointness constraint:\n" + beginWithConstraint);
				constraintRepository.add(beginWithConstraint);
			}

			RPLElement secondRPLElement = null;
			if (constraintRepository.doesBeginWithAnything(disjointnessConstraint.getSecondRPL())) {
				secondRPLElement = constraintRepository.getBeginning(disjointnessConstraint.getSecondRPL());
			} else {
				secondRPLElement = getFreshBeginningRPLElement();
				BeginWithConstraint beginWithConstraint = new BeginWithConstraint(disjointnessConstraint.getSecondRPL(), secondRPLElement);
				Logger.log("Added the following constraint to solve a disjointness constraint:\n" + beginWithConstraint);
				constraintRepository.add(beginWithConstraint);
			}

			if (firstRPLElement != null && secondRPLElement != null) {
				RPLElementDistinctnessConstraint distinctnessConstraint = new RPLElementDistinctnessConstraint(firstRPLElement, secondRPLElement);
				Logger.log("Added the following constraint to solve a disjointness constraint:\n" + distinctnessConstraint);
				constraintRepository.add(distinctnessConstraint);
			}

			if (constraintRepository.getBeginning(disjointnessConstraint.getFirstRPL()).equals(constraintRepository.getBeginning(disjointnessConstraint.getSecondRPL()))) {
				System.err.println("Failed to satisfy the disjointness constraint: " + disjointnessConstraint);
				return false;
			}
		}
		return true;
	}

	private RPLElement getFreshBeginningRPLElement() {
		Env<AttrContext> currentEnv = parentEnv;
		while (currentEnv.outer != null) {
			currentEnv = currentEnv.outer;
		}
		return FreshRPLElementFactory.getFreshNameRPLElement(names, currentEnv);
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

	private Collection<WriteEffect> getWriteEffects(Effects effects) {
		Collection<WriteEffect> result = new HashSet<WriteEffect>();
		for (Effect effect : effects) {
			if (effect instanceof WriteEffect) {
				result.add((WriteEffect) effect);
			} else if (effect instanceof InvocationEffect) {
				result.addAll(getWriteEffects(((InvocationEffect) effect).withEffects));
			}
		}
		return result;
	}

	private Collection<ReadEffect> getReadEffects(Effects effects) {
		Collection<ReadEffect> result = new HashSet<ReadEffect>();
		for (Effect effect : effects) {
			if (effect instanceof ReadEffect) {
				result.add((ReadEffect) effect);
			} else if (effect instanceof InvocationEffect) {
				result.addAll(getReadEffects(((InvocationEffect) effect).withEffects));
			}
		}
		return result;
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

	private Constraint generateReadWriteDisjointnessConstraint(ReadEffect readEffect, Collection<WriteEffect> writeEffects) {
		Constraints constraints = new ConstraintsSet();
		for (WriteEffect writeEffect : writeEffects) {
			constraints.add(new DisjointnessConstraint(readEffect.rpl, writeEffect.rpl));
		}
		return ConjunctiveConstraint.newConjunctiveConstraint(constraints);
	}

	private Constraint tryToMakeReadEffectsDisjointFromWriteEffects(Collection<ReadEffect> readEffects, RPLElement rplElement, Collection<WriteEffect> writeEffects) {
		Constraints result = new ConstraintsSet();
		for (ReadEffect readEffect : readEffects) {
			Constraint readEffectConstraint = readEffect.rpl.shouldContainRPLElement(rplElement);
			if (readEffectConstraint instanceof CompositeConstraint && ((CompositeConstraint) readEffectConstraint).isAlwaysFalse()) {
				// If it the read effect does not accept the loop index
				// variable, we have to generate the constraint to make the read
				// effect disjoint from other write effects.
				result.add(generateReadWriteDisjointnessConstraint(readEffect, writeEffects));
			} else {
				result.add(readEffectConstraint);
			}
		}
		return ConjunctiveConstraint.newConjunctiveConstraint(result);
	}

}
