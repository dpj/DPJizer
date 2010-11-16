/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.visitors;

import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.comp.EnvScanner;
import com.sun.tools.javac.parser.Parser;
import com.sun.tools.javac.parser.Scanner;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class MethodParameterInserter extends EnvScanner {

	protected static final Context.Key<MethodParameterInserter> methodParameterInserterKey = new Context.Key<MethodParameterInserter>();

	Scanner.Factory scannerFactory;
	Parser.Factory parserFactory;
	TreeMaker treeMaker;

	public static MethodParameterInserter instance(Context context) {
		MethodParameterInserter instance = context.get(methodParameterInserterKey);
		if (instance == null)
			instance = new MethodParameterInserter(context);
		return instance;
	}

	protected MethodParameterInserter(Context context) {
		super(context);
		context.put(methodParameterInserterKey, this);
		scannerFactory = Scanner.Factory.instance(context);
		parserFactory = Parser.Factory.instance(context);
	}

	public void visitMethodDef(JCMethodDecl tree) {
		super.visitMethodDef(tree);

		List<String> rgnParamNames = getMethodRegionParams(tree);

		if (rgnParamNames.nonEmpty()) {
			String rgnParamsString = regionParamsToStr(rgnParamNames);
			Parser P = parserFor(rgnParamsString);
			tree.paramInfo = P.typeRPLEffectParamsOpt().snd;
		}
	}

	private String regionParamsToStr(List<String> rgnParamNames) {
		StringBuilder sb = new StringBuilder();
		sb.append("< region ");

		for (; rgnParamNames.nonEmpty(); rgnParamNames = rgnParamNames.tail) {
			sb.append(rgnParamNames.head);
			if (rgnParamNames.size() > 1)
				sb.append(",");
		}

		sb.append(">");
		return sb.toString();
	}

	private List<String> getMethodRegionParams(JCMethodDecl tree) {
		ListBuffer<String> rgnParamNamesBuf = ListBuffer.lb();

		for (JCVariableDecl varDecl : tree.params) {
			for (List<RegionParameterSymbol> allrgnparams = varDecl.type.allrgnparams(); allrgnparams.nonEmpty(); allrgnparams = allrgnparams.tail) {
				rgnParamNamesBuf.append(allrgnparams.head.toString() + "_" + varDecl.name);
			}
		}
		return rgnParamNamesBuf.toList();
	}

	private Parser parserFor(String programFragment) {
		Scanner S = scannerFactory.newScanner(programFragment);
		Parser P = parserFactory.newParser(S, true, false);
		return P;
	}

}
