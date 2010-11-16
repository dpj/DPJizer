/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.compile;

import java.io.File;
import java.io.IOException;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import com.google.inject.Inject;
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

import edu.illinois.dpjizer.region.core.change.Changes;
import edu.illinois.dpjizer.region.core.constraints.Constraints;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class CompilerInvoker {

	public enum EraseDPJ {
		YES, NO
	}

	Context context;

	Constraints constraints;

	ConstraintCollectionPhase constraintCollectionPhase;

	@Inject
	public CompilerInvoker(Context context, ConstraintCollectionPhase constraintCollectionPhase, Constraints constraints) {
		this.context = context;
		this.constraintCollectionPhase = constraintCollectionPhase;
		this.constraints = constraints;
	}

	protected List<JCCompilationUnit> parseFiles(String[] filepaths) {
		// context = new Context();

		// JavacFileManager.preRegister(context);
		// ConstraintCollector.preRegister(context);

		// Get an instance of Attr through DPJizerAttr so that all calls to
		// Attr.instance() return an instance of DPJizerAttr instead of Attr.
		// Attr attr = DPJizerAttr.instance(context, constraints);

		// Replace Parser.Factory by DPJizerParser.DPJizerFactory in the same
		// that Attr was replaced by DPJizerAttr.
		// Parser.Factory parserFactory =
		// DPJizerParser.DPJizerFactory.instance(context);

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

	protected List<Pair<Env<AttrContext>, JCClassDecl>> analyzeASTs(List<JCCompilationUnit> astList, Phase lastPhase) {
		return analyzeASTs(astList, false, lastPhase);
	}

	private List<Pair<Env<AttrContext>, JCClassDecl>> analyzeASTs(List<JCCompilationUnit> astList, boolean expectErrors, Phase additionalPhase) {
		if (context == null) {
			throw new RuntimeException("Must call #parse first.");
		}

		JavaCompiler comp = JavaCompiler.instance(context);
		comp.eraseDPJ = false;
		comp.enterTrees(astList);

		List<Pair<Env<AttrContext>, JCClassDecl>> result = null;

		result = comp.desugar(comp.flow(additionalPhase.analyze(comp.checkEffects(comp.attribute(comp.todo)), context)));

		if (!expectErrors)
			if (result.isEmpty())
				// throw new RuntimeException("Result is empty.");
				System.out.println("Result is empty.\n");

		try {
			context.get(JavaFileManager.class).close();
		} catch (IOException e) {
			throw new RuntimeException("Error in closing the context.", e);
		}

		if (expectErrors != (Log.instance(context).nerrors != 0)) {
			// new RuntimeException("Errors were (not) reported as expected.");
			System.out.println("Errors were (not) reported as expected.\n");
		}

		return result;
	}

	public void compile(String[] filepaths) {
		analyzeFiles(filepaths, new IdentitiyPhase());
	}

	private Changes analyzeFiles(String[] filepaths, Phase additionalPhase) {
		analyzeASTs(parseFiles(filepaths), additionalPhase);
		return additionalPhase.getChanges();
	}

	public Changes collectConstraints(String[] filepaths) {
		return analyzeFiles(filepaths, constraintCollectionPhase);
	}

}
