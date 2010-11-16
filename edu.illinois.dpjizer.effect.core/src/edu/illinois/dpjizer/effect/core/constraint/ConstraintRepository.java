/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import java.io.File;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.tools.javac.code.Effect;
import com.sun.tools.javac.code.Effect.ReadEffect;
import com.sun.tools.javac.code.Effect.WriteEffect;
import com.sun.tools.javac.code.Effects;
import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.effect.core.constraint.ConstraintsWriter.WhatToSerialize;
import edu.illinois.dpjizer.effect.core.log.Dirs;
import edu.illinois.dpjizer.effect.core.log.Logger;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintRepository {

	ConstraintsWriter constraintsWriter = new ConstraintsWriter(this);
	ConstraintsNormalizer constraintsNormalizer = new ConstraintsNormalizer(this);
	ConstraintsSolver constraintsSolver = new ConstraintsSolver(this);
	Map<String, Constraints> methodEffects;
	Map<String, Constraints> methodInvocations;
	Map<String, Constraints> methodOverrides;
	Map<String, Method> methodsToAnnotate;
	MethodEnvironments methodEnvs;

	public ConstraintRepository(MethodEnvironments methodEnvs) {
		super();
		methodEffects = new HashMap<String, Constraints>();
		methodInvocations = new HashMap<String, Constraints>();
		methodOverrides = new HashMap<String, Constraints>();
		methodsToAnnotate = new HashMap<String, Method>();
		if (methodEnvs == null)
			throw new RuntimeException("Unexpected MethodEnvironments");
		this.methodEnvs = methodEnvs;
	}

	public Constraints methodInvocationsOf(String methodName) {
		initializeIfNotExists(methodInvocations, methodName);
		return methodInvocations.get(methodName);
	}

	public Constraints effectsOf(String methodName) {
		initializeIfNotExists(methodEffects, methodName);
		return methodEffects.get(methodName);
	}

	public Constraints methodOverridesOf(String methodName) {
		initializeIfNotExists(methodOverrides, methodName);
		return methodOverrides.get(methodName);
	}

	protected void initializeIfNotExists(Map<String, Constraints> map, String key) {
		if (key == null)
			throw new RuntimeException("null key inserted into the map");
		if (!map.containsKey(key)) {
			map.put(key, new Constraints());
		}
	}

	protected void addToKey(Map<String, Constraints> map, String key, Constraint value) {
		initializeIfNotExists(map, key);
		map.get(key).add(value);
	}

	public void updateMethodInvocations(String methodName, Constraints constraints) {
		methodInvocations.put(methodName, constraints);
	}

	public void updateEffects(String methodName, Constraints constraints) {
		methodEffects.put(methodName, constraints);
	}

	/**
	 * The "reads" and "writes" effects of "m1" should contain those of "m2".
	 */
	public void addInvokeConstraint(String callerMethod, String calleeMethod, List<RegionParameterSymbol> classRegionParameters,
			List<RPL> classRegionActuals, RPL receiver) {
		addToKey(methodInvocations, callerMethod, new InvokeConstraint(callerMethod, calleeMethod, classRegionParameters, classRegionActuals,
				receiver));
	}

	public void addInvokeConstraint(InvokeConstraint invokeConstraint) {
		addToKey(methodInvocations, invokeConstraint.ownerMethod, invokeConstraint);
	}

	public void addInvokeConstraints(Constraints invokeConstraints) {
		for (Constraint constraint : invokeConstraints) {
			addInvokeConstraint((InvokeConstraint) constraint);
		}
	}

	/**
	 * Effects of overridden should include those of the overrider.
	 */
	public void addOverrideConstraint(String overrider, String overridden, List<RegionParameterSymbol> parentRegionParameters,
			List<RPL> childRegionParamaters, List<RegionParameterSymbol> overridenRegionParameters,
			List<RegionParameterSymbol> overriderRegionParameters) {
		addToKey(methodOverrides, overrider, new OverrideConstraint(overrider, overridden, parentRegionParameters, childRegionParamaters,
				overridenRegionParameters, overriderRegionParameters));
	}

	/**
	 * Converts the set of effects of statements in the given method to a set of
	 * constraints for the method "callerMethod".
	 * 
	 * @param callerMethod
	 * @param effects
	 *            the set of effects of statements in the body of the given
	 *            method.
	 */
	public void addReadWriteConstraints(String callerMethod, Effects effects) {
		for (Effect effect : effects/* .pruneLocalEffects() */) {
			if (effect instanceof ReadEffect) {
				addReadConstraint(callerMethod, ((ReadEffect) effect).rpl);
			} else if (effect instanceof WriteEffect) {
				addWriteConstraint(callerMethod, ((WriteEffect) effect).rpl);
			}
		}
	}

	public void addWriteConstraint(String callerMethod, RPL writtenRegion) {
		addToKey(methodEffects, callerMethod, new WriteConstraint(callerMethod, writtenRegion));
	}

	public void addReadConstraint(String callerMethod, RPL readRegion) {
		addToKey(methodEffects, callerMethod, new ReadConstraint(callerMethod, readRegion));
	}

	public void addMethod(Method method) {
		if (method.getName() == null)
			throw new RuntimeException("Unknown method");
		methodsToAnnotate.put(method.getName(), method);
	}

	String[] allMethodNames() {
		Set<String> allMethods = new HashSet<String>();

		allMethods.addAll(methodsToAnnotate.keySet());
		allMethods.addAll(methodInvocations.keySet());

		// Add the set of invoked methodsToAnnotate to the list of all method
		// names.
		for (Constraints invocationConstratints : methodInvocations.values()) {
			for (Constraint constraint : invocationConstratints) {
				allMethods.add(((InvokeConstraint) constraint).invokedMethod);
			}
		}

		allMethods.addAll(methodOverrides.keySet());
		for (Constraints overrideConstraints : methodOverrides.values()) {
			for (Constraint constraint : overrideConstraints) {
				allMethods.add(((OverrideConstraint) constraint).overriddenMethod);
			}
		}

		allMethods.addAll(methodEffects.keySet());
		String[] sortedMethodNames = (String[]) allMethods.toArray(new String[allMethods.size()]);
		Arrays.sort(sortedMethodNames);
		return sortedMethodNames;
	}

	Constraints getReadEffects(String methodName) {
		Constraints readEffects = new Constraints();
		for (Constraint constraint : effectsOf(methodName)) {
			if (constraint instanceof ReadConstraint)
				readEffects.add(constraint);
		}
		return readEffects;
	}

	Constraints getWriteEffects(String methodName) {
		Constraints writeEffects = new Constraints();
		for (Constraint constraint : effectsOf(methodName)) {
			if (constraint instanceof WriteConstraint)
				writeEffects.add(constraint);
		}
		return writeEffects;
	}

	public void collectAndWriteConstraints() {
		constraintsNormalizer.makeInitialConstraintsInEnv();
		constraintsNormalizer.normalizeConstraints();
		String filepath = Dirs.ACTUAL_CONSTRAINTS_PATH;
		if (new File(Dirs.ROOT_TEST_PROGRAMS).exists()) {
			constraintsWriter.serializeAllConstraints(filepath, ConstraintsWriter.WhatToSerialize.ALL);
		} else {
			Logger.log(Dirs.ROOT_TEST_PROGRAMS + " not found.");
		}
	}

	public Map<Method, String> collectSolveAndWriteConstraints() {
		collectAndWriteConstraints();
		constraintsSolver.solveConstraints();
		constraintsNormalizer.normalizeConstraints();

		String filepath = Dirs.ACTUAL_SOLVED_CONSTRAINTS_PATH;
		if (new File(Dirs.ROOT_TEST_PROGRAMS).exists()) {
			constraintsWriter.serializeAllConstraints(filepath, ConstraintsWriter.WhatToSerialize.READS_WRITES);
		} else {
			Logger.log(Dirs.ROOT_TEST_PROGRAMS + " not found.");
		}
		Map<Method, String> toEffects = constraintsWriter.constraintsToEffects(ConstraintsWriter.WhatToSerialize.READS_WRITES);
		return toEffects;
	}

	public void summarizeAllInvocationConstraints() {
		for (String callerMethod : allMethodNames()) {
			Constraints callerMethodInvocations = methodInvocationsOf(callerMethod);
			summarizeInvocationConstraints(callerMethodInvocations);
		}
	}

	private void summarizeInvocationConstraints(Constraints callerMethodInvocations) {
		Constraints summarized = new Constraints();
		for (Constraint constraint : callerMethodInvocations) {
			summarized.add(((InvokeConstraint) constraint).summarized());
		}
		callerMethodInvocations.addAll(summarized);
	}

	@Override
	public String toString() {
		StringWriter stringWriter = new StringWriter();
		constraintsWriter.serializeAllConstraints(stringWriter, WhatToSerialize.ALL);
		return stringWriter.toString();
	}
}
