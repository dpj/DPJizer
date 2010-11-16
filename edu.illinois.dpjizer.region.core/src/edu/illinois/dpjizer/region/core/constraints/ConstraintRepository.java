/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.constraints;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.google.inject.Inject;

import edu.illinois.dpjizer.region.core.dirs.Dirs;
import edu.illinois.dpjizer.region.core.dirs.FileUtils;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintRepository {

	Constraints constraints;

	Dirs dirs;

	int counter = 1;

	@Inject
	public ConstraintRepository(Constraints constraints, Dirs dirs) {
		this.constraints = constraints;
		this.dirs = dirs;
	}

	public void add(Constraint constraint) {
		constraints.add(constraint);
	}

	@Override
	public String toString() {
		return constraints.toString();
	}

	public void writeToFile() {
		FileUtils.createDirIfNotFound(dirs.getLogDirName());
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(dirs.getConstraintsFileName());
			for (Constraint constraint : constraints) {
				printWriter.println(constraint);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			if (printWriter != null)
				printWriter.close();
		}
	}

	public void solve() {

	}

}
