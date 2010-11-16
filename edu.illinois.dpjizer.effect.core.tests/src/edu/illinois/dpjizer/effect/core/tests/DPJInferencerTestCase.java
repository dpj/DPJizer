/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import junit.framework.Assert;
import junit.framework.TestCase;
import edu.illinois.dpjizer.effect.core.compile.CompilerInvoker;
import edu.illinois.dpjizer.effect.core.constraint.Method;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 * 
 */
public abstract class DPJInferencerTestCase extends TestCase {

	protected void printDisabledTestMessage(String reason) {
		System.out.println(String.format("%s is disabled (%s).", getTestDir() + testNameToFileName(), reason));
	}

	protected String absoultePathOf(String filepath) {
		String curdir = new File(".").getAbsolutePath();
		String target = Dirs.TEST_PLUGIN_NAME + edu.illinois.dpjizer.effect.core.log.Dirs.FILE_SEPARATOR;
		int index = curdir.lastIndexOf(target);
		if (index < 0)
			throw new Error("Cannot locate directory " + target + edu.illinois.dpjizer.effect.core.log.Dirs.ROOT_TEST_PROGRAMS);
		int len = index + target.length();
		String dir = curdir.substring(0, len) + edu.illinois.dpjizer.effect.core.log.Dirs.ROOT_TEST_PROGRAMS + edu.illinois.dpjizer.effect.core.log.Dirs.FILE_SEPARATOR;
		return dir + filepath;
	}

	private void assertFilesAreTheSame(String expectedFilename, String actualFilename) throws FileNotFoundException {
		Scanner expected = new Scanner(new File(expectedFilename));
		Scanner actual = new Scanner(new File(actualFilename));
		StringBuffer expectedStringBuf = new StringBuffer();
		StringBuffer actualStringBuf = new StringBuffer();

		while (expected.hasNextLine())
			expectedStringBuf.append(expected.nextLine() + Dirs.LINE_SEPARATOR);

		while (actual.hasNextLine())
			actualStringBuf.append(actual.nextLine() + Dirs.LINE_SEPARATOR);

		expected.close();
		actual.close();
		assertEquals(expectedStringBuf.toString(), actualStringBuf.toString());
	}

	private String getActualFilename(String filename) {
		Assert.assertTrue(filename.equals(edu.illinois.dpjizer.effect.core.log.Dirs.ACTUAL_CONSTRAINTS_FILE) || filename.equals(edu.illinois.dpjizer.effect.core.log.Dirs.ACTUAL_SOLVED_CONSTRAINTS_FILE));
		return absoultePathOf(filename);
	}

	private String getActualConstraintsFilename() {
		return getActualFilename(edu.illinois.dpjizer.effect.core.log.Dirs.ACTUAL_CONSTRAINTS_FILE);
	}

	private String getActualSolvedConstraintsFilename() {
		return getActualFilename(edu.illinois.dpjizer.effect.core.log.Dirs.ACTUAL_SOLVED_CONSTRAINTS_FILE);
	}

	private String getExpectedFilename(String name, String postfix) {
		Assert.assertTrue(postfix.equals(Dirs.EXPECTED_CONSTRAINTS_EXTENSION) || postfix.equals(Dirs.EXPECTED_SOLVED_CONSTRAINTS_EXTENSION));
		return absoultePathOf(getTestDir() + name + postfix);
	}

	private String getExpectedConstraintsFilename(String name) {
		return getExpectedFilename(name, Dirs.EXPECTED_CONSTRAINTS_EXTENSION);
	}

	private String getExpectedSolvedConstraintsFilename(String name) {
		return getExpectedFilename(name, Dirs.EXPECTED_SOLVED_CONSTRAINTS_EXTENSION);
	}

	protected abstract String getTestDir();

	private String testNameToFileName() {
		String testName = getName();
		String name = testName.substring("test".length());
		return name;
	}

	protected void compareCollectedConstraints() {
		String name = testNameToFileName();
		compareCollectedConstraints(name, new String[] { name });
	}

	protected void compareCollectedConstraints(String expectedFileName, String[] filepaths) {
		String[] absoluteFilePaths = absolutePathsOf(filepaths);
		collectConstraints(absoluteFilePaths);
		try {
			assertFilesAreTheSame(getExpectedConstraintsFilename(expectedFileName), getActualConstraintsFilename());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private String[] absolutePathsOf(String[] filepaths) {
		String[] absoluteFilePaths = new String[filepaths.length];
		for (int i = 0; i < absoluteFilePaths.length; i++) {
			absoluteFilePaths[i] = absoultePathOf(getTestDir() + filepaths[i] + ".java");
		}
		return absoluteFilePaths;
	}

	protected void compareSolvedConstraints() {
		String name = testNameToFileName();
		compareSolvedConstraints(name, new String[] { name });
	}

	protected void compareSolvedConstraints(String expectedFileName, String[] filepaths) {
		String[] absoluteFilePaths = absolutePathsOf(filepaths);
		solveConstraints(absoluteFilePaths);
		try {
			assertFilesAreTheSame(getExpectedSolvedConstraintsFilename(expectedFileName), getActualSolvedConstraintsFilename());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	protected void collectConstraints(String filepath) {
		collectConstraints(new String[] { filepath });
	}

	protected void collectConstraints(String[] filepaths) {
		try {
			new CompilerInvoker().collectConstraints(filepaths);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	protected void solveConstraints(String filepath) {
		solveConstraints(new String[] { filepath });
	}

	protected void solveConstraints(String[] filepaths) {
		try {
			ArrayList<Map<Method, String>> fileEffects = new CompilerInvoker().solveConstraints(filepaths);
			String[] annotatedFilepaths = insertEffects(fileEffects, filepaths);
			new CompilerInvoker().compile(annotatedFilepaths);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Insert the effects of each method back into the original source file.
	 * 
	 * @return the list of new files with inferred effect annotations.
	 */
	String[] insertEffects(ArrayList<Map<Method, String>> fileEffects, String[] filepaths) {
		String[] annotatedFilepaths = annotatedFilePaths(filepaths);

		for (int i = 0; i < filepaths.length; ++i) {
			copyFile(filepaths[i], annotatedFilepaths[i]);
			for (Map<Method, String> methodEffects : fileEffects) {
				ArrayList<Edit> edits = new ArrayList<Edit>();
				for (Method method : methodEffects.keySet()) {
					// FIXME: The following condition doesn't distinguish among
					// files of the same name in different dirs.
					if (new File(filepaths[i]).getName().equals(method.getSourceFileName())) {
						// System.out.println(filepaths[i] + " <-> " +
						// method.getSourceFileName());
						edits.add(new Edit(method.getOffset(), methodEffects.get(method) + " "));
					}
				}
				insertEdits(edits, annotatedFilepaths[i]);
			}
		}

		return annotatedFilepaths;
	}

	private String[] annotatedFilePaths(String[] filepaths) {
		String[] annotatedFilepaths = new String[filepaths.length];
		for (int i = 0; i < filepaths.length; ++i) {
			File file = new File(filepaths[i]);
			String effectsDir = file.getParent() + edu.illinois.dpjizer.effect.core.log.Dirs.FILE_SEPARATOR + Dirs.EFFECTS_SUBDIR;
			new File(effectsDir).mkdir();
			annotatedFilepaths[i] = effectsDir + edu.illinois.dpjizer.effect.core.log.Dirs.FILE_SEPARATOR + file.getName();
		}
		return annotatedFilepaths;
	}

	static class Edit implements Comparable<Edit> {
		int offset;
		String str;

		public Edit(int offset, String str) {
			super();
			this.offset = offset;
			this.str = str;
		}

		@Override
		public int compareTo(Edit other) {
			return offset - other.offset;
		}

		@Override
		public String toString() {
			return "(" + offset + ", " + str + ")";
		}
	}

	void insertEdits(ArrayList<Edit> edits, String filepath) {
		Edit[] sortedEdits = new Edit[edits.size()];
		edits.toArray(sortedEdits);
		Arrays.sort(sortedEdits);
		String originalStr = fileContents(filepath);
		ArrayList<Character> originalChars = new ArrayList<Character>(originalStr.length());
		for (char character : originalStr.toCharArray()) {
			originalChars.add(character);
		}

		for (int i = sortedEdits.length - 1; i >= 0; --i) {
			for (int j = sortedEdits[i].str.length() - 1; j >= 0; --j)
				originalChars.add(sortedEdits[i].offset, sortedEdits[i].str.charAt(j));
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (Character character : originalChars) {
			stringBuilder.append(character);
		}
		writeEdits(stringBuilder.toString(), filepath);
	}

	private void writeEdits(String contents, String filepath) {
		FileWriter out = null;
		try {
			File outputFile = new File(filepath);
			out = new FileWriter(outputFile);
			out.append(contents);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	String fileContents(String filepath) {
		StringBuilder stringBuilder = new StringBuilder();
		FileReader in = null;
		try {
			File inputFile = new File(filepath);
			in = new FileReader(inputFile);
			int c;
			while ((c = in.read()) != -1)
				stringBuilder.append((char) c);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return stringBuilder.toString();
	}

	void copyFile(String inputPath, String outputPath) {
		try {
			File inputFile = new File(inputPath);
			File outputFile = new File(outputPath);

			FileReader in = new FileReader(inputFile);
			FileWriter out = new FileWriter(outputFile);
			int c;

			while ((c = in.read()) != -1)
				out.write(c);

			in.close();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("File copy failed.");
		} finally {
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		new File(edu.illinois.dpjizer.effect.core.log.Dirs.ACTUAL_SOLVED_CONSTRAINTS_PATH).delete();
		new File(edu.illinois.dpjizer.effect.core.log.Dirs.ACTUAL_CONSTRAINTS_PATH).delete();
	}
}
