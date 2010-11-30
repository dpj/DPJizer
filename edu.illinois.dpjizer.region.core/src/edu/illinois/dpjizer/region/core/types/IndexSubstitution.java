/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCExpression;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class IndexSubstitution implements Substitution {

	VarSymbol varSymbol;

	JCExpression expression;

	public IndexSubstitution(VarSymbol varSym, JCExpression expression) {
		super();
		this.varSymbol = varSym;
		this.expression = expression;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(varSymbol.toString());
		sb.append(" <- ");
		sb.append(expression.toString());
		return sb.toString();
	}
}
