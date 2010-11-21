/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.effect.core.rpl.RPLUtils;
import edu.illinois.dpjizer.utils.Logger;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 * 
 */
public class ConstraintsSolver {

	ConstraintRepository constraintsRepository;

	public ConstraintsSolver(ConstraintRepository constraintsRepository) {
		super();
		this.constraintsRepository = constraintsRepository;
	}

	void solveConstraints() {
		boolean changed;
		int iterationNum = 0;
		boolean watchChanges = true;
		boolean watchProgress = false;

		do {
			Logger.log("Iteration Number = " + iterationNum);
			Logger.log(constraintsRepository.toString());
			if (watchProgress) {
				Logger.log("begin summarizeEffects()");
			}
			constraintsRepository.summarizeAllInvocationConstraints();
			Logger.log("Constraint repository after summarization is");
			Logger.log(constraintsRepository.toString());
			// summarizeEffects();
			if (watchProgress) {
				Logger.log("end summarizeEffects()");
			}
			changed = false;
			Constraints newConstraints = new Constraints();

			if (watchProgress) {
				Logger.log("Number of methods=" + constraintsRepository.allMethodNames().length);
			}
			for (String callerMethod : constraintsRepository.allMethodNames()) {
				if (watchProgress) {
					Logger.log(callerMethod);
				}

				Constraints callerMethodInvocations = constraintsRepository.methodInvocations.get(callerMethod);
				Constraints newCallerInvokeConstraints = new Constraints();

				if (watchProgress) {
					Logger.log("Number of invokes=" + callerMethodInvocations.size());
				}
				for (Constraint constraint : callerMethodInvocations) {
					InvokeConstraint invokeConstraint = (InvokeConstraint) constraint;
					Constraints callerEffects = constraintsRepository.effectsOf(callerMethod);
					Constraints calleeEffects = constraintsRepository.effectsOf(invokeConstraint.invokedMethod);
					Constraints calleeInvokeConstraints = constraintsRepository.methodInvocationsOf(invokeConstraint.invokedMethod);

					// if (!invokeConstraint.isExpandingRecursive())
					{
						ChangeWatcher changeWatcher = new ChangeWatcher();
						changeWatcher.takeSnapshot1(callerEffects);
						addCalleeConstraints(callerMethod, invokeConstraint, callerEffects, calleeEffects);
						changeWatcher.takeSnapshot2(callerEffects);
						changed = changed || changeWatcher.gotNewConstraints();
						newConstraints.addAll(changeWatcher.newConstraints());

						// if (invokeConstraint.isRecursive())
						addCalleeConstraints(callerMethod, invokeConstraint, newCallerInvokeConstraints, calleeInvokeConstraints);
						// else
						// addCalleeConstraints(callerMethod, invokeConstraint,
						// newCallerInvokeConstraints, calleeInvokeConstraints
						// .nonRecursiveInvokes());
						// edu.uiuc.edu.dpj.TestConstraintSolver18.testFour()
						// won't terminate if we included recursive invokes
						// addCalleeConstraints(callerMethod, invokeConstraint,
						// newCallerInvokeConstraints, calleeInvokeConstraints);
					}
				}

				constraintsRepository.summarizeAllInvocationConstraints();
				ChangeWatcher changeWatcher = new ChangeWatcher();
				changeWatcher.takeSnapshot1(callerMethodInvocations);
				callerMethodInvocations.addAll(newCallerInvokeConstraints);
				constraintsRepository.summarizeAllInvocationConstraints();
				changeWatcher.takeSnapshot2(callerMethodInvocations);
				changed = changed || changeWatcher.gotNewConstraints();
				newConstraints.addAll(changeWatcher.newConstraints());

				// Process method overrides
				Constraints callerMethodOverrides = constraintsRepository.methodOverridesOf(callerMethod);

				if (watchProgress) {
					Logger.log("Number of overrides=" + callerMethodOverrides.size());
				}

				for (Constraint constraint : callerMethodOverrides) {
					OverrideConstraint overrideConstraint = (OverrideConstraint) constraint;
					Constraints overriddenEffects = constraintsRepository.effectsOf(overrideConstraint.overriddenMethod);
					Constraints overriderEffects = constraintsRepository.effectsOf(overrideConstraint.ownerMethod);

					changeWatcher = new ChangeWatcher();
					changeWatcher.takeSnapshot1(overriddenEffects);
					addOverrideConstraints(overrideConstraint, overriddenEffects, overriderEffects);
					changeWatcher.takeSnapshot2(overriddenEffects);
					changed = changed || changeWatcher.gotNewConstraints();
					newConstraints.addAll(changeWatcher.newConstraints());
				}
			}

			Logger.log("---------------------------------------------------------------");
			if (watchProgress) {
				iterationNum = (iterationNum + 1) % 1000;
			}
			if (watchChanges) {
				// if (iterationNum % 10 == 0) {
				Logger.log(newConstraints.toString());
				// }
			}

		} while (changed);
	}

	private boolean addOverrideConstraints(OverrideConstraint overrideConstraint, Constraints overridenConstraints, Constraints overriderConstraints) {
		Constraints constraintsToAdd;
		constraintsToAdd = overriderConstraints.ownersChangedTo(overrideConstraint.overriddenMethod);
		if (overrideConstraint.isClassParametric()) {
			List<RegionParameterSymbol> from = RPLUtils.toRegionParameterSymbols(overrideConstraint.childClassRegionParameters);
			List<RPL> to = RPLUtils.toRPLs(overrideConstraint.parentClassRegionParameters);
			constraintsToAdd = constraintsToAdd.substituteRegions(from, to);
		}
		if (overrideConstraint.isMethodParametric()) {
			List<RegionParameterSymbol> from = overrideConstraint.childMethodRegionParamaters;
			List<RPL> to = RPLUtils.toRPLs(overrideConstraint.parentMethodRegionParameters);
			constraintsToAdd = constraintsToAdd.substituteRegions(from, to);
		}
		constraintsToAdd = constraintsToAdd.inEnvironment(constraintsRepository.methodEnvs, overrideConstraint.overriddenMethod);
		return overridenConstraints.addAll(constraintsToAdd);
	}

	private boolean addCalleeConstraints(String callerMethod, InvokeConstraint invokeConstraint, Constraints callerConstraints,
			Constraints calleeConstraints) {

		// if (callerMethod.contains("partitionHelperRight"))
		// System.out.println("Add callee constraints to partitionHelperRight.");

		// substituting regions more than once causes trouble for substitutions
		// of the form X -> X : L
		Constraints constraintsToAdd;
		constraintsToAdd = calleeConstraints.ownersChangedTo(callerMethod);

		constraintsToAdd = constraintsToAdd.substituteForThis(invokeConstraint.actualThis);

		// TODO: Is it necessary to invoke inEnvironemnt here?
		constraintsToAdd = constraintsToAdd.inEnvironment(constraintsRepository.methodEnvs, callerMethod);

		if (invokeConstraint.isParametric()) {
			constraintsToAdd = constraintsToAdd.substituteRegions(invokeConstraint.regionParameters, invokeConstraint.regionActuals);
		}

		// If the region parameter substitution creates local effects that are
		// not valid in the scope of the method, we have to prune those effects
		// away
		constraintsToAdd = constraintsToAdd.inEnvironment(constraintsRepository.methodEnvs, callerMethod);

		Constraints invalidConstraints = constraintsToAdd.invalids();
		if (!invalidConstraints.isEmpty())
			throw new RuntimeException("Invalid Constraints:" + invalidConstraints);

		return callerConstraints.addAll(constraintsToAdd);
	}

	// private Set<Constraint> nonRecursiveInvokes(Set<Constraint> allInvokes) {
	// Set<Constraint> nonRecursive = new HashSet<Constraint>();
	// for (Constraint constraint : allInvokes) {
	// if (!((InvokeConstraint) constraint).isExpandingRecursive())
	// nonRecursive.add(constraint);
	// }
	// return nonRecursive;
	// }

	// private void summarizeEffects() {
	// for (String callerMethod : constraintsRepository.allMethodNames()) {
	// Constraints callerMethodInvocations =
	// constraintsRepository.methodInvocations.get(callerMethod);
	// for (Constraint constraint : callerMethodInvocations) {
	// summarizeEffects((InvokeConstraint) constraint);
	// }
	// }
	// }

	// // For each expanding substitution such as "X/X:R", apply "X/X:R:*" on
	// // effects which don't have any stars.
	// private void summarizeEffects(InvokeConstraint invokeConstraint) {
	// if (!invokeConstraint.isExpandingRecursive())
	// return;
	// Constraints effects =
	// constraintsRepository.methodEffects.get(invokeConstraint.ownerMethod);
	// Constraints summarizedEffects = new Constraints();
	// summarizedEffects =
	// effects.starFree().substituteRegions(invokeConstraint.regionParameters,
	// RPLUtils.starAppended(invokeConstraint.regionParameters,
	// invokeConstraint.regionActuals));
	// effects.addAll(summarizedEffects);
	// }

	// // From the list of read/write constraints, return those that don't
	// contain
	// // stars.
	// private Set<Constraint> starFree(Set<Constraint> readWriteEffects) {
	// Set<Constraint> withoutStars = new HashSet<Constraint>();
	// for (Constraint constraint : readWriteEffects) {
	// if (!((ReadWriteConstraint) constraint).containsStar()) {
	// withoutStars.add(constraint);
	// }
	// }
	// return withoutStars;
	// }

}
