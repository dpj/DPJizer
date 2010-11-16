/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class InferEffectsInputPage extends UserInputWizardPage {

	public InferEffectsInputPage(InferEffectsInput inferEffectsChangeInfo) {
		super(InferEffectsInputPage.class.getName());
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = createRootComposite(parent);
		setControl(composite);
		createChangeDescription(composite);
	}

	private Composite createRootComposite(final Composite parent) {
		Composite result = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 10;
		gridLayout.marginHeight = 10;
		result.setLayout(gridLayout);
		initializeDialogUnits(result);
		Dialog.applyDialogFont(result);
		return result;
	}

	private void createChangeDescription(final Composite composite) {
		Label changeDescriptionLabel = new Label(composite, SWT.NONE);
		changeDescriptionLabel.setText(Messages.InferEffectsInputPage_changeDescription);
	}

}
