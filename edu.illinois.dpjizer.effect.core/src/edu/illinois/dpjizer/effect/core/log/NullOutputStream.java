/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class NullOutputStream extends OutputStream {
	@Override
	public void write(int b) throws IOException {
	}
}