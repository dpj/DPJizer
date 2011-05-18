/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.visitors;

import static com.sun.tools.javac.code.Flags.DEPRECATED;
import static com.sun.tools.javac.code.Flags.PROPRIETARY;
import static com.sun.tools.javac.code.Flags.STATIC;
import static com.sun.tools.javac.code.Kinds.EFFECT;
import static com.sun.tools.javac.code.Kinds.ERR;
import static com.sun.tools.javac.code.Kinds.MTH;
import static com.sun.tools.javac.code.Kinds.NIL;
import static com.sun.tools.javac.code.Kinds.PCK;
import static com.sun.tools.javac.code.Kinds.RPL_ELT;
import static com.sun.tools.javac.code.Kinds.TYP;
import static com.sun.tools.javac.code.Kinds.VAL;
import static com.sun.tools.javac.code.Kinds.VAR;
import static com.sun.tools.javac.code.TypeTags.ARRAY;
import static com.sun.tools.javac.code.TypeTags.CLASS;
import static com.sun.tools.javac.code.TypeTags.ERROR;
import static com.sun.tools.javac.code.TypeTags.TYPEVAR;

import com.sun.tools.javac.code.Effects;
import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.ForAll;
import com.sun.tools.javac.code.dpjizer.constraints.Constraints;
import com.sun.tools.javac.code.dpjizer.constraints.RegionVarElt;
import com.sun.tools.javac.code.dpjizer.constraints.RegionVarEltSymbol;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.DPJRegionPathList;
import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;


/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class DPJizerAttr extends Attr {

	Constraints constraints;

	public static Attr instance(Context context, Constraints constraints) {
		Attr instance = context.get(attrKey);
		if (instance == null)
			instance = new DPJizerAttr(context, constraints);
		return instance;
	}

	protected DPJizerAttr(Context context, Constraints constraints) {
		super(context);
		context.put(attrKey, this);
		this.constraints = constraints;
	}

	protected boolean hasNonDefaultValue(JCIdent indexVar) {
		return (indexVar != null) && (!indexVar.toString().equals("_"));
	}

	protected RPL getNewRPL() {
		Env<AttrContext> topLevelEnv = enter.getTopLevelEnv(env.toplevel);

		RegionVarEltSymbol regionVarSym = new RegionVarEltSymbol(STATIC, Name.fromString(names, new String("Pi" + RegionVarEltSymbol.numIDs)),
				topLevelEnv.info.scope.owner);

		return new RPL(new RegionVarElt(regionVarSym)/* , constraints */);
	}

	// protected IndexVarSymbol getNewIndexVar(Scope scope) {
	// return new IndexVarSymbol(0, Name.fromString(names, IndexVarCounter
	// .getNextIndexVarName()), syms.intType, scope.owner);
	// }

	/**
	 * Determine type of identifier or select expression and check that (1) the
	 * referenced symbol is not deprecated (2) the symbol's type is safe (@see
	 * checkSafe) (3) if symbol is a variable, check that its type and kind are
	 * compatible with the prototype and protokind. (4) if symbol is an instance
	 * field of a raw type, which is being assigned to, issue an unchecked
	 * warning if its type changes under erasure. (5) if symbol is an instance
	 * method of a raw type, issue an unchecked warning if its argument types
	 * change under erasure. If checks succeed: If symbol is a constant, return
	 * its constant type else if symbol is a method, return its result type
	 * otherwise return its type. Otherwise return errType.
	 * 
	 * @param tree
	 *            The syntax tree representing the identifier
	 * @param site
	 *            If this is a select, the type of the selected expression,
	 *            otherwise the type of the current class.
	 * @param sym
	 *            The symbol representing the identifier.
	 * @param env
	 *            The current environment.
	 * @param pkind
	 *            The set of expected kinds.
	 * @param pt
	 *            The expected type.
	 */
	@Override
	protected Type checkId(JCTree tree, Type site, Symbol sym, Env<AttrContext> env, int pkind, Type pt, boolean useVarargs) {
		if (pt.isErroneous())
			return syms.errType;
		Type owntype; // The computed type of this identifier occurrence.
		switch (sym.kind) {
		case TYP:
			// For types, the computed type equals the symbol's type,
			// except for two situations:
			owntype = sym.type;
			if (owntype.tag == CLASS) {
				int numParams = owntype.tsym.type.getRegionParams().size();
				Type ownOuter = owntype.getEnclosingType();

				// (a) If the symbol's type is parameterized, erase it
				// because no type parameters were given.
				// We recover generic outer type later in visitTypeApply.
				if (owntype.tsym.type.getTypeArguments().nonEmpty() || owntype.tsym.type.getRegionParams().nonEmpty()) {
					List<RegionParameterSymbol> regionParams = owntype.getRegionParams();
					owntype = eraseType(owntype);
					// Don't erase the region params!
					((ClassType) owntype).rgnparams_field = regionParams;
					/* DPJIZER:Begin modification */
					List<RPL> defaultRegions = insertDefaultRegions(env, numParams);
					((ClassType) owntype).rgnactuals_field = defaultRegions;
					/* DPJIZER:End modification */
				}

				// (b) If the symbol's type is an inner class, then
				// we have to interpret its outer type as a superclass
				// of the site type. Example:
				//
				// class Tree<A> { class Visitor { ... } }
				// class PointTree extends Tree<Point> { ... }
				// ...PointTree.Visitor...
				//
				// Then the type of the last expression above is
				// Tree<Point>.Visitor.
				else if (ownOuter.tag == CLASS && site != ownOuter) {
					Type normOuter = site;
					if (normOuter.tag == CLASS)
						normOuter = types.asEnclosingSuper(site, ownOuter.tsym);
					if (normOuter == null) // perhaps from an import
						normOuter = eraseType(ownOuter);
					if (normOuter != ownOuter)
						owntype = new ClassType(normOuter, List.<Type> nil(), List.<RegionParameterSymbol> nil(), List.<Effects> nil(), owntype.tsym);
				}
			}
			break;
		case VAR:
			VarSymbol v = (VarSymbol) sym;
			// Test (4): if symbol is an instance field of a raw type,
			// which is being assigned to, issue an unchecked warning if
			// its type changes under erasure.
			if (allowGenerics && pkind == VAR && v.owner.kind == TYP && (v.flags() & STATIC) == 0 && (site.tag == CLASS || site.tag == TYPEVAR)) {
				Type s = types.asOuterSuper(site, v.owner);
				if (s != null && s.isRaw() && !types.isSameType(v.type, v.erasure(types))) {
					chk.warnUnchecked(tree.pos(), "unchecked.assign.to.var", v, s);
				}
			}
			// The computed type of a variable is the type of the
			// variable symbol, taken as a member of the site type.
			owntype = (sym.owner.kind == TYP && sym.name != names._this && sym.name != names._super) ? types.memberType(site, sym) : sym.type;

			if (env.info.tvars.nonEmpty() || env.info.rvars.nonEmpty()) {
				Type owntype1 = new ForAll(env.info.tvars, env.info.rvars, List.<Effects> nil(), owntype);
				for (List<Type> l = env.info.tvars; l.nonEmpty(); l = l.tail)
					if (!owntype.contains(l.head)) {
						log.error(tree.pos(), "undetermined.type", owntype1);
						owntype1 = syms.errType;
					}
				owntype = owntype1;
			}

			// Substitutions required by DPJ type system
			if (tree instanceof JCFieldAccess) {
				JCFieldAccess fa = (JCFieldAccess) tree;
				RPL rpl = exprToRPL(fa.selected);
				if (rpl != null) {
					owntype = types.substForThis(owntype, rpl);
				}
			}

			// If the variable is a constant, record constant value in
			// computed type.
			if (v.getConstValue() != null && isStaticReference(tree))
				owntype = owntype.constType(v.getConstValue());

			if (pkind == VAL) {
				owntype = capture(owntype); // capture "names as expressions"
			}
			break;
		case MTH: {
			JCMethodInvocation app = (JCMethodInvocation) env.tree;
			owntype = checkMethod(site, sym, env, app.args, pt.getParameterTypes(), pt.getTypeArguments(), pt.getRegionActuals(),
					pt.getEffectArguments(), env.info.varArgs);
			break;
		}
		case PCK:
		case ERR:
		case RPL_ELT:
		case EFFECT:
			owntype = sym.type;
			break;
		default:
			throw new AssertionError("unexpected kind: " + sym.kind + " in tree " + tree);
		}

		// Test (1): emit a `deprecation' warning if symbol is deprecated.
		// (for constructors, the error was given when the constructor was
		// resolved)
		if (sym.name != names.init && (sym.flags() & DEPRECATED) != 0 && (env.info.scope.owner.flags() & DEPRECATED) == 0
				&& sym.outermostClass() != env.info.scope.owner.outermostClass())
			chk.warnDeprecated(tree.pos(), sym);

		if ((sym.flags() & PROPRIETARY) != 0)
			log.strictWarning(tree.pos(), "sun.proprietary", sym);

		// Test (3): if symbol is a variable, check that its type and
		// kind are compatible with the prototype and protokind.
		Type checkedType = check(tree, owntype, sym.kind, pkind, pt);
		return checkedType;
	}

	private Type eraseType(Type type) {
		// Clear the erasure cache.
		type.tsym.erasure_field = null;
		Type erasedType = types.erasure(type);

		return erasedType;
	}

	/*
	 * Insert fresh region variables instead of Root as default region
	 * arguments.
	 */
	protected List<RPL> insertDefaultRegions(Env<AttrContext> env, int numParams) {
		// Put the default region of ROOT in for each param
		// position. If this type is subject to a region apply, the
		// ROOT's will get replaced. Otherwise, we have a type
		// instantiated with ROOT's. This is the DPJ equivalent of a
		// Java "raw type."
		ListBuffer<RPL> buf = ListBuffer.lb();
		for (int i = 0; i < numParams; ++i) {
			buf.append(getNewRPL());
		}
		return buf.toList();
	}

	@Override
	public void visitTypeArray(JCArrayTypeTree tree) {
		VarSymbol indexVar = null;
		Env<AttrContext> localEnv = env.dup(tree, env.info.dup(env.info.scope.dup()));
		// If we're in an initializer, we need the actual scope
		// so we can add params to it
		Scope scope = enter.enterScope(localEnv).getActualScope();
		if (tree.indexParam != null) {
			indexVar = new VarSymbol(0, tree.indexParam.name, syms.intType, scope.owner);
			tree.indexParam.sym = indexVar;
			scope.enter(indexVar);
		}
		if (tree.rpl != null) {
			attribTree(tree.rpl, localEnv, NIL, Type.noType);
		}
		Type etype = attribType(tree.elemtype, localEnv);
		Type type = new ArrayType(etype, null, indexVar, syms.arrayClass);
		result = check(tree, type, TYP, pkind, pt);
		localEnv.info.scope.leave();
		if (tree.rpl != null) {
			((ArrayType) tree.type).rpl = tree.rpl.rpl;
		}
		/* DPJIZER:Begin modification */
		else {
			((ArrayType) tree.type).rpl = getNewRPL();
		}
		/* DPJIZER:End modification */
	}

	@Override
	public void visitNewArray(JCNewArray tree) {
		Env<AttrContext> oldEnv = env;
		Type owntype = syms.errType;
		Type elemtype;
		if (tree.elemtype != null) {
			List<DPJRegionPathList> rpls = tree.rpls;
			List<JCIdent> indexVars = tree.indexVars;
			ListBuffer<RPL> rplBuf = ListBuffer.lb();
			ListBuffer<VarSymbol> indexBuf = ListBuffer.lb();
			Env<AttrContext> localEnv = env.dup(tree, env.info.dup(env.info.scope.dup()));
			// If we're in an initializer, we need the actual scope
			// so we can add params to it
			Scope scope = enter.enterScope(localEnv).getActualScope();
			while (!rpls.isEmpty()) {
				RPL rpl = null;
				VarSymbol indexVar = null;
				/* DPJIZER:Begin modification */
				if (indexVars.head != null) {
					indexVar = new VarSymbol(0, indexVars.head.name, syms.intType, scope.owner);
					indexVars.head.sym = indexVar;
					scope.enter(indexVar);
				}
				/* DPJZIER:End modification */
				if (rpls.head != null) {
					attribTree(rpls.head, localEnv, NIL, Type.noType);
					rpl = rpls.head.rpl;
				}
				/* DPJIZER:Begin modification */
				else {
					rpl = getNewRPL();
				}
				/* DPJIZER:End modification */
				rplBuf.append(rpl);
				indexBuf.append(indexVar);
				rpls = rpls.tail;
				indexVars = indexVars.tail;
			}
			elemtype = attribType(tree.elemtype, localEnv);
			chk.validate(tree.elemtype);
			owntype = elemtype;
			List<RPL> rplSyms = rplBuf.toList().reverse();
			List<VarSymbol> indexSyms = indexBuf.toList().reverse();
			for (List<JCExpression> l = tree.dims; l.nonEmpty(); l = l.tail) {
				attribExpr(l.head, env, syms.intType);
				owntype = new ArrayType(owntype, rplSyms.head, indexSyms.head, syms.arrayClass);
				rplSyms = rplSyms.tail;
				indexSyms = indexSyms.tail;
			}
			localEnv.info.scope.leave();
		} else {
			// we are seeing an untyped aggregate { ... }
			// this is allowed only if the prototype is an array
			if (pt.tag == ARRAY) {
				elemtype = types.elemtype(pt);
			} else {
				if (pt.tag != ERROR) {
					log.error(tree.pos(), "illegal.initializer.for.type", pt);
				}
				elemtype = syms.errType;
			}
		}
		if (tree.elems != null) {
			attribExprs(tree.elems, env, elemtype);
			/* DPJIZER:Begin modification */
			owntype = new ArrayType(elemtype, getNewRPL(), null, syms.arrayClass);
			/* DPJZIER:End modification */
		}
		if (!types.isReifiable(elemtype))
			log.error(tree.pos(), "generic.array.creation");
		result = check(tree, owntype, VAL, pkind, pt);
		env = oldEnv;
	}

}
