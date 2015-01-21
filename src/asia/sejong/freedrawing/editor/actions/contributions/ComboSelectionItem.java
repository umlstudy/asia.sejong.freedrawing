package asia.sejong.freedrawing.editor.actions.contributions;

import java.util.List;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ComboSelectionItem<T> extends ControlContribution {

	private ComboViewer comboViewer;
	private ISelectionChangedListener listener;
	private ILabelProvider provider;
	private List<T> items;
	
	private IStructuredSelection defaultSelection;
	
	public ComboSelectionItem(IAction action) {
		super(action.getId()+"_CSI");
	}
	
	public void setLabelProvider(ILabelProvider provider) {
		this.provider = provider;
	}
	
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.listener = listener;
	}
	
	@Override
	protected Control createControl(Composite parent) {
		Composite background = new Composite(parent, SWT.CENTER);
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		background.setLayout(fillLayout);
		
		comboViewer = new ComboViewer(background,  SWT.BORDER);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		if ( provider != null ) {
			comboViewer.setLabelProvider(provider);
		} else {
			comboViewer.setLabelProvider(new LabelProvider());
		}
		comboViewer.addSelectionChangedListener(listener);
		if ( items != null ) {
			comboViewer.setInput(items);
			comboViewer.setSelection(new StructuredSelection(items.get(0)));
		} else {
			comboViewer.setInput(new Object[] {"......."});
		}
		
		comboViewer.setSelection(defaultSelection);
		
		return background;
	}

	public void setInput(List<T> items) {
		this.items = items;
		if ( comboViewer != null ) {
			comboViewer.setInput(items);
//			setSelection(2);
//			comboViewer.getControl().getParent().getParent().setRedraw(true);
		}
	}
	
	public void setSelection(T value) {
		if ( items != null ) {
			int indexOf = items.indexOf(value);
			setSelection(indexOf);
		}
	}
	
	public void setSelection(int index) {
		if ( items != null ) {
			setSelection(new StructuredSelection(items.get(index)));
		}
	}
	
	public void setSelection(IStructuredSelection selection) {
		if ( comboViewer != null ) {
			comboViewer.setSelection(selection);
		} else {
			defaultSelection = selection;
		}
	}
}
