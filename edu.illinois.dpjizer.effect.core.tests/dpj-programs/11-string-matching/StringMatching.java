/**
 * Parallel string matching
 * Adapted from Gibbons and Rytter, Efficient Parallel Algorithms,
 * pp. 220 ff.
 * @author Robert L. Bocchino Jr.
 * Created June 2008
 *
 * HOW THE ALGORITHM WORKS
 *
 * Let s be an arbitrary string over some alphabet.  Then s is either
 * periodic or aperiodic.  It is periodic if it equals some "short"
 * pattern p repeated over and over, possibly truncated at the end.
 * Here, "short" means |p| <= 2^k < |s|, where 2^k is the largest
 * power of 2 less than |s|.  For example, s = abaabaab is periodic
 * with p = aba and k = 2.  If a string is not periodic, then it is
 * aperiodic.  For example, abaababa is aperiodic, because the
 * shortest p is abaabab, which is too long.
 *
 * If a pattern is aperiodic, then we can use that fact to match it
 * efficiently against some text.  The idea is that for pairs of
 * candidate positions that are close together, we can always
 * eliminate at least one, because otherwise the string would overlap
 * itself in a way that violates aperiodicity.  We divide the text
 * string into segments, proceeding in iterations of larger and larger
 * segments.  At each iteration, we use this idea to eliminate all but
 * one of the candidate positions from each segment.  We then check
 * those positions for a match.
 *
 * The overall algorithm works as follows.  The input is some text and
 * a pattern to match against it.  First, we compute "witnesses" to
 * the aperiodicity of the pattern, i.e., an array of size 2^k such
 * that the 0 position is -1 and every other position i is -1 if the
 * pattern transposed to that point might match to the end of the
 * pattern, otherwise some index w such that pattern[i] =/=
 * pattern[i+w].
 *
 * If every position in the witness array other than the zero position
 * is greater than -1, then the pattern is aperiodic, and we can use
 * aperiodic string matching to match it efficiently against the text,
 * as suggested above.  Otherwise, the pattern is periodic, i.e.,
 * pattern = p^n p' for some |p| < 2^k < |pattern| and |p'| < |p|.  In
 * that case, we use the fact that the string p p' is aperiodic.  We
 * match it against the text, using the aperiodic matching algorithm.
 * Then we use recursive doubling to count the number of repetitions
 * to the right at each index where this pattern appears.  The pattern
 * matches at the positions where the count is sufficiently large.
 *
 * The algorithm requires the pattern length to be a power of two.  We
 * extend it to arbitrary patterns by padding the input pattern to the
 * nearest power of two.  The padding character is a sentinel that
 * "matches everything."  It must be some character that cannot appear
 * in any valid input string.
 */

import DPJRuntime.*;

class StringMatching {

	region SM;

	/**
	 * Log base 2 of the pattern length
	 */
	public int logPatLen in SM;

	/**
	 * True pattern length, in case we used padding
	 */
	public int truePatLen in SM;

	/**
	 * Period of the pattern; 0 if the pattern is aperiodic
	 */
	public int period in SM  = 0;

	/**
	 * Character to use for padding; set to a printable character for
	 * debugging
	 */
	public static final char PAD_CHAR = '@';

	public StringMatching() {}

	/**
	 * Match pattern against text, adding the indices where a match
	 * occurred to result.
	 *
	 * @param text    Text to match against
	 * @param pattern Pattern to match against text
	 * @param result  Set of indices where match occurred
	 */
	public <region In, Out | In:* # Out:*, Out # SM>
	void match(char[]<In> text, char[]<In> pattern,
			DPJSet<Integer,Out> result) {
		match(new DPJArrayChar<In>(text),
				new DPJArrayChar<In>(pattern),
				result);
	}

	public <region In, Out | In:* # Out:*, Out # SM>
	void match(DPJArrayChar<In> text, 
			DPJArrayChar<In> pattern,
			DPJSet<Integer,Out> result) {
		logPatLen = DPJUtils.log2(pattern.length);
		truePatLen = pattern.length;
		pattern = pad(pattern);

		// Compute the pattern witnesses
		region PW;
		DPJArrayInt<PW> patternWitnesses =
			new DPJArrayInt<PW>(pattern.length >> 1);
		computePatternWitnesses(pattern, patternWitnesses);
		if (period == 0) {
			matchAperiodic(text, pattern, patternWitnesses, result);
		} else {
			matchPeriodic(text, pattern, patternWitnesses, result);
		}
	}

	/**
	 * Pad pattern to length that is a power of 2
	 *
	 * @param pattern Pattern of arbitrary length
	 * @returns       Pattern padded if necessary
	 */
	public <region Pat>DPJArrayChar<Pat> 
	pad(DPJArrayChar<Pat> pattern) 
	//	reads SM, Pat:* writes Pat 
	{
		if (pattern.length != 1 << logPatLen) {
			int padLength = 1 << ++logPatLen;
			DPJArrayChar<Pat> padded = 
				new DPJArrayChar<Pat>(padLength);
			for (int i = 0; i < padLength; ++i) {
				padded.put(i, (i < pattern.length) ? pattern.get(i) : PAD_CHAR);
			}
			pattern = padded;
		}
		return pattern;
	}

	/**
	 * Compute match indices for an aperiodic pattern
	 */
	public 
	<region Text, Pat, PW, Out | 
	Text:* # Out, Pat:* # Out, Out # SM>
	void matchAperiodic(DPJArrayChar<Text> text, 
			DPJArrayChar<Pat> pattern,
			DPJArrayInt<PW> patternWitnesses,
			DPJSet<Integer,Out> result) 
	//	reads SM, Text:*, Pat:*, PW:* writes Out 
	{

		// Compute the match witnesses
		region MW;
		DPJArrayInt<MW> matchWitnesses =
			new DPJArrayInt<MW>(text.length);
		computeMatchWitnesses(text, pattern, patternWitnesses,
				matchWitnesses);

		// Search for the pattern in parallel over text blocks of size
		// pattern.length / 2
		int len = pattern.length >> 1;
				if (len == 0) len = 1;
				int numSegs = text.length / len;
				if (numSegs * len != text.length) ++numSegs;
				foreach (int i in 0, numSegs) {
					int start = i * len;
					int matchIdx = 0;
					if (len <= text.length - start) {
						matchIdx = checkSegment(text, pattern,
								matchWitnesses.subarray(start, len));
					} else {
						matchIdx = checkSegment(text, pattern,
								matchWitnesses.subarray(start, text.length - start));
					}
					// Commutative
					if (matchIdx >= 0) {
						result.add(matchIdx);
					}
				}
	}

	/**
	 * Compute match indices for a periodic pattern
	 *
	 * @param text              The text to match against
	 * @param pattern           Pattern to match against text
	 * @param patternWitnesses  Witnesses to aperiodicity of pattern
	 * @param result            Positions where match occurred
	 */
	public <region Text,Pat,PW,Out | 
	Text:* # Out:*, Pat:* # Out:*, PW # Out, Out # SM>
	void matchPeriodic(DPJArrayChar<Text> text, 
			DPJArrayChar<Pat> pattern,
			DPJArrayInt<PW> patternWitnesses,
			DPJSet<Integer,Out> result)
	//	writes Text:*, Pat:*, PW:*, Out, SM 
	{
		//reads Text:*, Pat:*, PW:* writes Out, SM, Pat:[?] {

		// Create a new set to hold the temporary answer
		DPJSet<Integer,Out> tmpResult = 
			new DPJHashSet<Integer,Out>();

		// Compute the last periodic segment of the pattern plus any
		// remainder (guaranteed to be aperiodic)
		int len = period + truePatLen % period;
		int numRepeat = truePatLen / period;
		pattern = pattern.subarray(0, len);
		logPatLen = DPJUtils.log2(len);
		truePatLen = len;
		pattern = pad(pattern);
		patternWitnesses = patternWitnesses.subarray(0, pattern.length >> 1);

		// Match last periodic segment + remainder against text;
		matchAperiodic(text, pattern, patternWitnesses, tmpResult);

		// Use occurrences of last periodic segment + remainder to
		// locate pattern, i.e., period or more successive occurrences
		// of (last periodic segment + pattern)
		region LocalIn, LocalOut;
		DPJSparseArray<Integer,Local:LocalIn> in = 
			new DPJSparseArray<Integer,Local:LocalIn>(text.length,0);
		DPJSparseArray<Integer,Local:LocalOut> out = 
			new DPJSparseArray<Integer,Local:LocalOut>(text.length,0);
		foreach(Integer I in tmpResult.iterator()) {
			out.put(I, 1);
		}

		// Recursive doubling
		DPJSparseArray<Integer,Local:*> myOut = null;
		for (int i = 1; i < numRepeat; i <<= 1) {
			myOut = in;
			doDoubling(out, in, text, i);

			i <<= 1;
			if (i >= numRepeat) break;
			myOut = out;
			doDoubling(in, out, text, i);
		}

		// Copy answer into result set
		foreach (int i in in.nonZeroIndices().iterator()) {
			if (myOut.get(i) >= numRepeat) {
				result.add(i);
			}
		}
	}

	private <region R1, R2, Text | R1 # R2>void doDoubling(DPJSparseArray<Integer,R1> in,
			DPJSparseArray<Integer,R2> out, 
			DPJArrayChar<Text> text, int i) 
	//	reads SM, R1 : *  writes R2 : * 
	{
		foreach (int j in in.nonZeroIndices().iterator()) {
			int num1 = in.get(j);
			if ((j < text.length - i*period)
					&& (num1 == i)) {
				int num2 = in.get(j+i*period);
				out.put(j, num1+num2);
			} else {
				out.put(j, num1);
			}
		}
	}

	/**
	 *  Compute witnesses to the aperiodicity of pattern.  For all i,
	 *  0 <= i < pattern.length/2, patternWitnesses[i] <
	 *  pattern.length-i.  Let j=patternWitnesses[i],
	 *  m=pattern.length-1.  If j >= 0, then pattern[j] !=
	 *  pattern[i+j], and therefore pattern[i..m] is not a prefix of
	 *  pattern.  If j < 0, then pattern[i..m] may (but need not be) a
	 *  prefix of pattern.
	 * 
	 *  @param pattern   The pattern for which we are computing
	 *                   witnesses
	 *  @param witnesses The witnesses
	 */
	public <region Pat, Wit | Pat:* # Wit:*>void 
	computePatternWitnesses(DPJArrayChar<Pat> pattern,
			DPJArrayInt<Wit> witnesses)
	//	reads Pat:* writes Wit:*, SM 
	{
		period = 0;
		initialize(witnesses, -1);
		int i = 0;
		while (i < logPatLen-1) {
			int idx = (1 << i);
			// Look for a candidate in witnesses[idx..2*idx-1]
			int candidate = 
				idx+getNegIdx(witnesses.subarray(idx, idx));
			if (candidate < 0) { 
				// No candidate in range
				++i; 
			} else {
				// Look for witness in next range
				int len = (1 << (i+2)) - candidate;
				DPJArrayChar<Pat> subarray =
					pattern.subarray(candidate, len);
				int witness = witness(pattern, subarray);
				witnesses.put(candidate, witness);
				if (witness >= 0) {
					// Aperiodic case
					if (i < logPatLen - 2) {
						idx = 1<<(i+1);
						int[]<Local> arr = new int[2]<Local>;
						arr[0] = idx;
						arr[1] = idx << 1;
						final DPJPartitionInt<Wit> segs =
							new DPJPartitionInt<Wit>(witnesses, arr);
						makeSparse(pattern, pattern, 
								segs.get(0), segs.get(1),
								i+1);
					} 
					++i;
				} else {
					// Periodic case
					int c = periodCont(pattern, candidate, i+2);
					if (c == logPatLen) {
						// Whole pattern is periodic; we're done
						period = candidate;
						return;
					}
					makeSparsePeriodic(pattern, witnesses,
							i,candidate,c);
					i = c-1;
				}
			}
		}
	}

	/**
	 * Compute witnesses to a mismatch of pattern and text.  For all
	 * i, 0 <= i <= text.length-pattern.length, let w =
	 * matchWitnesses[i] If w >=0, then pattern[j] != text[i+j], and
	 * therefore pattern does not match text at position i.  If w ==
	 * -1, then pattern may (but need not) match text at position i.
	 * After this computation, the "unknown" (-1) positions are
	 * sparse.
	 *
	 * @param text             The text to match against
	 * @param pattern          The pattern to match
	 * @param patternWitnesses Witnesses to aperiodicity of pattern
	 * @param matchWitnesses   Witnesses we are computing
	 */

	private <region Text, Pat, PW, MW | 
	Text:* # MW:*, Pat:* # MW:*, PW:* # MW:*> void
	computeMatchWitnesses(DPJArrayChar<Text> text,
			DPJArrayChar<Pat> pattern,
			DPJArrayInt<PW> patternWitnesses,
			DPJArrayInt<MW> matchWitnesses) 
	//	reads SM, Text:*, Pat:*, PW:* writes MW:* 
	{
		initialize(matchWitnesses, -1);
		makeSparse(text, pattern, patternWitnesses, 
				matchWitnesses, logPatLen - 1);
	}

	/**
	 * Examine a segment of matchWitnesses to find a position where
	 * the witness is -1, if any (there is guaranteed to be at most
	 * one).  If a -1 witness is found, check for a match.  If a match
	 * is found, return that position.  If no -1 position or no match
	 * is found, return -1.
	 */
	private <region Text,Pat,Wit>
	int checkSegment(DPJArrayChar<Text> text,
			DPJArrayChar<Pat> pattern,
			DPJArrayInt<Wit> witnessSeg)
	//	reads SM, Text:*, Pat:*, Wit:* 
	{
		// Look for index where witness is -1
		int idx = getNegIdx(witnessSeg);
		// If none, return failure
		if (idx < 0) return -1;
		idx += witnessSeg.start;
		// Otherwise check for pattern and return result
		return checkIdx(text, pattern, idx) ? idx : -1;
	}

	/**
	 * Iteratively make the "unknown" (-1) slots in matchWit sparse to
	 * the required sparsity
	 *
	 * @param text       The text to match against
	 * @param pattern    The pattern to match against the text
	 * @param patternWit Array of witnesses to the aperiodicity of
	 *                   pattern.  We use this to compute matchWit.
	 * @param matchWit   Array of witnesses to a mismatch between
	 *                   pattern and text.  This is what we are
	 *                   computing.
	 */
	protected <region Text, Pat, PW, MW | 
	Text:* # MW:*, Pat:* # MW:*, PW:* # MW:*>
	void makeSparse(DPJArrayChar<Text> text,
			DPJArrayChar<Pat> pattern,
			DPJArrayInt<PW> patternWit,
			DPJArrayInt<MW> matchWit,
			int sparsity) 
	//	reads Text:*, Pat:*, PW:* writes MW:* 
	{
		for (int k = 1; k <= sparsity; ++k) {
			makeKSparse(text, pattern, patternWit, matchWit, k);
		}
	}

	/**
	 * Use patternWit to make matchWit sparse to a sparsity of k.
	 *
	 * @param text       Text to match against
	 * @param pattern    Pattern to match against text
	 * @param patternWit Witnesses to aperiodicity of pattern
	 * @param matchWit   Witnesses to mismatch of pattern and text
	 */
	protected <region Text,Pat,PW,MW | 
	Text:* # MW:*, Pat:* # MW:*, PW:* # MW:*>
	void makeKSparse(DPJArrayChar<Text> text,
			DPJArrayChar<Pat> pattern,
			DPJArrayInt<PW> patternWit,
			DPJArrayInt<MW> matchWit,
			int k) 
	//	reads Text:*, Pat:*, PW:* writes MW:* 
	{
		final DPJPartitionInt<MW> matchWitSegs =
			DPJPartitionInt.stridedPartition(matchWit, 1<<k);
		foreach (int i in 0, matchWitSegs.length) {
			DPJArrayInt<matchWitSegs:[i]:*> matchWitSeg = matchWitSegs.get(i);
			DPJPair<Integer,Integer,Local:[i]:*> candidates = 
				this.<region Local:[i]:*,matchWitSegs:[i]:*>getCandidates(matchWitSeg);
				int p = candidates.first.intValue();
				int q = candidates.second.intValue();
				// If there are two candidates in this segment, p and q
				// are both 0 or greater.  If not, we are done.
				if (p >= 0 && q >= 0) {
					duel(p+matchWitSeg.start, q+matchWitSeg.start,
							text,pattern,patternWit,matchWitSeg);
				}
		}
	}

	/**
	 * Make witnesses sparse in the periodic case
	 */
	public <region Pat,Wit | Pat:* # Wit:*>void
	makeSparsePeriodic(DPJArrayChar<Pat> pattern,
			DPJArrayInt<Wit> witnesses,
			int i, int j, int c) 
	//	reads Pat:* writes Wit:* 
	{
		int w = witness(pattern,
				pattern.subarray(j,(1<<(c+1))-j));			
		witnesses.put(j, w);
		for (int p = j+1; p < j+1+(1<<c)-(j+1); p++) {
			if ((p-j) % j == 0) {
				witnesses.put(p, w - (p-j));
			} else if (p < (1<<c)-(1<<(i+1))) {
				witnesses.put(p, modPrime(p,j));
			}
		}
		i = c-3;
		if (i >= 0) {
			int segLen = (1<<i);
			for (int k = 4; k >= 1; --k) {
				int start = (1<<c)-k*segLen;
				int[]<Local> arr = new int[2]<Local>;
				arr[0] = start;
				arr[1] = start+segLen;
				final DPJPartitionInt<Wit> segs =
					new DPJPartitionInt<Wit>(witnesses, arr);
				makeSparse(pattern, pattern,  segs.get(0), segs.get(1), i); 
				int cand = getCandidate(segs.get(1));
				if (cand >= 0) {
					cand += start;
					witnesses.put(cand, 
							witness(pattern,
									pattern.subarray(cand, 
											(1<<(c+1))-cand)));
				}
			}
		}
	}

	/**
	 * Compute the continuation c of the period P, i.e., the maximal
	 * integer such that P is the period of the first c-block.
	 */
	public <region Pat>int periodCont(DPJArrayChar<Pat> pattern,
			int j, int currentCont) 
	//	reads SM, Pat:* 
	{
		int result = currentCont;
		while (result < logPatLen) {
			int w = witness(pattern,
					pattern.subarray(j, (1<<(result+1))-j));
			if (w != -1) break;
			++result;
		}
		return result;
	}

	// RECURSIVE LEAF METHODS (NO CALLEES EXCEPT ITSELF)

	/**
	 * Find an index that is a witness to a mismatch between pattern
	 * and text.  Return -1 if pattern is a prefix of text.  Otherwise
	 * return k, where k is any integer 0 <= k < pattern.length such
	 * that pattern[k] != text[k].
	 *
	 * @param text    The text to match against
	 * @param pattern The pattern to match against text
	 */
	public <region Text, Pat>int witness(DPJArrayChar<Text> text,
			DPJArrayChar<Pat> pattern)
	//	reads Text:*, Pat:* 
	{
		int result = -1;
		if (pattern.length == 1) {
			if (pattern.get(0) != PAD_CHAR &&
					pattern.get(0) != text.get(0))
				result = 0;
		} else if (pattern.length > 0) {
			int result1 = 0, result2 = 0;
			int idx = pattern.length >> 1;
					DPJPartitionChar<Pat> segs =
						new DPJPartitionChar<Pat>(pattern, idx);
					cobegin {
						result1 = witness(text, segs.get(0));
						result2 = witness(text.subarray(idx, text.length-idx),
								segs.get(1));
					}
					if (result1 >= 0) result = result1;
					else if (result2 >= 0) result = result2 + idx;
		}
		return result;
	}

	// LEAF METHODS (NO CALLEES)

	/**
	 * Initialize an array to a single value
	 */
	private <region R>void initialize(DPJArrayInt<R> A, int v) 
	//	writes R 
	{
		for (int i = 0; i < A.length; ++i) {
			A.put(i, v);
		}
	}

	/**
	 * Look for two candidates p and q (i.e., p and q such that seg[p]
	 * = -1 and seg[q] = -1).  If either candidate does not exist, use
	 * -1 for that candidate.
	 */
	private <region R1,R2>DPJPair<Integer, Integer, R1>
	getCandidates(DPJArrayInt<R2> seg) 
	//	reads R2 
	{
		int p=-1, q=-1;
		for (int i = 0; i < seg.length; ++i) {
			if (seg.get(i) == -1) {
				if (p == -1) {
					p = i;
				} else {
					q = i;
					break;
				}
			}
		}
		return new DPJPair<Integer,Integer,R1>(p,q);
	}

	/**
	 * Examine a witnesses for a position where the witness is negative.
	 * If one is found, return it (there is guaranteed to be at most
	 * one).  If none is found, return -1.
	 */
	private <region R>int getNegIdx(DPJArrayInt<R> witnesses) 
	//	reads R 
	{
		// Could do this in parallel because we know there is at most
		// one zero index.  However, proving determinism in this case
		// would be tough.  Sequential is probably OK because it's
		// short (O(log m)).
		for (int i = 0; i < witnesses.length; ++i) {
			if (witnesses.get(i) < 0) return i;
		}
		return -1;
	}

	/**
	 * Look for a single candidate p such that witnesses[p] == -1 and
	 * return it.  If no candidate exists, return -1.
	 */
	private static <region R>int getCandidate(DPJArrayInt<R> witnesses) 
	//	reads R 
	{
		int p = -1;
		for (int i = 0; i < witnesses.length; ++i) {
			if (witnesses.get(i) == -1) {
				p = i;
				break;
			}
		}
		return p;
	}

	/**
	 * Eliminate one of a pair of candidates from matchWit by deducing
	 * that both cannot be candidates.  If both were candidates, then
	 * pattern would overlap with itself in a way that is contradicted
	 * by patternWit.
	 */
	private <region Text, Pat, PW, MW | Text # MW, PW # MW>
	void duel(int p, int q,
			DPJArrayChar<Text> text,
			DPJArrayChar<Pat> pattern,
			DPJArrayInt<PW> patternWit,
			DPJArrayInt<MW> matchWit)	
	//	reads PW, Pat, Text writes MW 
	{
		int j = q-p;
		int w = patternWit.get(j);
		if (pattern.get(w) != PAD_CHAR &&
				(q+w >= text.length ||
						text.get(q+w) != pattern.get(w))) {
			matchWit.put(q - matchWit.start, w);
		} else {
			matchWit.put(p - matchWit.start, q-p+w);
		}
	}

	/**
	 * Check a pattern against the text at position idx.  Return true
	 * or false according to whether the pattern matches the text.
	 */
	private <region Text,Pat>boolean checkIdx(DPJArrayChar<Text> text,
			DPJArrayChar<Pat> pattern,
			int idx)
	//	reads SM, Text, Pat
	{
		if (idx + truePatLen > text.length) return false;
		boolean result = true;
		for (int i = 0; i < truePatLen; ++i) {
			if (pattern.get(i) != text.get(idx+i))
				result = false;
		}
		return result;
	}

	/**
	 * Compute p mod' P, defined as p mod P if p mod P != 0, else
	 * P.
	 */
	int modPrime(int p, int P) 
	//	pure 
	{
		int m = p % P;
		return (m != 0) ? m : P;
	}

}


