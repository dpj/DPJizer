/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.effect;

import com.sun.tools.javac.code.Effect;
import com.sun.tools.javac.code.Effects;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;

import edu.illinois.dpjizer.effect.core.constraint.ReadConstraint;
import edu.illinois.dpjizer.effect.core.constraint.ReadWriteConstraint;
import edu.illinois.dpjizer.effect.core.constraint.WriteConstraint;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class EffectUtils {
	public static Effect toEffect(ReadConstraint constraint) {
		return new Effect.ReadEffect(null, constraint.getRPL(), false, false);
	}

	public static Effect toEffect(WriteConstraint constraint) {
		return new Effect.WriteEffect(null, constraint.getRPL(), false, false);
	}

	public static Effect toEffect(ReadWriteConstraint constraint) {
		if (constraint instanceof ReadConstraint)
			return toEffect((ReadConstraint) constraint);
		else if (constraint instanceof WriteConstraint)
			return toEffect((WriteConstraint) constraint);
		else
			return null;
	}

	public static Effects inEnvironment(Effects effects, Resolve rs, Env<AttrContext> env) {
		return effects.inEnvironment(rs, env, true);
	}

	/**
	 * @return Is "left" a subeffect of "right"?
	 */
	public static boolean isSubeffectOf(Effect left, Effect right) {
		return left.isSubeffectOf(right);
	}
}
