package edu.illinois.dpjizer.effect.core.log;

import java.io.File;

public class Dirs {

	public static final String FILE_SEPARATOR = File.separator;
	public static final String ROOT_TEST_PROGRAMS = "dpj-programs";
	public static final String ACTUAL_CONSTRAINTS_FILE = "constraints.txt";
	public static final String ACTUAL_SOLVED_CONSTRAINTS_FILE = "solved-constraints.txt";
	public static final String ACTUAL_CONSTRAINTS_PATH = ROOT_TEST_PROGRAMS + Dirs.FILE_SEPARATOR + Dirs.ACTUAL_CONSTRAINTS_FILE;
	public static final String ACTUAL_SOLVED_CONSTRAINTS_PATH = ROOT_TEST_PROGRAMS + Dirs.FILE_SEPARATOR + Dirs.ACTUAL_SOLVED_CONSTRAINTS_FILE;

}
