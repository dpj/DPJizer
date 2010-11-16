/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.transform;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.Pretty;

import edu.illinois.dpjizer.region.core.dirs.FileUtils;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public abstract class Transformer {

	protected String getSourceFileName(JCCompilationUnit cuTree) {
		return cuTree.sourcefile.toString();
	}

	protected void prettyPrintTo(JCCompilationUnit cuTree, String outputDir) {
		PrintWriter printWriter = createOutputWriter(cuTree, outputDir);
		Pretty pretty = createPrettyPrinter(printWriter);
		try {
			pretty.printUnit(cuTree, null);
		} catch (IOException e) {
			throw new RuntimeException("Error in pretty printing.", e);
		}
		printWriter.close();
	}

	protected Pretty createPrettyPrinter(PrintWriter printWriter) {
		Pretty pretty = new Pretty(printWriter, true, 0);
		return pretty;
	}

	protected PrintWriter createOutputWriter(JCCompilationUnit cuTree, String outputDir) {
		try {
			PrintWriter writer = null;
			FileUtils.createDirIfNotFound(outputDir);
			String annotatedSourceFileName = outputDir + cuTree.sourcefile.getName();
			writer = new PrintWriter(annotatedSourceFileName);
			return writer;
		} catch (IOException e) {
			throw new RuntimeException("Error in pretty priting the transformed AST.", e);
		}
	}

	public void prettyPrint(JCCompilationUnit cuTree) {
		String originalSourceFileName = getSourceFileName(cuTree);
		String outputDir = outputDir(originalSourceFileName);
		prettyPrintTo(cuTree, outputDir);
	}

	abstract String outputDir(String originalSourceFileName);

}
