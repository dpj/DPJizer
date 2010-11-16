/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.dirs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Assert;

import com.google.inject.Inject;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class FileComparator {

	DPJizerTestDirs dpjizerTestDirs;

	@Inject
	public FileComparator(DPJizerTestDirs dpjizerTestDirs) {
		super();
		this.dpjizerTestDirs = dpjizerTestDirs;
	}

	public void compareFiles(String[] actualFiles, String[] expectedFiles) {
		Assert.assertEquals(expectedFiles.length, actualFiles.length);
		int numFiles = expectedFiles.length;
		for (int i = 0; i < numFiles; ++i) {
			assertFilesAreTheSame(expectedFiles[i], actualFiles[i]);
		}
	}

	protected void compareParamFiles(String[] filePaths, String testDir) {
		String[] actualFiles = dpjizerTestDirs.actualParamPaths(filePaths, testDir);
		String[] expectedFiles = dpjizerTestDirs.expectedParamPaths(filePaths, testDir);
		compareFiles(actualFiles, expectedFiles);
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
		FileReader in = null;
		FileWriter out = null;

		try {
			File inputFile = new File(inputPath);
			File outputFile = new File(outputPath);

			in = new FileReader(inputFile);
			out = new FileWriter(outputFile);
			int c;

			while ((c = in.read()) != -1)
				out.write(c);

			in.close();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("File copy failed.");
		}
	}

	protected void assertFilesAreTheSame(String expectedFilename, String actualFilename) {
		Scanner expected = null, actual = null;
		try {
			expected = new Scanner(new File(expectedFilename));
			actual = new Scanner(new File(actualFilename));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		StringBuilder expectedStringBuf = new StringBuilder();
		StringBuilder actualStringBuf = new StringBuilder();

		while (expected.hasNextLine())
			expectedStringBuf.append(expected.nextLine() + "\n");

		while (actual.hasNextLine())
			actualStringBuf.append(actual.nextLine() + "\n");

		expected.close();
		actual.close();
		Assert.assertEquals(expectedStringBuf.toString(), actualStringBuf.toString());
	}

}
