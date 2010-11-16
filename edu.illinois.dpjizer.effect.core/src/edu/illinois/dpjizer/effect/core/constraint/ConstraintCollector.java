/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import java.util.HashMap;
import java.util.Map;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.Check;
import com.sun.tools.javac.comp.EnvScanner;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import edu.illinois.dpjizer.effect.core.rpl.RPLUtils;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 * 
 */
public class ConstraintCollector extends EnvScanner {

	protected static final Context.Key<ConstraintCollector> constraintCollectorKey = new Context.Key<ConstraintCollector>();

	Map<Name, Tree> symbolTable = new HashMap<Name, Tree>();
	ConstraintRepository constraints;
	Check check;
	Types types;
	Attr attr;
	MethodEnvironments methodEnvs;

	public ConstraintRepository getConstraints() {
		return constraints;
	}

	public static ConstraintCollector instance(Context context) {
		ConstraintCollector instance = context.get(constraintCollectorKey);
		if (instance == null)
			instance = new ConstraintCollector(context);
		return instance;
	}

	protected ConstraintCollector(Context context) {
		super(context);
		context.put(constraintCollectorKey, this);
		check = Check.instance(context);
		types = Types.instance(context);
		attr = Attr.instance(context);
		methodEnvs = MethodEnvironments.instance(Resolve.instance(context));
		constraints = new ConstraintRepository(methodEnvs);
	}

	private String getSourceFileName() {
		return parentEnv.toplevel.getSourceFile().getName();
	}

	private MethodSymbol getEnclosingMethodSymbol() {
		JCMethodDecl enclMethod = getEnclosingMethodDecl();
		if (enclMethod == null)
			return null;
		MethodSymbol enclosingMethod = enclMethod.sym;
		return enclosingMethod;
	}

	private JCMethodDecl getEnclosingMethodDecl() {
		return parentEnv.enclMethod;
	}

	private String getEnclosingMethodName() {
		return getQualifiedName(getEnclosingMethodSymbol());
	}

	private String getQualifiedName(MethodSymbol method) {
		if (method == null)
			return null;
		// return method.owner.getQualifiedName() + "." +
		// method.getQualifiedName().toString();
		return method.owner.getQualifiedName() + "." + method.toString();
	}

	// private Method getEnclosingMethod() {
	// return new Method(getQualifiedName(getEnclosingMethodSymbol()),
	// getSourceFileName(), getEnclosingMethodDecl().getStartPosition());
	// }

	/** Return the method symbol overridden by the method symbol s. */
	private Symbol overridenBy(Symbol s) {
		if (s.kind != com.sun.tools.javac.code.Kinds.MTH || s.isStatic())
			return null;
		MethodSymbol m = (MethodSymbol) s;
		TypeSymbol owner = (TypeSymbol) m.owner;
		for (Type sup : types.closure(owner.type)) {
			if (sup == owner.type)
				continue; // skip "this"
			com.sun.tools.javac.code.Scope scope = sup.tsym.members();
			for (com.sun.tools.javac.code.Scope.Entry e = scope.lookup(m.name); e.scope != null; e = e.next()) {
				if (!e.sym.isStatic() && m.overrides(e.sym, owner, types, true))
					return e.sym;
			}
		}
		return null;
	}

	@Override
	public void visitMethodDef(JCMethodDecl tree) {
		super.visitMethodDef(tree);

		String qualifiedMethodName = getQualifiedName(tree.sym);
		methodEnvs.setMethodEnv(qualifiedMethodName, childEnvs.head);

		constraints.addMethod(new Method(qualifiedMethodName, getSourceFileName(), effectInsertionOffset(tree)));

		if (tree.body != null)
			constraints.addReadWriteConstraints(qualifiedMethodName, methodEnvs.inEnvironment(tree.body.effects, qualifiedMethodName));

		if (check.isOverrider(tree.sym)) {
			Symbol overridenSymbol = overridenBy(tree.sym);
			if (overridenSymbol != null) {
				MethodSymbol overridenMethodSymbol = (MethodSymbol) overridenSymbol;
				Type overriderAsInSuper = types.asOuterSuper(tree.sym.owner.type, overridenMethodSymbol.owner);
				List<RegionParameterSymbol> superclassRegionParams = overridenMethodSymbol.owner.type.allrgnparams();
				List<RPL> subclassRegionParams = overriderAsInSuper.allrgnactuals();
				List<RegionParameterSymbol> superclassMethodRegionParams = overridenMethodSymbol.rgnParams;
				List<RegionParameterSymbol> subclassMethodRegionParams = tree.sym.rgnParams;

				// System.out.println(qualifiedMethodName + " overrides " +
				// getQualifiedName(overridenMethodSymbol));
				// System.out.println("superclass: " +
				// overridenMethodSymbol.owner.type.toString());
				// System.out.println("subclass  : " +
				// tree.sym.owner.type.toString());
				// System.out.println("asMemberOf: " +
				// overriderAsInSuper.toString());
				// System.out.println("region params  (" +
				// superclassRegionParams.size() + "): " +
				// superclassRegionParams);
				// System.out.println("actual regions (" +
				// subclassRegionParams.size() + "): " + subclassRegionParams);
				constraints.addOverrideConstraint(qualifiedMethodName, getQualifiedName(overridenMethodSymbol), superclassRegionParams,
						subclassRegionParams, superclassMethodRegionParams, subclassMethodRegionParams);
			}
		}

		// Make the set of invocation constraints valid in the environment of
		// this method signature.
		// FIXME: There is a bug here. We might collect the invocation
		// constraints later and don't apply inEnv on them anymore. We should
		// either compute the environments of each method in a separate pass or
		// project the collected constraints into the corresponding environments
		// in the next pass.
		// Why? Isn't it supposed to have traversed the children at this point?
		if (constraints.methodInvocationsOf(qualifiedMethodName) != null) {
			Constraints inEnvConstraints = constraints.methodInvocationsOf(qualifiedMethodName).inEnvironment(methodEnvs, qualifiedMethodName);
			constraints.updateMethodInvocations(qualifiedMethodName, inEnvConstraints);
		}
	}

	private int effectInsertionOffset(JCMethodDecl tree) {
		int effectInsertionPoint = 0;
		if (tree.getBody() != null) {
			effectInsertionPoint = tree.getBody().getStartPosition();
		} else {
			// TODO: A hack to get the end position of a body less method.
			effectInsertionPoint = tree.getStartPosition() + tree.toString().length() - 2;
		}
		if (tree.getThrows() != null && tree.getThrows().nonEmpty())
			effectInsertionPoint = tree.getThrows().head.getStartPosition() - "throws ".length();
		return effectInsertionPoint;
	}

	/**
	 * @see com.sun.tools.javac.comp.CheckEffects#visitApply(JCMethodInvocation)
	 */
	public void visitApply(JCMethodInvocation tree) {
		super.visitApply(tree);

		if (getEnclosingMethodDecl() == null)
			return;

		// If the method is not a member of a class e.g. it's a member of an
		// enum ignore it.
		if (!(getEnclosingMethodDecl().sym.owner instanceof ClassSymbol))
			return;

		// Accumulate the effect of invoking m
		MethodSymbol sym = tree.getMethodSymbol();
		if (sym != null) {
			List<RegionParameterSymbol> classRegionParams = null;
			List<RPL> classRegionActuals = null;
			ClassType ct = null;

			// Collect class region params
			if (tree.meth instanceof JCFieldAccess) {
				JCFieldAccess fa = (JCFieldAccess) tree.meth;
				if (fa.selected.type instanceof ClassType) {
					ct = (ClassType) fa.selected.type;
				} else
					// What constraint should be generated for the expression
					// "first.toString()" in 11-string-matching/DPJPair.java
					return;
			} else
				ct = (ClassType) getEnclosingMethodDecl().sym.owner.type;

			if (ct == null)
				throw new NullPointerException("ct is null");

			Type derivedAsInBase = types.asOuterSuper(ct, sym.owner);
			if (derivedAsInBase != null) {
				List<RegionParameterSymbol> superclassRegionParams = sym.owner.type.allrgnparams();
				List<RPL> subclassRegionParams = derivedAsInBase.allrgnactuals();

				// List<RegionParameterSymbol> superclassMethodRegionParams =
				// sym.rgnParams;
				// List<RegionParameterSymbol> subclassMethodRegionParams =
				// getEnclosingMethodDecl().sym.rgnParams;

				classRegionParams = superclassRegionParams;
				classRegionActuals = subclassRegionParams;

				// System.out.println(sym.owner + " asIn " +
				// getEnclosingMethodDecl().sym.owner.type + " --> " +
				// derivedAsInBase + ": { "
				// + superclassRegionParams + "-->" + subclassRegionParams +
				// " }");
			}

			// if (derivedAsInBase == null) {
			// // classRegionParams = ct.tsym.type.getRegionParams();
			// classRegionParams = sym.owner.type.getRegionParams();
			// classRegionActuals = ct.getRegionActuals();
			// }

			// Collect method region params
			List<RegionParameterSymbol> methodRegionParams = sym.rgnParams;
			List<RPL> methodRegionActuals = null;

			if (methodRegionParams != null && tree.mtype != null) {
				methodRegionActuals = tree.mtype.regionActuals;
			}
			List<RegionParameterSymbol> regionParams = null;
			if (methodRegionParams == null)
				regionParams = classRegionParams;
			else if (classRegionParams == null)
				regionParams = methodRegionParams;
			else
				regionParams = classRegionParams.appendList(methodRegionParams);

			List<RPL> regionActuals = null;
			if (methodRegionActuals == null)
				regionActuals = classRegionActuals;
			else if (classRegionActuals == null)
				regionActuals = methodRegionActuals;
			else
				regionActuals = classRegionActuals.appendList(methodRegionActuals);

			// Store the substitution for this
			RPL receiver = null;
			if (tree.meth instanceof JCFieldAccess) {
				JCFieldAccess fa = (JCFieldAccess) tree.meth;
				receiver = RPLUtils.toRPL(fa.selected, attr);
				// if (fa.selected instanceof JCIdent) {
				// JCIdent id = (JCIdent) fa.selected;
				// if (id.sym instanceof VarSymbol) {
				// receiver = (VarSymbol) id.sym;
				// }
				// }
			}

			// TODO: Store the substitution for index exprs

			// System.out.println(getEnclosingMethodName());
			constraints.addInvokeConstraint(getEnclosingMethodName(), getQualifiedName(sym), regionParams, regionActuals, receiver);
		}
	}
}
