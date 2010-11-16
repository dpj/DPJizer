/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.ui;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "edu.illinois.dpjizer.effect.ui.messages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String InferEffectsRefactoring_name;
	public static String InferEffectsRefactoring_noProjectFound;
	public static String InferEffectsRefactoring_collectingChanges;
	public static String InferEffectsRefactoring_checking;
	public static String InferEffectsRefactoring_changeName;
	public static String InferEffectsInputPage_changeDescription;
}