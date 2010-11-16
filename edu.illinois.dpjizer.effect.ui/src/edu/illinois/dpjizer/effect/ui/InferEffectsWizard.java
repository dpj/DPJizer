/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.ui;

import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class InferEffectsWizard extends RefactoringWizard {

	private final InferEffectsInput inferEffectsChangeInfo;

	public InferEffectsWizard(Refactoring refactoring, InferEffectsInput inferEffectsChangeInfo) {
		super(refactoring, DIALOG_BASED_USER_INTERFACE);
		this.inferEffectsChangeInfo = inferEffectsChangeInfo;
	}

	@Override
	protected void addUserInputPages() {
		setDefaultPageTitle(getRefactoring().getName());
		addPage(new InferEffectsInputPage(inferEffectsChangeInfo));
	}

}
