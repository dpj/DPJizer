/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.testsetup;

import org.junit.Before;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.tools.javac.code.dpjizer.constraints.RegionVarEltSymbol;

import edu.illinois.dpjizer.region.core.compile.CompilerInvoker;
import edu.illinois.dpjizer.region.core.tests.dependencies.DPJizerTestDependencyModule;
import edu.illinois.dpjizer.region.core.tests.dirs.DPJizerTestDirs;
import edu.illinois.dpjizer.region.core.tests.dirs.FileComparator;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class DependencyInjector {

	protected Injector guiceInjector;
	protected CompilerInvoker compilerInvoker;
	protected DPJizerTestDirs dpjizerTestDirs;
	protected FileComparator fileComparator;

	@Before
	public void setup() {
		guiceInjector = Guice.createInjector(new DPJizerTestDependencyModule());
		compilerInvoker = guiceInjector.getInstance(CompilerInvoker.class);
		dpjizerTestDirs = guiceInjector.getInstance(DPJizerTestDirs.class);
		fileComparator = guiceInjector.getInstance(FileComparator.class);
		RegionVarEltSymbol.numIDs = 1;
	}

}
