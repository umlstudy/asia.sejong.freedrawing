package asia.sejong.freedrawing.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import asia.sejong.freedrawing.model.FreedrawingData;

public class FreedrawingEditPartFactory implements EditPartFactory {
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof FreedrawingData)
			return new FreedrawingDataEditPart((FreedrawingData) model);
//		if (model instanceof Person)
//			return new PersonEditPart((Person) model);
//		if (model instanceof Marriage)
//			return new MarriageEditPart((Marriage) model);
//		if (model instanceof GenealogyConnection)
//			return new GenealogyConnectionEditPart((GenealogyConnection) model);
//		if (model instanceof Note)
//			return new NoteEditPart((Note) model);
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}
}