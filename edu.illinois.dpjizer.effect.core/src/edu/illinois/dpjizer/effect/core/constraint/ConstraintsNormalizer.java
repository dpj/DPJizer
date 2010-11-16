/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintsNormalizer {

	ConstraintRepository constraintRepository;

	public ConstraintsNormalizer(ConstraintRepository constraintRepository) {
		super();
		this.constraintRepository = constraintRepository;
	}

	Constraint[] sortConstraintsForMethod(String methodName, Constraints constraintsForMethod) {
		if (constraintsForMethod == null)
			return new Constraint[] {};

		Constraint[] constraints = (Constraint[]) constraintsForMethod.toArray(new Constraint[constraintsForMethod.size()]);
		Arrays.sort(constraints);
		return constraints;
	}

	/**
	 * Maps in this class map method names into sets. Not all of these maps have
	 * the same keys. So, indexing a key in a map might return null while it
	 * returns a valid set in another map. To make things more regular and avoid
	 * excessive checking against null values, this method makes sure that every
	 * possible method name is mapped to a set in all maps. However, the set
	 * might be empty.
	 * 
	 */
	void makeSetValuesNonNull() {
		String[] methodNames = constraintRepository.allMethodNames();

		for (String methodName : methodNames) {
			constraintRepository.initializeIfNotExists(constraintRepository.methodInvocations, methodName);
			constraintRepository.initializeIfNotExists(constraintRepository.methodOverrides, methodName);
			constraintRepository.initializeIfNotExists(constraintRepository.methodEffects, methodName);
		}
	}

	void pruneRedundantConstraints(String methodName) {
		// pruneLocalEffects(methodName);
		pruneEqualEffects(methodName);
		pruneSubeffects(methodName);
		pruneEqualInvokes(methodName);
		// pruneDerivedInvokes(methodName);
		pruneCoveredInvocations(methodName);
	}

	// void pruneLocalEffects(String methodName) {
	// Constraints redundantConstraints = new Constraints();
	// Constraints methodConstraints =
	// constraintRepository.effectsOf(methodName);
	//
	// for (Constraint constraint : methodConstraints) {
	// if (constraint.isLocal()) {
	// redundantConstraints.add(constraint);
	// }
	// }
	//
	// constraintRepository.effectsOf(methodName).removeAll(redundantConstraints);
	// }

	void pruneEqualEffects(String methodName) {
		Constraints redundantConstraints = new Constraints();
		Constraints methodConstraints = constraintRepository.effectsOf(methodName);
		Constraint[] methodEffects = new Constraint[methodConstraints.size()];
		methodConstraints.toArray(methodEffects);
		for (int i = 0; i < methodEffects.length; ++i) {
			for (int j = i + 1; j < methodEffects.length; ++j) {
				Constraint effect1 = methodEffects[i];
				Constraint effect2 = methodEffects[j];
				if (effect1.equals(effect2) || (effect1.covers(effect2) && effect2.covers(effect1))) {
					// System.out.println(effect1 + " = " + effect2 + " => " +
					// methodEffects[i]);
					redundantConstraints.add(methodEffects[i]);
				}
			}
		}
		constraintRepository.effectsOf(methodName).removeAll(redundantConstraints);
	}

	void pruneEqualInvokes(String methodName) {
		Constraints redundantInvokesConstraints = new Constraints();
		Constraints methodInvokesConstraints = constraintRepository.methodInvocationsOf(methodName);
		Constraint[] methodInvokes = new Constraint[methodInvokesConstraints.size()];
		methodInvokesConstraints.toArray(methodInvokes);
		for (int i = 0; i < methodInvokes.length; ++i) {
			for (int j = i + 1; j < methodInvokes.length; ++j) {
				InvokeConstraint invokes1 = (InvokeConstraint) methodInvokes[i];
				InvokeConstraint invokes2 = (InvokeConstraint) methodInvokes[j];
				if (invokes1.equals(invokes2) || (invokes1.covers(invokes2) && invokes2.covers(invokes1))) {
					redundantInvokesConstraints.add(methodInvokes[i]);
				}
			}
		}
		constraintRepository.methodInvocationsOf(methodName).removeAll(redundantInvokesConstraints);
	}

	void pruneSubeffects(String methodName) {
		Constraints redundantReadsWritesConstraints = new Constraints();
		Constraints methodReadsWritesConstraints = constraintRepository.effectsOf(methodName);
		for (Iterator<Constraint> iter1 = methodReadsWritesConstraints.iterator(); iter1.hasNext();) {
			Constraint constraint1 = iter1.next();
			for (Iterator<Constraint> iter2 = methodReadsWritesConstraints.iterator(); iter2.hasNext();) {
				Constraint constraint2 = iter2.next();
				if (constraint1 != constraint2) {
					if (constraint2.covers(constraint1)) {
						// System.out.println(constraint1 + " <= " + constraint2
						// + " => " + constraint1);
						redundantReadsWritesConstraints.add(constraint1);
					}
				}
			}
		}
		constraintRepository.effectsOf(methodName).removeAll(redundantReadsWritesConstraints);
	}

	void pruneCoveredInvocations(String methodName) {
		Constraints redundantInvokesConstraints = new Constraints();
		Constraints methodInvokesConstraints = constraintRepository.methodInvocationsOf(methodName);
		for (Iterator<Constraint> iter1 = methodInvokesConstraints.iterator(); iter1.hasNext();) {
			Constraint constraint1 = iter1.next();
			for (Iterator<Constraint> iter2 = methodInvokesConstraints.iterator(); iter2.hasNext();) {
				Constraint constraint2 = iter2.next();
				if (constraint1 != constraint2) {
					if (constraint2.covers(constraint1)) {
						// System.out.println(constraint1 + " <= " + constraint2
						// + " => " + constraint1);
						redundantInvokesConstraints.add(constraint1);
					}
				}
			}
		}
		constraintRepository.methodInvocationsOf(methodName).removeAll(redundantInvokesConstraints);
	}

	void pruneDerivedInvokes(String methodName) {
		Constraints redundantInvokesConstraints = new Constraints();
		Constraints methodInvokesConstraints = constraintRepository.methodInvocationsOf(methodName);
		for (Iterator<Constraint> iter1 = methodInvokesConstraints.iterator(); iter1.hasNext();) {
			Constraint constraint1 = iter1.next();
			for (Iterator<Constraint> iter2 = methodInvokesConstraints.iterator(); iter2.hasNext();) {
				Constraint constraint2 = iter2.next();
				if (constraint1 != constraint2) {
					if (((InvokeConstraint) constraint2).derives(((InvokeConstraint) constraint1))) {
						// System.out.println(constraint1 + " <= " + constraint2
						// + " => " + constraint1);
						redundantInvokesConstraints.add(constraint1);
					}
				}
			}
		}
		constraintRepository.methodInvocationsOf(methodName).removeAll(redundantInvokesConstraints);
	}

	protected void pruneRedundantConstraints() {
		for (String methodName : constraintRepository.allMethodNames()) {
			pruneRedundantConstraints(methodName);
		}
	}

	void normalizeConstraints() {
		// makeSetValuesNonNull();
		// TODO: Does it prune local effects?
		// makeInitialConstraintsInEnv();
		// pruneRedundantConstraints();
	}

	// Apply the inEnvironment() method on all the constraints initially
	// collected for each method body.
	void makeInitialConstraintsInEnv() {
		makeSetValuesNonNull();

		for (String methodName : constraintRepository.allMethodNames()) {
			makeInvocationsInEnv(methodName);
			makeReadWritesInEnv(methodName);
		}
	}

	private void makeInvocationsInEnv(String methodName) {
		Constraints inEnvConstraints = new Constraints();
		inEnvConstraints = constraintRepository.methodInvocationsOf(methodName).inEnvironment(constraintRepository.methodEnvs, methodName);
		constraintRepository.updateMethodInvocations(methodName, inEnvConstraints);
	}

	private void makeReadWritesInEnv(String methodName) {
		Constraints inEnvConstraints = new Constraints();
		inEnvConstraints = constraintRepository.effectsOf(methodName).inEnvironment(constraintRepository.methodEnvs, methodName);
		constraintRepository.updateEffects(methodName, inEnvConstraints);
	}

}
