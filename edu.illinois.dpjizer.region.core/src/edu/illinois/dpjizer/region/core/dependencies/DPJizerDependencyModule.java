/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.dependencies;

import com.google.inject.AbstractModule;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;

import edu.illinois.dpjizer.region.core.constraints.ConstraintRepository;
import edu.illinois.dpjizer.region.core.constraints.Constraints;
import edu.illinois.dpjizer.region.core.constraints.ConstraintsSet;
import edu.illinois.dpjizer.region.core.dirs.Dirs;
import edu.illinois.dpjizer.region.core.parser.DPJizerParser;
import edu.illinois.dpjizer.region.core.visitors.ConstraintCollector;
import edu.illinois.dpjizer.region.core.visitors.DPJizerAttr;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public abstract class DPJizerDependencyModule extends AbstractModule {

	private Constraints constraints;
	private ConstraintRepository constraintRepository;
	private Context context;

	@Override
	protected void configure() {
		createInstances();
		bind(Constraints.class).toInstance(constraints);
		bind(Context.class).toInstance(context);
		bind(ConstraintRepository.class).toInstance(constraintRepository);
		bind(ConstraintCollector.class).toInstance(new ConstraintCollector(context, constraintRepository));
	}

	abstract protected Dirs getDirs();

	private void createInstances() {
		constraints = new ConstraintsSet();
		constraintRepository = new ConstraintRepository(constraints, getDirs());
		context = new Context();
		JavacFileManager.preRegister(context);

		// Get an instance of Attr through DPJizerAttr so that all calls to
		// Attr.instance() return an instance of DPJizerAttr instead of Attr.
		DPJizerAttr.instance(context, constraints);

		// Replace Parser.Factory by DPJizerParser.DPJizerFactory in the same
		// way that Attr was replaced by DPJizerAttr.
		DPJizerParser.DPJizerFactory.instance(context);
	}

}
