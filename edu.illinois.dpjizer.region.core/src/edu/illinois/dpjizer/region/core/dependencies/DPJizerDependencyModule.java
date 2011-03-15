/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.dependencies;

import java.io.PrintWriter;

import com.google.inject.AbstractModule;
import com.sun.tools.javac.code.dpjizer.constraints.ConstraintRepository;
import com.sun.tools.javac.code.dpjizer.constraints.Constraints;
import com.sun.tools.javac.code.dpjizer.dirs.Dirs;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;
import com.sun.tools.javac.util.Log;

import edu.illinois.dpjizer.region.core.constraints.ConstraintsSet;
import edu.illinois.dpjizer.region.core.parser.DPJizerParser;
import edu.illinois.dpjizer.region.core.visitors.ConstraintCollector;
import edu.illinois.dpjizer.region.core.visitors.DPJizerAttr;
import edu.illinois.dpjizer.utils.Modes;
import edu.illinois.dpjizer.utils.NullOutputStream;

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

	private void redirectCompilerMessages() {
		if (!Modes.isInDebugMode()) {
			context.put(Log.outKey, new PrintWriter(new NullOutputStream()));
		}
	}

	private void createInstances() {
		constraints = new ConstraintsSet();
		constraintRepository = new ConstraintRepository(constraints, getDirs());
		context = new Context();

		redirectCompilerMessages();
		JavacFileManager.preRegister(context);

		// Get an instance of Attr through DPJizerAttr so that all calls to
		// Attr.instance() return an instance of DPJizerAttr instead of Attr.
		DPJizerAttr.instance(context, constraints);

		// Replace Parser.Factory by DPJizerParser.DPJizerFactory in the same
		// way that Attr was replaced by DPJizerAttr.
		DPJizerParser.DPJizerFactory.instance(context);
	}

}
