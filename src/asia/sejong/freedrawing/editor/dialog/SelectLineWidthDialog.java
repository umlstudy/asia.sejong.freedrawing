package asia.sejong.freedrawing.editor.dialog;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SelectLineWidthDialog extends Dialog {

	private Float result;
	private ComboViewer comboViewer;
	
	public SelectLineWidthDialog(Shell shell) {
		super(shell);
	}
	
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Select Line Width");
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(composite);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Select Line Width");
		
		comboViewer = new ComboViewer(composite);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return element == null ? "" : element.toString();
			}
		});
				
		return composite;
	}
	
	public void create() {
		super.create();
		
		addHook();
		initData();
	}
	
	private void addHook() {
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				if ( selection.getFirstElement() instanceof Float ) {
					result = (Float)selection.getFirstElement();
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				} else {
					result = null;
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				}
			}
		});
	}

	private void initData() {
		List<Float> data = Arrays.asList(1f,2f,3f,4f,5f,6f,7f);
		comboViewer.setInput(data);
		comboViewer.setSelection(new StructuredSelection(data.get(0)));
	}

	public Float getResult() {
		return result;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(200,200);
	}
	
	@Override
	protected boolean isResizable() {
		return false;
	}
}
