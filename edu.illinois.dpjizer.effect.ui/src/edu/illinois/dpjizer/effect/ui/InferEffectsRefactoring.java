/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.ValidateEditChecker;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEditGroup;

import edu.illinois.dpjizer.effect.core.compile.CompilerInvoker;
import edu.illinois.dpjizer.effect.core.constraint.Method;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 * 
 */
public class InferEffectsRefactoring extends Refactoring {

	private static final String DPJ_EXTENSION = "java"; //$NON-NLS-1$

	private final InferEffectsInput inferEffectsInput;

	private final List<IFile> dpjFiles;

	private ArrayList<Map<Method, String>> solvedConstraints;

	public InferEffectsRefactoring(final InferEffectsInput inferEffectsInput) {
		this.inferEffectsInput = inferEffectsInput;
		dpjFiles = new ArrayList<IFile>();
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor progressMonitor) throws CoreException, OperationCanceledException {
		RefactoringStatus result = new RefactoringStatus();
		progressMonitor.beginTask(Messages.InferEffectsRefactoring_checking, 100);
		findDPJFiles(inferEffectsInput.getProject().getProject(), result);
		progressMonitor.worked(50);
		result.merge(areDPJFilesSync(progressMonitor));
		progressMonitor.done();
		return result;
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		RefactoringStatus result = new RefactoringStatus();
		IProject project = inferEffectsInput.getProject();
		if (project == null || !project.exists()) {
			result.addFatalError(Messages.InferEffectsRefactoring_noProjectFound);

		}
		return result;
	}

	@Override
	public Change createChange(IProgressMonitor progressMonitor) {
		try {
			progressMonitor.beginTask(Messages.InferEffectsRefactoring_collectingChanges, 100);

			CompositeChange rootChange = new CompositeChange(Messages.InferEffectsRefactoring_changeName);
			String[] filePaths = filePathsOfIFiles();
			CompilerInvoker compilerInvoker = new CompilerInvoker();
			solvedConstraints = compilerInvoker.solveConstraints(filePaths);
			progressMonitor.worked(50);

			for (IFile dpjFile : dpjFiles) {
				rootChange.add(createInferEffectsChanges(dpjFile));
				progressMonitor.worked(50 / dpjFiles.size());
			}
			return rootChange;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			progressMonitor.done();
		}
	}

	@Override
	public String getName() {
		return Messages.InferEffectsRefactoring_name;
	}

	private void findDPJFiles(final IContainer rootContainer, final RefactoringStatus status) {
		try {
			IResource[] members = rootContainer.members();
			for (IResource member : members) {
				if (member instanceof IContainer) {
					findDPJFiles((IContainer) member, status);
				} else {
					IFile file = (IFile) member;
					handleFile(file, status);
				}
			}
		} catch (final CoreException coreException) {
			status.addFatalError(coreException.getMessage());
		}
	}

	private boolean isToRefactor(final IFile file) {
		return DPJ_EXTENSION.equals(file.getFileExtension());
	}

	private void handleFile(final IFile file, final RefactoringStatus status) {
		if (isToRefactor(file)) {
			dpjFiles.add(file);
		}
	}

	private String[] filePathsOfIFiles() {
		String[] filePaths = new String[dpjFiles.size()];
		for (int i = 0; i < filePaths.length; i++) {
			filePaths[i] = dpjFiles.get(i).getLocation().toOSString();
		}
		return filePaths;
	}

	private Change createInferEffectsChanges(final IFile dpjFile) {
		TextFileChange textFileChange = new TextFileChange(dpjFile.getName(), dpjFile);
		MultiTextEdit fileChangeRootEdit = new MultiTextEdit();
		textFileChange.setEdit(fileChangeRootEdit);
		// TODO: I should report compiler or inferencer errors using
		// RefactoringStatus.
		try {
			for (Map<Method, String> solutions : solvedConstraints) {
				for (Method method : solutions.keySet()) {
					if (method.getSourceFileName().equals(dpjFile.getName())) {
						InsertEdit insertEdit = new InsertEdit(method.getOffset(), solutions.get(method) + " ");
						fileChangeRootEdit.addChild(insertEdit);
						textFileChange.addTextEditGroup(new TextEditGroup("Effects on " + method.getName(), insertEdit));
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return textFileChange;
	}

	private RefactoringStatus areDPJFilesSync(IProgressMonitor progressMonitor) throws CoreException {
		IFile[] files = new IFile[dpjFiles.size()];
		dpjFiles.toArray(files);
		ValidateEditChecker editChecker = new ValidateEditChecker(null);
		editChecker.addFiles(files);
		return editChecker.check(progressMonitor);
	}

}
