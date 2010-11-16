/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import java.util.HashMap;
import java.util.Map;

import com.sun.tools.javac.code.Effects;
import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;

import edu.illinois.dpjizer.effect.core.effect.EffectUtils;
import edu.illinois.dpjizer.effect.core.rpl.RPLUtils;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class MethodEnvironments {
	Resolve rs;
	Map<String, Env<AttrContext>> methodEnvs;
	static MethodEnvironments singleInstance = null;

	public enum Prune {
		PRUNE_LOCALS, KEEP_LOCALS
	};

	public static MethodEnvironments instance(Resolve rs) {
		if (singleInstance != null)
			return singleInstance;
		singleInstance = new MethodEnvironments(rs);
		return singleInstance;
	}

	public static MethodEnvironments instance() {
		return singleInstance;
	}

	public MethodEnvironments(Resolve rs) {
		super();
		this.rs = rs;
		methodEnvs = new HashMap<String, Env<AttrContext>>();
	}

	public void setMethodEnv(String methodName, Env<AttrContext> env) {
		methodEnvs.put(methodName, env);
	}

	public Env<AttrContext> getMethodEnv(String methodName) {
		return methodEnvs.get(methodName);
	}

	public RPL inEnvironment(RPL rpl, String methodName, Prune prune) {
		return RPLUtils.inEnvironment(rpl, rs, methodEnvs.get(methodName), prune);
	}

	public Effects inEnvironment(Effects effects, String methodName) {
		return EffectUtils.inEnvironment(effects, rs, methodEnvs.get(methodName));
	}
}
