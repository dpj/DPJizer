/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Formats the constraints to be inserted back to the input DPJ files. For
 * debugging purposes one might like to print out invokes and isOverriddenBy
 * constraints as well. One can configure what constraint to include in the
 * output strings.
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintsWriter {
	public enum WhatToSerialize {
		ALL, READS_WRITES
	}

	PrintWriter out;
	ConstraintRepository constraintRepository;

	public ConstraintsWriter(ConstraintRepository constraintRepository) {
		this.constraintRepository = constraintRepository;
	}

	void serializeAllConstraints(String filePath, ConstraintsWriter.WhatToSerialize whatToSerialize) {
		try {
			out = new PrintWriter(filePath);
			serializeAllConstraints(new PrintWriter(filePath), whatToSerialize);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Output the list of effects of each method.
	 * 
	 */
	void serializeAllConstraints(Writer out, ConstraintsWriter.WhatToSerialize whatToSerialize) {
		try {
			String[] allMethodNames = constraintRepository.allMethodNames();
			Map<Method, String> constraintsToString = constraintsToEffects(whatToSerialize);
			for (String methodName : allMethodNames) {
				if (constraintsToString.containsKey(constraintRepository.methodsToAnnotate.get(methodName))) {
					out.write(methodName + " " + constraintsToString.get(constraintRepository.methodsToAnnotate.get(methodName)));
					out.write(System.getProperty("line.separator"));
				}
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	Map<Method, String> constraintsToEffects(ConstraintsWriter.WhatToSerialize whatToSerialize) {
		Map<Method, String> methodEffects = new HashMap<Method, String>();
		String[] allMethodNames = constraintRepository.allMethodNames();
		for (String methodName : allMethodNames) {
			StringBuilder effectsStringBuilder = new StringBuilder();
			Constraints thisMethodReadEffects = constraintRepository.getReadEffects(methodName);
			Constraints thisMethodWriteEffects = constraintRepository.getWriteEffects(methodName);
			Constraints thisMethodInvocations = constraintRepository.methodInvocationsOf(methodName);
			Constraints thisMethodOverrides = constraintRepository.methodOverridesOf(methodName);

			if (!thisMethodReadEffects.isEmpty()) {
				effectsStringBuilder.append("reads ");
				effectsStringBuilder.append(constraintsToClauses(constraintRepository.constraintsNormalizer.sortConstraintsForMethod(methodName,
						thisMethodReadEffects)));
				if (!thisMethodWriteEffects.isEmpty()) {
					effectsStringBuilder.append(" ");
				}
			}
			if (!thisMethodWriteEffects.isEmpty()) {
				effectsStringBuilder.append("writes ");
				effectsStringBuilder.append(constraintsToClauses(constraintRepository.constraintsNormalizer.sortConstraintsForMethod(methodName,
						thisMethodWriteEffects)));
			}
			if (whatToSerialize == WhatToSerialize.ALL) {
				if ((!thisMethodReadEffects.isEmpty() || !thisMethodWriteEffects.isEmpty()) && !thisMethodInvocations.isEmpty()) {
					effectsStringBuilder.append(" ");
				}
				if (!thisMethodInvocations.isEmpty()) {
					effectsStringBuilder.append("invokes ");
					effectsStringBuilder.append(constraintsToClauses(constraintRepository.constraintsNormalizer.sortConstraintsForMethod(methodName,
							thisMethodInvocations)));
				}

				if ((!thisMethodReadEffects.isEmpty() || !thisMethodWriteEffects.isEmpty() || !thisMethodInvocations.isEmpty())
						&& !thisMethodOverrides.isEmpty()) {
					effectsStringBuilder.append(" ");
				}
				if (!thisMethodOverrides.isEmpty()) {
					effectsStringBuilder.append("overrides ");
					effectsStringBuilder.append(constraintsToClauses(constraintRepository.constraintsNormalizer.sortConstraintsForMethod(methodName,
							thisMethodOverrides)));
				}

			}

			// if (!thisMethodReadEffects.isEmpty() ||
			// !thisMethodWriteEffects.isEmpty()
			// || (whatToSerialize == WhatToSerialize.ALL &&
			// (!thisMethodInvocations.isEmpty() ||
			// !thisMethodOverrides.isEmpty()))) {

			// If inferring the effects for 'methodName' is desired...
			if (constraintRepository.methodsToAnnotate.containsKey(methodName)) {
				String effectsString = effectsStringBuilder.toString();
				if (effectsString.equals("")) {
					// FIXME: Javac generates a default constructor if needed
					// and this causes a pure annotation to be placed in a wrong
					// place after the keyword 'class'.
					effectsString = "pure";
				}
				methodEffects.put(constraintRepository.methodsToAnnotate.get(methodName), effectsString);
			}
			// }
		}
		return methodEffects;
	}

	/**
	 * Summarizes "reads r1 reads r2" as "read r1, r2", ... .
	 * 
	 * @param constraints
	 * @return
	 */
	String constraintsToClauses(Constraint[] constraints) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < constraints.length; i++) {
			builder.append(constraints[i].getClause());
			if (i < constraints.length - 1)
				builder.append(", ");
		}
		return builder.toString();
	}

	public void cleanup(ConstraintRepository constraints) {
		out.close();
	}
}