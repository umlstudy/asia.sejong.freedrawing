package asia.sejong.freedrawing.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import asia.sejong.freedrawing.model.area.FDRectangle;
import asia.sejong.freedrawing.model.area.FreedrawingData;
import asia.sejong.freedrawing.model.connection.FDBendpointConnection;
import asia.sejong.freedrawing.parts.area.FDRectangeEditPart;
import asia.sejong.freedrawing.parts.area.FreedrawingDataEditPart;
import asia.sejong.freedrawing.parts.connection.FDBendpointConnectionEditPart;

public class FreedrawingEditPartFactory implements EditPartFactory {
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof FreedrawingData)
			return new FreedrawingDataEditPart((FreedrawingData) model);
		if (model instanceof FDRectangle)
			return new FDRectangeEditPart((FDRectangle) model);
		if (model instanceof FDBendpointConnection)
			return new FDBendpointConnectionEditPart((FDBendpointConnection) model);
//		if (model instanceof GenealogyConnection)
//			return new GenealogyConnectionEditPart((GenealogyConnection) model);
//		if (model instanceof Note)
//			return new NoteEditPart((Note) model);
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}
}