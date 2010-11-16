/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
import DPJRuntime.*;

public class StringMatchingHarness extends Harness {

	int TEXT_SIZE = size;
	// Must be a power of 2 to test computePatternWitnesses(...)
	// directly.  match(...) always pads the pattern to a power of 2
	// before calling this function.
	public static int PATTERN_SIZE = 16;
	public static final int ALPHA_SIZE = 20;
	public static final int PERIOD = 3;
	public static final int END_SIZE = 0;

	region Data;
	char[]<Data> text = new char[TEXT_SIZE]<Data>;
	char[]<Data> pattern = new char[PATTERN_SIZE]<Data>;

	/**
	 * Arbitrary size text and pattern OK (|pattern| <= |text|)
	 */
	public void testMatchRandom() {
		System.out.print("testMatchRandom...");
		int myPatternSize = PATTERN_SIZE + 3; // Not a power of 2
		pattern = new char[myPatternSize]<Data>;
		for (int i = 0; i < myPatternSize; ++i) {
			pattern[i] = randomChar(ALPHA_SIZE);
		}
		// Add one character outside the alphabet to pattern to
		// prevent false matches
		pattern[(int) (Math.random() * myPatternSize)] = 
			(char) ('a' + (char) ALPHA_SIZE);
		for (int i = 0; i < TEXT_SIZE; ++i) {
			text[i] = randomChar(ALPHA_SIZE);
		}
		int idx = (int) (Math.random() * (TEXT_SIZE - myPatternSize));
		for (int i = 0; i < myPatternSize; ++i) {
			text[idx+i] = pattern[i];
		}
		StringMatching sm = new StringMatching();
		DPJHashSet<Integer,Local> result = new DPJHashSet<Integer,Local>();
		sm.<region Data,Local>match(text, pattern, result);
		int num = 0;
		for (DPJIterator<Integer,Local> I = result.iterator();
		I.hasNext(); ) {
			testCheckIdx(I.next());
			++num;
		}
		if (num != 1) {
			System.out.println("num="+num);
		}
		assert(num == 1);
		System.out.println("OK");
	}

	public void testMatchPeriodic() {
		System.out.print("testMatchPeriodic...");
		int myPatternSize = PATTERN_SIZE + 3; // Not a power of 2
		pattern = new char[myPatternSize]<Data>;
		makePeriodicPattern(pattern, ALPHA_SIZE+1, PERIOD, myPatternSize, END_SIZE);
		for (int i = 0; i < TEXT_SIZE; ++i) {
			text[i] = randomChar(ALPHA_SIZE);
		}
		int idx = (int) (Math.random() * (TEXT_SIZE - myPatternSize));
		for (int i = 0; i < myPatternSize; ++i) {
			text[idx+i] = pattern[i];
		}
		StringMatching sm = new StringMatching();
		DPJHashSet<Integer,Local> result = new 
		DPJHashSet<Integer,Local>();
		sm.<region Data,Local>match(text, pattern, result);
		int num = 0;
		for (DPJIterator<Integer,Local> I = result.iterator();
		I.hasNext(); ) {
			testCheckIdx(I.next());
			++num;
		}
		if (num == 0) {
			System.err.println("ERROR: num=0");
			System.err.println("text="+AtoSChar(text));
			System.err.println("pattern="+AtoSChar(pattern));
			System.exit(1);
		}
		System.out.println("OK");
	}

	/**
	 * PATTERN_SIZE must be a power of 2
	 */
	public void testPWPeriodic() {
		System.out.print("testPWPeriodic...");
		makePeriodicPattern(pattern, ALPHA_SIZE, PERIOD, PATTERN_SIZE, END_SIZE);
		StringMatching sm = new StringMatching();
		sm.logPatLen = DPJUtils.log2(pattern.length);
		DPJArrayInt<Local> patternWitnesses =
			new DPJArrayInt<Local>(pattern.length >> 1);
		sm.<region Data,Local>computePatternWitnesses(new DPJArrayChar<Data>(pattern), 
				patternWitnesses);
		System.out.println("OK");
	}

	public void testPWRandom() {
		System.out.print("testPWRandom...");
		for (int i = 0; i < PATTERN_SIZE; ++i) {
			pattern[i] = randomChar(ALPHA_SIZE);
		}
		StringMatching sm = new StringMatching();
		sm.logPatLen = DPJUtils.log2(pattern.length);
		DPJArrayInt<Local> patternWitnesses =
			new DPJArrayInt<Local>(pattern.length >> 1);
		sm.<region Data,Local>computePatternWitnesses(new DPJArrayChar<Data>(pattern), 
				patternWitnesses);
		assert(patternWitnesses.get(0) == -1);
		for (int i = 1; i < patternWitnesses.length; ++i) {
			int w = patternWitnesses.get(i);
			if (w >= 0) {
				assert(pattern[w] != pattern[i+w]);
			} else {
				assert(sm.period > 0);
			}
		}
		System.out.println("OK");
	}

	/**
	 * Utility stuff
	 */
	public static char randomChar(int alphaSize) {
		return (char) ('a' + (char) (Math.random() * alphaSize));
	}

	public static <T>String AtoS(T[] A) {
		StringBuffer sb = new StringBuffer();
		for (T a : A) {
			sb.append(a.toString());
		}
		return sb.toString();
	}

	public static String AtoSChar(char[]<*> A) {
		StringBuffer sb = new StringBuffer();
		for (char a : A) {
			sb.append(a);
		}
		return sb.toString();
	}

	private void testCheckIdx(int idx) {
		for (int i = 0; i < PATTERN_SIZE; ++i) {
			if (pattern[i] != text[idx+i]) {
				System.err.println("ERROR:  No match at " + idx);
				System.exit(1);
			}
		}
	}

	public static void makePeriodicPattern(char[]<*> pattern, int alphaSize, int period, 
			int patternSize, int endSize) {
		char[] periodPattern = new char[period];
		for (int i = 0; i < period; ++i) {
			periodPattern[i] = randomChar(alphaSize);
		}
		for (int i = 0; i < patternSize - endSize; i += period) {
			for (int j = 0; j < period; ++j) {
				if (i+j < pattern.length)
					pattern[i+j] = periodPattern[j];
			}
		} 
		for (int i = 0; i < endSize; ++i) {
			pattern[patternSize - endSize + i] = 
				randomChar(alphaSize);
		}
	}

	public StringMatchingHarness(String[] args) {
		super("StringMatching", args, 2, 3);
	}

	@Override
	public void initialize() {
		pattern = new char[PATTERN_SIZE]<Data>;
		for (int i = 0; i < PATTERN_SIZE; ++i) {
			pattern[i] = randomChar(ALPHA_SIZE);
		}
		for (int i = 0; i < TEXT_SIZE; ++i) {
			text[i] = randomChar(ALPHA_SIZE);
		}
		int idx = (int) (Math.random() * (TEXT_SIZE - PATTERN_SIZE));
		for (int i = 0; i < PATTERN_SIZE; ++i) {
			text[idx+i] = pattern[i];
		}
	}

	@Override
	public void runTest() {
		testPWRandom();
		testPWPeriodic();
		testMatchRandom();
		testMatchPeriodic();
	}

	@Override
	public void runWork() {
		StringMatching sm = new StringMatching();
		DPJHashSet<Integer,Local> result = 
			new DPJHashSet<Integer,Local>();
		sm.<region Data,Local>match(text, pattern, result);
	}

	public void usage() {
		System.err.println("Usage:  java " + progName + ".java [mode] [text_length] [pattern_length]");
		System.err.println("mode = TEST, IDEAL, TASK_GRAPH");
		System.err.println("text_length = int");
		System.err.println("pattern_length = int");
	}

	public static void main(String[] args) {
		StringMatchingHarness harness = new StringMatchingHarness(args);
		if (args.length == 3) {
			PATTERN_SIZE = Integer.parseInt(args[2]);
		}
		harness.run();
	}

}