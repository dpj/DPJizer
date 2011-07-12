/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.visitors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;
import com.sun.tools.javac.code.Effect;
import com.sun.tools.javac.code.Effect.InvocationEffect;
import com.sun.tools.javac.code.Effect.ReadEffect;
import com.sun.tools.javac.code.Effect.WriteEffect;
import com.sun.tools.javac.code.Effects;
import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.RPLElement.ArrayIndexRPLElement;
import com.sun.tools.javac.code.RPLElement.RPLParameterElement;
import com.sun.tools.javac.code.dpjizer.FreshRPLElementFactory;
import com.sun.tools.javac.code.dpjizer.constraints.BeginWithConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.CompositeConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.ConjunctiveConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.Constraint;
import com.sun.tools.javac.code.dpjizer.constraints.ConstraintRepository;
import com.sun.tools.javac.code.dpjizer.constraints.Constraints;
import com.sun.tools.javac.code.dpjizer.constraints.ConstraintsSet;
import com.sun.tools.javac.code.dpjizer.constraints.DisjointnessConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.DisjunctiveConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.RPLElementDistinctnessConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.RPLEqualityConstraint;
import com.sun.tools.javac.code.dpjizer.constraints.RegionVarElt;
import com.sun.tools.javac.code.dpjizer.substitutions.IndexSubstitution;
import com.sun.tools.javac.code.dpjizer.substitutions.RegionSubstitution;
import com.sun.tools.javac.code.dpjizer.substitutions.Substitution;
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
		RPLElement indexVarRPLElement = getLoopIndexVarAsRPLElement(tree);
		Constraint constraintsToMakeWriteEffectsContainLoopIndexVariable = makeWriteEffectsContainRPLElement(tree.effects,
				new RPL(indexVarRPLElement));
		Logger.log("Constraints to make the write effects contain the loop index variable are:\n"
				+ constraintsToMakeWriteEffectsContainLoopIndexVariable.toString());
		constraintRepository.add(constraintsToMakeWriteEffectsContainLoopIndexVariable);
		Collection<WriteEffect> writeEffects = getWriteEffects(tree.effects);
		Collection<ReadEffect> readEffects = getReadEffects(tree.effects);
		Constraint pairwiseEffectDisjointnessConstraints = makeEffectsPairwiseDisjoint(readEffects, writeEffects);
		Logger.log("Constraints to make the read effects disjoint from the write effects are:\n" + pairwiseEffectDisjointnessConstraints);
		// Constraint constraintsToMakeReadEffectsDisjointFromWriteEffects =
		// tryToMakeReadEffectsDisjointFromWriteEffects(readEffects, new RPL(
		// indexVarRPLElement), writeEffects);
		// Logger.log("Constraints to make the read effects disjoint from the write effects are:\n"
		// + constraintsToMakeReadEffectsDisjointFromWriteEffects.toString());
		// // Since simpler constraints replace the following constraint, we
		// don't
		// // add the big constraint to the repository for the time being.
		// //
		// constraintRepository.add(constraintsToMakeReadEffectsDisjointFromWriteEffects);
		// boolean satisfiedDisjointnessConstraints =
		// beginRPLsWithFreshRPLs(constraintsToMakeReadEffectsDisjointFromWriteEffects);
		// Logger.log((satisfiedDisjointnessConstraints ? "Succeeded" :
		// "Failed") + " to satisfy disjointness constaints.");
		// Collection<BeginWithConstraint> beginWithConstraints =
		// constraintRepository.getBeginWithConstraints();
		// for (BeginWithConstraint beginWithConstraint : beginWithConstraints)
		// {
		// constraintRepository.markAsReplacedConstraint(beginWithConstraint);
		// }
		// Constraint constraintsToSatisfyBeginWithConstraints =
		// satisfyBeginWithConstraints(beginWithConstraints);
		// Logger.log("Constraints to satisfy the begin-with constraints are:\n"
		// + constraintsToSatisfyBeginWithConstraints.toString());
		// constraintRepository.add(constraintsToSatisfyBeginWithConstraints);
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

	private boolean beginRPLsWithFreshRPLs(Constraint disjointnessConstraints) {
		if (disjointnessConstraints instanceof ConjunctiveConstraint) {
			Iterator<Constraint> iterator = ((ConjunctiveConstraint) disjointnessConstraints).iterator();
			boolean satisfiedDisjointnessConstraints = true;
			while (iterator.hasNext()) {
				satisfiedDisjointnessConstraints = satisfiedDisjointnessConstraints & beginRPLsWithFreshRPLs(iterator.next());
			}
			return satisfiedDisjointnessConstraints;
		} else if (disjointnessConstraints instanceof DisjointnessConstraint) {
			DisjointnessConstraint disjointnessConstraint = (DisjointnessConstraint) disjointnessConstraints;

			RPL firstRPL = null;
			if (constraintRepository.doesBeginWithAnything(disjointnessConstraint.getFirstRPL())) {
				firstRPL = constraintRepository.getBeginning(disjointnessConstraint.getFirstRPL());
			} else {
				firstRPL = new RPL(getFreshBeginningRPLElement());
				BeginWithConstraint beginWithConstraint = new BeginWithConstraint(disjointnessConstraint.getFirstRPL(), firstRPL);
				Logger.log("Added the following constraint to solve a disjointness constraint:\n" + beginWithConstraint);
				constraintRepository.add(beginWithConstraint);
			}

			RPL secondRPL = null;
			if (constraintRepository.doesBeginWithAnything(disjointnessConstraint.getSecondRPL())) {
				secondRPL = constraintRepository.getBeginning(disjointnessConstraint.getSecondRPL());
			} else {
				secondRPL = new RPL(getFreshBeginningRPLElement());
				BeginWithConstraint beginWithConstraint = new BeginWithConstraint(disjointnessConstraint.getSecondRPL(), secondRPL);
				Logger.log("Added the following constraint to solve a disjointness constraint:\n" + beginWithConstraint);
				constraintRepository.add(beginWithConstraint);
			}

			if (firstRPL != null && secondRPL != null) {
				// FIXME: We are making two RPLs begin with different RPLs but
				// require their first RPL elements to be distinct. We might
				// have to replace the RPL element distinctness constraint by
				// RPL disjointness constraint.
				RPLElementDistinctnessConstraint distinctnessConstraint = new RPLElementDistinctnessConstraint(firstRPL.elts.head,
						secondRPL.elts.head);
				Logger.log("Added the following constraint to solve a disjointness constraint:\n" + distinctnessConstraint);
				constraintRepository.add(distinctnessConstraint);
			}

			if (constraintRepository.getBeginning(disjointnessConstraint.getFirstRPL()).equals(
					constraintRepository.getBeginning(disjointnessConstraint.getSecondRPL()))) {
				System.err.println("Failed to satisfy the disjointness constraint: " + disjointnessConstraint);
				return false;
			}
		}
		return true;
	}

	private RPLElement getFreshBeginningRPLElement() {
		Env<AttrContext> currentEnv = getGlobalEnv();
		return FreshRPLElementFactory.getFreshNameRPLElement(names, currentEnv);
	}

	public Env<AttrContext> getGlobalEnv() {
		Env<AttrContext> currentEnv = parentEnv;
		while (currentEnv.outer != null) {
			currentEnv = currentEnv.outer;
		}
		return currentEnv;
	}

	// Turn the loop index variable v into an RPL element [v].
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

	private Constraint makeWriteEffectsContainRPLElement(Effects effects, RPL rpl) {
		Constraints result = new ConstraintsSet();
		for (Effect effect : effects) {
			if (effect instanceof WriteEffect) {
				result.add(((WriteEffect) effect).rpl.shouldContainRPLElement(rpl));
			} else if (effect instanceof InvocationEffect) {
				result.add(makeWriteEffectsContainRPLElement(((InvocationEffect) effect).withEffects, rpl));
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

	private Constraint tryToMakeReadEffectsDisjointFromWriteEffects(Collection<ReadEffect> readEffects, RPL rpl, Collection<WriteEffect> writeEffects) {
		Constraints result = new ConstraintsSet();
		for (ReadEffect readEffect : readEffects) {
			Constraint readEffectConstraint = readEffect.rpl.shouldContainRPLElement(rpl);
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

	Collection<RPL> getRPLs(Collection<? extends Effect> effects) {
		Collection<RPL> rpls = new ArrayList<RPL>();
		for (Effect effect : effects) {
			if (effect instanceof ReadEffect) {
				rpls.add(((ReadEffect) effect).rpl);
			} else if (effect instanceof WriteEffect) {
				rpls.add(((WriteEffect) effect).rpl);
			} else {
				throw new AssertionError("Expected a read/write effect but got: " + effect);
			}
		}
		return rpls;
	}

	private Constraint makeEffectsPairwiseDisjoint(Collection<? extends Effect> first, Collection<? extends Effect> second) {
		Constraints constraints = new ConstraintsSet();
		for (RPL anRPLOfFirst : getRPLs(first)) {
			for (RPL anRPLOfSecond : getRPLs(second)) {
				constraints.add(generateConstraintsToMakeRPLsDisjoint(anRPLOfFirst, anRPLOfSecond));
			}
		}
		return ConjunctiveConstraint.newConjunctiveConstraint(constraints);
	}

	private Constraint generateConstraintsToMakeRPLsDisjoint(RPL rpl1, RPL rpl2) {
		if (rpl1.hasSubstitutionChain()) {
			Constraints disjunctiveConstraints = new ConstraintsSet();
			disjunctiveConstraints.add(generateConstraintsToMakeRPLsDisjoint(rpl1.withoutLastSubstitutions(), rpl2));
			List<Substitution> lastSubstitutions = rpl1.getLastSubstitutions().getSubstitutions();
			if (lastSubstitutions.size() > 1) {
				throw new AssertionError("Expected a substitution of length 1.");
			}
			Substitution lastSubstitution = lastSubstitutions.get(0);
			if (lastSubstitution instanceof RegionSubstitution) {
				// rpl1 # rpl2 <=>
				// (detailedRPL1 # rpl2) v
				// (regionVarEltForRHS : rpl1Tail # rpl2)
				// where
				// rpl1 = detailedRPL1 [parameterRPLElement <- rhs],
				// regionVarEltForRHS = rhs,
				// detailedRPL1 = parameterRPLElement : rpl1Tail

				RegionSubstitution regionSubstitution = (RegionSubstitution) lastSubstitution;
				RPLParameterElement parameterRPLElement = new RPLElement.RPLParameterElement(regionSubstitution.getLHS());
				RegionVarElt rpl1Tail = RegionVarElt.getFreshRegionVarElt(names, getGlobalEnv());
				RPL detailedRPL1 = new RPL(com.sun.tools.javac.util.List.<RPLElement> of(parameterRPLElement, rpl1Tail));

				RPL rhs = regionSubstitution.getRHS();
				RegionVarElt regionVarEltForRHS = RegionVarElt.getFreshRegionVarElt(names, getGlobalEnv());

				Constraints conjuctiveConstraints = new ConstraintsSet();
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsEqual(rpl1.withoutLastSubstitutions(), detailedRPL1));
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsEqual(new RPL(regionVarEltForRHS), rhs));
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsDisjoint(rpl2,
						new RPL(com.sun.tools.javac.util.List.<RPLElement> of(regionVarEltForRHS, rpl1Tail))));
				disjunctiveConstraints.add(ConjunctiveConstraint.newConjunctiveConstraint(conjuctiveConstraints));
			} else if (lastSubstitution instanceof IndexSubstitution) {
				// rpl1 # rpl2 <=>
				// (detailedRPL1 # rpl2) v
				// (rpl1Head : rhsRPLElement : rpl1Tail # rpl2)
				// where
				// rpl1 = detailedRPL1 [arrayIndexRPLElement <- rhsRPLElement],
				// detailedRPL1 = rpl1Head : arrayIndexRPLElement : rpl1Tail

				IndexSubstitution indexSubstitution = (IndexSubstitution) lastSubstitution;
				ArrayIndexRPLElement arrayIndexRPLElement = new RPLElement.ArrayIndexRPLElement(treeMaker.Ident(indexSubstitution.getLHS()));
				RegionVarElt rpl1Head = RegionVarElt.getFreshRegionVarElt(names, getGlobalEnv());
				RegionVarElt rpl1Tail = RegionVarElt.getFreshRegionVarElt(names, getGlobalEnv());
				RPL detailedRPL1 = new RPL(com.sun.tools.javac.util.List.<RPLElement> of(rpl1Head, arrayIndexRPLElement, rpl1Tail));

				RPLElement rhsRPLElement = new RPLElement.ArrayIndexRPLElement(indexSubstitution.getRHS());

				Constraints conjuctiveConstraints = new ConstraintsSet();
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsEqual(rpl1.withoutLastSubstitutions(), detailedRPL1));
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsDisjoint(rpl2,
						new RPL(com.sun.tools.javac.util.List.<RPLElement> of(rpl1Head, rhsRPLElement, rpl1Tail))));
				disjunctiveConstraints.add(ConjunctiveConstraint.newConjunctiveConstraint(conjuctiveConstraints));
			}
			return DisjunctiveConstraint.newDisjunctiveConstraint(disjunctiveConstraints);
		} else if (rpl2.hasSubstitutionChain()) {
			return generateConstraintsToMakeRPLsDisjoint(rpl2, rpl1);
		} else {
			return new DisjointnessConstraint(rpl1, rpl2);
		}
	}

	private Constraint generateConstraintsToMakeRPLsEqual(RPL rpl1, RPL rpl2) {
		if (rpl1.hasSubstitutionChain()) {
			Constraints disjunctiveConstraints = new ConstraintsSet();
			disjunctiveConstraints.add(generateConstraintsToMakeRPLsEqual(rpl1.withoutLastSubstitutions(), rpl2));
			List<Substitution> lastSubstitutions = rpl1.getLastSubstitutions().getSubstitutions();
			if (lastSubstitutions.size() > 1) {
				throw new AssertionError("Expected a substitution of length 1.");
			}
			Substitution lastSubstitution = lastSubstitutions.get(0);
			if (lastSubstitution instanceof RegionSubstitution) {
				// rpl1 = rpl2 <=>
				// (detailedRPL1 = rpl2) v
				// (regionVarEltForRHS : rpl1Tail = rpl2)
				// where
				// rpl1 = detailedRPL1 [parameterRPLElement <- rhs],
				// regionVarEltForRHS = rhs,
				// detailedRPL1 = parameterRPLElement : rpl1Tail

				RegionSubstitution regionSubstitution = (RegionSubstitution) lastSubstitution;
				RPLParameterElement parameterRPLElement = new RPLElement.RPLParameterElement(regionSubstitution.getLHS());
				RegionVarElt rpl1Tail = RegionVarElt.getFreshRegionVarElt(names, getGlobalEnv());
				RPL detailedRPL1 = new RPL(com.sun.tools.javac.util.List.<RPLElement> of(parameterRPLElement, rpl1Tail));

				RPL rhs = regionSubstitution.getRHS();
				RegionVarElt regionVarEltForRHS = RegionVarElt.getFreshRegionVarElt(names, getGlobalEnv());

				Constraints conjuctiveConstraints = new ConstraintsSet();
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsEqual(rpl1.withoutLastSubstitutions(), detailedRPL1));
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsEqual(new RPL(regionVarEltForRHS), rhs));
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsEqual(rpl2,
						new RPL(com.sun.tools.javac.util.List.<RPLElement> of(regionVarEltForRHS, rpl1Tail))));
				disjunctiveConstraints.add(ConjunctiveConstraint.newConjunctiveConstraint(conjuctiveConstraints));
			} else if (lastSubstitution instanceof IndexSubstitution) {
				// rpl1 = rpl2 <=>
				// (detailedRPL1 = rpl2) v
				// (rpl1Head : rhsRPLElement : rpl1Tail = rpl2)
				// where
				// rpl1 = detailedRPL1 [arrayIndexRPLElement <- rhsRPLElement],
				// detailedRPL1 = rpl1Head : arrayIndexRPLElement : rpl1Tail

				IndexSubstitution indexSubstitution = (IndexSubstitution) lastSubstitution;
				ArrayIndexRPLElement arrayIndexRPLElement = new RPLElement.ArrayIndexRPLElement(treeMaker.Ident(indexSubstitution.getLHS()));
				RegionVarElt rpl1Head = RegionVarElt.getFreshRegionVarElt(names, getGlobalEnv());
				RegionVarElt rpl1Tail = RegionVarElt.getFreshRegionVarElt(names, getGlobalEnv());
				RPL detailedRPL1 = new RPL(com.sun.tools.javac.util.List.<RPLElement> of(rpl1Head, arrayIndexRPLElement, rpl1Tail));

				RPLElement rhsRPLElement = new RPLElement.ArrayIndexRPLElement(indexSubstitution.getRHS());

				Constraints conjuctiveConstraints = new ConstraintsSet();
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsEqual(rpl1.withoutLastSubstitutions(), detailedRPL1));
				conjuctiveConstraints.add(generateConstraintsToMakeRPLsEqual(rpl2,
						new RPL(com.sun.tools.javac.util.List.<RPLElement> of(rpl1Head, rhsRPLElement, rpl1Tail))));
				disjunctiveConstraints.add(ConjunctiveConstraint.newConjunctiveConstraint(conjuctiveConstraints));
			}
			return DisjunctiveConstraint.newDisjunctiveConstraint(disjunctiveConstraints);
		} else if (rpl2.hasSubstitutionChain()) {
			return generateConstraintsToMakeRPLsEqual(rpl2, rpl1);
		} else {
			return new RPLEqualityConstraint(rpl1, rpl2);
		}
	}

}
