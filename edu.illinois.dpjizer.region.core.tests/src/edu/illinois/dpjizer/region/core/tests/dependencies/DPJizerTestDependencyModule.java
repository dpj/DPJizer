/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.dependencies;

import com.sun.tools.javac.code.dpjizer.dirs.Dirs;

import edu.illinois.dpjizer.region.core.dependencies.DPJizerDependencyModule;
import edu.illinois.dpjizer.region.core.tests.dirs.DPJizerDirs;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class DPJizerTestDependencyModule extends DPJizerDependencyModule {

	@Override
	protected void configure() {
		super.configure();
		bind(Dirs.class).toInstance(new DPJizerDirs());
	}

	@Override
	protected Dirs getDirs() {
		return new DPJizerDirs();
	}

}
