/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.transform;

import java.io.IOException;
import java.io.Writer;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.util.List;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class DPJizerPretty extends Pretty {

	public DPJizerPretty(Writer out, boolean sourceOutput, int codeGenMode) {
		super(out, sourceOutput, codeGenMode);
	}

	public <T extends RPL> void printRPLs(List<T> trees, String sep)
			throws IOException {
		if (trees.nonEmpty()) {
			print(trees.head.toString());
			for (List<T> l = trees.tail; l.nonEmpty(); l = l.tail) {
				print(sep);
				print(l.head.toString());
			}
		}
	}

	public <T extends RPL> void printRPLs(List<T> trees) throws IOException {
		printRPLs(trees, ", ");
	}

	public void printArguments(List<JCExpression> typeargs, List<RPL> rplargs,
			boolean printKeyword) throws IOException {
		boolean rplsToPrint = ((codeGenMode == NONE) && rplargs.nonEmpty());
		if (!typeargs.isEmpty() || rplsToPrint) {
			print("<");
			printExprs(typeargs);
			if (typeargs.nonEmpty() && rplsToPrint)
				print(", ");
			if (rplsToPrint && printKeyword)
				print("region ");
			printRPLs(rplargs);
			print(">");
		}
	}

	@Override
	public void visitApply(JCMethodInvocation tree) {
		try {
			MethodType methodType = (MethodType) tree.meth.type;
			List<RPL> regionActuals = methodType.regionActuals;
			if (!tree.typeargs.isEmpty() || !regionActuals.isEmpty()) {
				if (tree.meth.getTag() == JCTree.SELECT) {
					JCFieldAccess left = (JCFieldAccess) tree.meth;
					printExpr(left.selected);
					print(".");
					printArguments(tree.typeargs, regionActuals, true);
					print(left.name);
				} else {
					printArguments(tree.typeargs, regionActuals, true);
					printExpr(tree.meth);
				}
			} else {
				printExpr(tree.meth);
			}
			print("(");
			printExprs(tree.args);
			print(")");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void visitNewArray(JCNewArray tree) {
		try {
			print(newArrayToString(tree));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void visitIdent(JCIdent tree) {
		try {
			if (thisIsBogus && tree.toString().equals("this"))
				print("__dpj_this");
			else if (printOwner && tree.toString().equals("this")) {
				Types.printDPJ = false;
				print(tree.sym.owner.type + "." + "this");
				Types.printDPJ = false;
			} else {
				// BEGIN MODIFICATION
				if (tree.sym instanceof Symbol.ClassSymbol)
					print(tree.type);
				else
					// END MODIFICATION
					print(tree.name);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @basedOn com.sun.tools.javac.code.Type.ArrayType.toString()
	 * @param arrayType
	 * @return
	 */
	protected String newArrayToString(JCNewArray tree) {
		ArrayType arrayType = (ArrayType) tree.type;
		StringBuilder sb = new StringBuilder();
		sb.append("new ");
		sb.append(getBaseType(arrayType));
		Type nextType = arrayType;
		List<JCExpression> l = tree.dims;
		do {
			ArrayType currentType = (ArrayType) nextType;
			sb.append("[");
			sb.append(l.head);
			sb.append("]<");
			sb.append(currentType.rpl);
			sb.append(">");
			if (currentType.indexVar != null) {
				sb.append("#");
				sb.append(currentType.indexVar);
			}
			nextType = currentType.elemtype;
			l = l.tail;
		} while (nextType instanceof ArrayType);
		return sb.toString();
	}

	protected Type getBaseType(Type baseType) {
		while (baseType instanceof ArrayType) {
			baseType = ((ArrayType) baseType).elemtype;
		}
		return baseType;
	}

	@Override
	protected void printBrackets(JCArrayTypeTree tree) throws IOException {
		ArrayType arrayType = (ArrayType) tree.type;
		Type nextType = arrayType;
		do {
			ArrayType currentType = (ArrayType) nextType;
			print("[]<");
			print(currentType.rpl);
			print(">");
			if (currentType.indexVar != null) {
				print("#");
				print(currentType.indexVar);
			}
			nextType = currentType.elemtype;
		} while (nextType instanceof ArrayType);
	}

}
