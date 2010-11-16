/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.parser;

import static com.sun.tools.javac.parser.Token.GT;
import static com.sun.tools.javac.parser.Token.GTGT;
import static com.sun.tools.javac.parser.Token.LBRACE;
import static com.sun.tools.javac.parser.Token.LBRACKET;
import static com.sun.tools.javac.parser.Token.LT;
import static com.sun.tools.javac.parser.Token.LTLT;
import static com.sun.tools.javac.parser.Token.NUMBER;
import static com.sun.tools.javac.parser.Token.RBRACKET;

import com.sun.tools.javac.parser.EndPosParser;
import com.sun.tools.javac.parser.Lexer;
import com.sun.tools.javac.parser.Parser;
import com.sun.tools.javac.tree.JCTree.DPJRegionPathList;
import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;

import edu.illinois.dpjizer.region.core.types.IndexVarCounter;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class DPJizerParser extends Parser {

	public static class DPJizerFactory extends Factory {

		public static Factory instance(Context context) {
			Factory instance = context.get(parserFactoryKey);
			if (instance == null)
				instance = new DPJizerFactory(context);
			return instance;
		}

		protected DPJizerFactory(Context context) {
			super(context);
			context.put(parserFactoryKey, this);
		}

		/**
		 * Create a new Parser.
		 * 
		 * @param S
		 *            Lexer for getting tokens while parsing
		 * @param keepDocComments
		 *            true if javadoc comments should be kept
		 * @param genEndPos
		 *            true if end positions should be generated
		 */
		@Override
		public Parser newParser(Lexer S, boolean keepDocComments, boolean genEndPos) {
			if (!genEndPos)
				return new DPJizerParser(this, S, keepDocComments);
			else
				return new EndPosParser(this, S, keepDocComments);
		}

	}

	protected DPJizerParser(Factory fac, Lexer S, boolean keepDocComments) {
		super(fac, S, keepDocComments);
	}

	protected String nextIndexVarName() {
		return IndexVarCounter.getNextIndexVarName();
	}

	@Override
	protected JCArrayTypeTree bracketsOptCont(JCExpression t, int pos, boolean regionsAllowed) {
		accept(RBRACKET);
		DPJRegionPathList rpl = null;
		JCIdent indexParam = null;
		if (regionsAllowed) {
			if (S.token() == LT) {
				S.nextToken();
				rpl = rpl();
				accept(GT);
			} else if (S.token() == LTLT) {
				// Deprecated syntax
				S.nextToken();
				rpl = rpl();
				accept(GTGT);
			}
			if (S.token() == NUMBER) {
				S.nextToken();
				indexParam = toP(F.at(S.pos()).Ident(ident()));
			} else {
				indexParam = toP(F.at(S.pos()).Ident(names.fromString(nextIndexVarName())));
			}
		}
		t = bracketsOpt(t, regionsAllowed);
		return toP(F.at(pos).TypeArray(t, rpl, indexParam));
	}

	@Override
	protected JCExpression arrayCreatorRest(int newpos, JCExpression elemtype) {
		accept(LBRACKET);
		if (S.token() == RBRACKET) {
			accept(RBRACKET);
			elemtype = bracketsOpt(elemtype, true);
			if (S.token() == LBRACE) {
				return arrayInitializer(newpos, elemtype);
			} else {
				return syntaxError(S.pos(), "array.dimension.missing");
			}
		} else {
			ListBuffer<JCExpression> dims = new ListBuffer<JCExpression>();
			ListBuffer<DPJRegionPathList> rpls = new ListBuffer<DPJRegionPathList>();
			ListBuffer<JCIdent> params = new ListBuffer<JCIdent>();
			ListBuffer<JCIdent> indexVars = new ListBuffer<JCIdent>();
			dims.append(expression());
			accept(RBRACKET);
			if (S.token() == LT) {
				S.nextToken();
				rpls.append(rpl());
				accept(GT);
			} else if (S.token() == LTLT) {
				// Deprecated syntax
				S.nextToken();
				rpls.append(rpl());
				accept(GTGT);
			} else {
				rpls.append(null);
				params.append(null);
			}
			if (S.token() == NUMBER) {
				S.nextToken();
				indexVars.append(toP(F.at(S.pos()).Ident(ident())));
			} else {
				indexVars.append(toP(F.at(S.pos()).Ident(names.fromString(nextIndexVarName()))));
				// indexVars.append(null);
			}
			while (S.token() == LBRACKET) {
				int pos = S.pos();
				S.nextToken();
				if (S.token() == RBRACKET) {
					elemtype = bracketsOptCont(elemtype, pos, true);
				} else {
					dims.append(expression());
					accept(RBRACKET);
					if (S.token() == LT) {
						S.nextToken();
						rpls.append(rpl());
						accept(GT);
					} else if (S.token() == LTLT) {
						// Deprecated syntax
						S.nextToken();
						rpls.append(rpl());
						accept(GTGT);
					} else {
						params.append(null);
						rpls.append(null);
					}
					if (S.token() == NUMBER) {
						S.nextToken();
						indexVars.append(toP(F.at(S.pos()).Ident(ident())));
					} else {
						indexVars.append(toP(F.at(S.pos()).Ident(names.fromString(nextIndexVarName()))));
						// indexVars.append(null);
					}
				}
			}
			JCNewArray result = toP(F.at(newpos).NewArray(elemtype, dims.toList(), rpls.toList(), null));
			result.indexVars = indexVars.toList();
			return result;
		}
	}

}
