package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;

import asia.sejong.freedrawing.context.FreedrawingEditorContext;
import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDElement;


public abstract class ElementSelectionAction<T extends FDElement> extends SelectionAction {

	public ElementSelectionAction(FreedrawingEditor part) {
		super(part);
	}
	
	private FreedrawingEditor getEditor() {
		return (FreedrawingEditor)getWorkbenchPart();
	}
	
	protected FreedrawingEditorContext getEditorContext() {
		return getEditor().getEditorContext();
	}
	
	@SuppressWarnings("unchecked")
	protected final List<T> getElements(Class<T> clazz) {

		List<T> lists = new ArrayList<T>();
		for ( Object item : getSelectedObjects() ) {
			if ( item instanceof EditPart ) {
				Object model = ((EditPart)item).getModel();
				if ( clazz.isInstance(model) ) {
					lists.add((T)model);
				}
			}
		}
		
		return lists;
	}
	
	//protected abstract T filter(Object model);

	@Override
	protected boolean calculateEnabled() {
		return true;
	}
}
