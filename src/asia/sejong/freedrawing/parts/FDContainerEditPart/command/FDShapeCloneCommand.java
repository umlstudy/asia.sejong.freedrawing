package asia.sejong.freedrawing.parts.FDContainerEditPart.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDWire;

public class FDShapeCloneCommand extends Command {

	private FDRoot root;
	private FDContainer container;
	private Map<FDShape, List<FDWire>> copiedShapesAndWires;
	
	public FDShapeCloneCommand(FDRoot root, FDContainer container, Map<FDShape, List<FDWire>> copiedShapesAndWires) {
		this.root = root;
		this.container = container;
		this.copiedShapesAndWires = copiedShapesAndWires;
	}
	
	@Override
	public void execute() {
		for ( FDShape shape : copiedShapesAndWires.keySet() ) {
			container.addShape(shape);
		}
		
		// wire
		for ( List<FDWire> wires : copiedShapesAndWires.values() ) {
			for ( FDWire wire : wires ) {
				root.addWire(wire);
			}
		}
	}
	
	@Override
	public void undo() {
		for ( FDShape shape : copiedShapesAndWires.keySet() ) {
			container.removeShape(shape);
		}
//		
//		// wire
//		for ( List<FDWire> wires : copiedShapesAndWires.values() ) {
//			for ( FDWire wire : wires ) {
//				root.removeWire(wire);
//			}
//		}
	}
	
	public static final Map<FDShape, List<FDWire>> cloneShapesWithWires(ArrayList<?> editParts, Point delta) {
		Map<FDShape, List<FDWire>> copiedShapesAndWires = new HashMap<FDShape, List<FDWire>>();
		
		if (editParts != null && editParts.size()>0 ) {
			List<FDShape> selectedShapes = new ArrayList<FDShape>();
			for ( Object selected : editParts ) {
				EditPart selectedEditPart = (EditPart)selected;
				if ( selectedEditPart.getModel() instanceof FDShape ) {
					selectedShapes.add((FDShape)selectedEditPart.getModel());
				}
			}
			
			copiedShapesAndWires.putAll(cloneShapesWithWires(selectedShapes, delta));
		}
		
		return copiedShapesAndWires;
	}

	public static Map<FDShape, List<FDWire>> cloneShapesWithWires(List<FDShape> selectedShapes, Point delta) {
		Map<FDShape, List<FDWire>> copiedShapesAndWires = new HashMap<FDShape, List<FDWire>>();
		
		Map<FDShape, FDShape> clonedShapesMap = new HashMap<FDShape, FDShape>();
		for ( FDShape source : selectedShapes ) {
			FDShape clonedSource = source.clone();
			Point loc = clonedSource.getLocation();
			clonedSource.setLocation(loc.x+delta.x, loc.y+delta.y);
			clonedShapesMap.put(source, clonedSource);
		}
		
		for ( FDShape source : selectedShapes ) {
			List<FDWire> clonedWires = new ArrayList<FDWire>();
			for ( FDWire wire : source.getOutgoingWires() ) {
				if ( selectedShapes.contains(wire.getTarget()) ) {
					FDWire clonedWire = (FDWire)wire.clone();
					clonedWire.applyBendpointsDelta(delta);
					clonedWire.setSource(clonedShapesMap.get(source));
					clonedWire.setTarget(clonedShapesMap.get(wire.getTarget()));
					clonedWires.add(clonedWire);
				}
			}
			
			copiedShapesAndWires.put(clonedShapesMap.get(source), clonedWires);
		}
		
		return copiedShapesAndWires;
	}
}
