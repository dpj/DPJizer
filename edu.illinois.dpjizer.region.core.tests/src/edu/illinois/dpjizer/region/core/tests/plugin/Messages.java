/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.plugin;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "edu.illinois.dpjizer.region.core.tests.plugin.messages"; //$NON-NLS-1$
	public static String Dirs_PluginId;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
