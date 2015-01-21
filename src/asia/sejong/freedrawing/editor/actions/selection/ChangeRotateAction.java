package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import org.eclipse.gef.commands.CompoundCommand;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.RotateCommand;

public class ChangeRotateAction extends ElementSelectionAction<FDShape> {
	
	private double degree;

	public ChangeRotateAction(FreedrawingEditor part) {
		super(part);
	}
	
	public void run() {
		
		List<FDShape> lists = getElements(FDShape.class);
		
		if (lists.size()>0) {
			CompoundCommand rotateCmd = new CompoundCommand();
			for ( FDShape element : lists ) {
				rotateCmd.add(new RotateCommand(element, degree));
			}
			execute(rotateCmd);
			
		} else {
		}
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	public void setDegree(double degree) {
		this.degree = degree;
		getEditorContext().setDegree(degree);
	}
}
