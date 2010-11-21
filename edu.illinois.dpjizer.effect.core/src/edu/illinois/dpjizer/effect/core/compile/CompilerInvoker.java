/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.compile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import com.sun.tools.javac.code.Source;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Pair;

import edu.illinois.dpjizer.effect.core.constraint.Method;
import edu.illinois.dpjizer.utils.Modes;
import edu.illinois.dpjizer.utils.NullOutputStream;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 * 
 */
public class CompilerInvoker {

	public enum EraseDPJ {
		YES, NO
	}

	public enum LastPhase {
		COMPILE, COLLECT_CONSTRAINTS, SOLVE_CONSTRAINTS
	}

	Context context;
	ConstraintSolverPhase constraintSolverPhase;

	public CompilerInvoker() {
		super();
		// RegionParameterSymbol.numIDs = 0;
	}

	private void redirectCompilerMessages() {
		if (!Modes.isInDebugMode()) {
			context.put(Log.outKey, new PrintWriter(new NullOutputStream()));
		}
	}

	protected List<JCCompilationUnit> parseFiles(String[] filepaths) {
		context = new Context();
		redirectCompilerMessages();
		JavacFileManager.preRegister(context);
		// ConstraintCollector.preRegister(context);

		// Force Java 1.5 parsing and code generation
		Options.instance(context).put(OptionName.SOURCE, Source.JDK1_5.name);
		Options.instance(context).put(OptionName.TARGET, Target.JDK1_5.name);

		JavaCompiler comp = JavaCompiler.instance(context);

		List<JavaFileObject> javaFileObjects = javaFileObjectsOf(filepaths);
		List<JCCompilationUnit> astList;
		try {
			astList = comp.parseFiles(javaFileObjects);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (astList == null)
			throw new RuntimeException("The ast list is null.");
		for (JCCompilationUnit compilationUnit : astList) {
			if (compilationUnit == null)
				throw new RuntimeException("A returned AST is null.");
		}
		return astList;
	}

	private List<JavaFileObject> javaFileObjectsOf(String[] filepaths) {
		JavacFileManager fileManager = (JavacFileManager) context.get(JavaFileManager.class);
		JavaFileObject[] javaFileObjects = new JavaFileObject[filepaths.length];
		for (int i = 0; i < filepaths.length; ++i) {
			javaFileObjects[i] = fileManager.getRegularFile(new File(filepaths[i]));
		}
		return List.from(javaFileObjects);
	}

	protected List<Pair<Env<AttrContext>, JCClassDecl>> analyzeASTs(List<JCCompilationUnit> astList, LastPhase lastPhase) throws Throwable {
		return analyzeASTs(astList, false, lastPhase);
	}

	private List<Pair<Env<AttrContext>, JCClassDecl>> analyzeASTs(List<JCCompilationUnit> astList, boolean expectErrors, LastPhase lastPhase)
			throws Throwable {
		if (context == null) {
			throw new RuntimeException("Must call #parse first.");
		}

		JavaCompiler comp = JavaCompiler.instance(context);
		comp.eraseDPJ = false;
		comp.enterTrees(astList);

		List<Pair<Env<AttrContext>, JCClassDecl>> result = null;

		constraintSolverPhase = new ConstraintSolverPhase();
		if (lastPhase == LastPhase.SOLVE_CONSTRAINTS) {
			result = comp.desugar(comp.flow(constraintSolverPhase.collectSolveAndWriteConstraints(comp.checkEffects(comp.attribute(comp.todo)),
					context)));
		} else if (lastPhase == LastPhase.COLLECT_CONSTRAINTS) {
			result = comp.desugar(comp.flow(constraintSolverPhase.collectAndWriteConstraints(comp.checkEffects(comp.attribute(comp.todo)), context)));
		} else if (lastPhase == LastPhase.COMPILE) {
			result = comp.desugar(comp.checkEffects(comp.attribute(comp.todo)));
		}

		if (!expectErrors)
			if (result.isEmpty())
				throw new RuntimeException("Result is empty.");

		context.get(JavaFileManager.class).close();
		if (expectErrors != (Log.instance(context).nerrors != 0))
			new RuntimeException("Errors were (not) reported as expected.");
		return result;
	}

	public void compile(String[] filepaths) throws Throwable {
		analyzeASTs(parseFiles(filepaths), LastPhase.COMPILE);
	}

	public void collectConstraints(String[] filepaths) throws Throwable {
		analyzeASTs(parseFiles(filepaths), LastPhase.COLLECT_CONSTRAINTS);
	}

	public ArrayList<Map<Method, String>> solveConstraints(String[] filepaths) throws Throwable {
		analyzeASTs(parseFiles(filepaths), LastPhase.SOLVE_CONSTRAINTS);
		return constraintSolverPhase.getSolutions();
	}

}
