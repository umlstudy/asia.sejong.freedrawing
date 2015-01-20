package asia.sejong.freedrawing.editor.actions.contributions;

import java.util.List;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
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
	
	public ComboSelectionItem(String id) {
		super(id);
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
		comboViewer.setInput(items);
		comboViewer.setSelection(new StructuredSelection(items.get(0)));
		
		return background;
	}

	public void setInput(List<T> items) {
		this.items = items;
	}
}
