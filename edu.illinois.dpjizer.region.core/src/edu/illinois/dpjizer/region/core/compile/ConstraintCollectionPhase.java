/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.compile;

import com.google.inject.Inject;
import com.sun.tools.javac.code.dpjizer.constraints.ConstraintRepository;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.region.core.transform.RegionVarWriter;
import edu.illinois.dpjizer.region.core.visitors.ConstraintCollector;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintCollectionPhase extends Phase {

	ConstraintCollector constraintCollector;
	RegionVarWriter regionVarWriter;

	@Inject
	public ConstraintCollectionPhase(ConstraintCollector constraintCollector, RegionVarWriter regionVarWriter) {
		this.constraintCollector = constraintCollector;
		this.regionVarWriter = regionVarWriter;
	}

	@Override
	public List<Env<AttrContext>> analyze(List<Env<AttrContext>> envs, Context context) {
		visit(envs, constraintCollector);
		prettyPrint(envs, regionVarWriter);
		ConstraintRepository constraintRepository = constraintCollector.getConstraintRepository();
		constraintRepository.writeToFile();
		constraintRepository.solve();
		return envs;
	}

}
