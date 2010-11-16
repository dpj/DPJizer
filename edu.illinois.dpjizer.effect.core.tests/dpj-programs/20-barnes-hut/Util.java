/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * Some utility methods for the BH application
 * @author Robert L. Bocchino Jr.
 * @date July 2008
 */

import java.util.Formatter;

public class Util {
	public Util() {}
	
	public static final double A = 16807.0;
	public static final double M = 2147483647.0;

	/** 
	 * Return a random number, which is also the next seed. 
	 *
	 * @param seed  The random number seed
	 */
	public static double rand(double seed) {
		double t = A*seed  + 1;
		seed = t - (M * Math.floor(t / M));
		return seed;

	}

	/**
	 * Generate floating-point random number
	 */
	public static double xrand(double xl, double xh, double r) {   
		double res;
		res = xl + (xh-xl)*r/2147483647.0;
		return res;
	}

	/**
	 * Formatted output
	 */
	public static boolean chatting(String fmt, Object... args) {
		Formatter f = new Formatter(System.out);
		f.format(fmt, args);
		return true;
	}

	public static void printResults(String name) {
	}
}
