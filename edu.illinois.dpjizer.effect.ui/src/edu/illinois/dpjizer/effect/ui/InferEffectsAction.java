/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class InferEffectsAction implements IObjectActionDelegate {

	private Shell shell;
	private ISelection selection;
	private InferEffectsInput inferEffectsInput = new InferEffectsInput();

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		if (isProjectSelected()) {
			applySelection((IStructuredSelection) selection);
			openWizard();
		}
	}

	private boolean isProjectSelected() {
		return selection != null && selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1
				&& (((IStructuredSelection) selection).getFirstElement() instanceof IProject);
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	private void applySelection(final IStructuredSelection structuredSelection) {
		inferEffectsInput.setProject((IProject) structuredSelection.getFirstElement());
	}

	private void openWizard() {
		InferEffectsRefactoring change = new InferEffectsRefactoring(inferEffectsInput);
		InferEffectsWizard wizard = new InferEffectsWizard(change, inferEffectsInput);
		RefactoringWizardOpenOperation openOperation = new RefactoringWizardOpenOperation(wizard);
		try {
			openOperation.run(shell, "");
		} catch (final InterruptedException irex) {
		}
	}

}
