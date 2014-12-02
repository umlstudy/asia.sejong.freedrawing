package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import java.util.ArrayList;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDWire;

public class FDShapeDeleteCommand extends Command
{
	private final FDRoot nodeRoot;
	private final FDShape target;
	
	private ArrayList<FDWire> removedSourceWires;
	private ArrayList<FDWire> removedTargetWires;

	public FDShapeDeleteCommand(FDRoot nodeRoot, FDShape target) {
		super("Delete Node");
		this.nodeRoot = nodeRoot;
		this.target = target;
		
		this.removedSourceWires = new ArrayList<FDWire>();
		this.removedTargetWires = new ArrayList<FDWire>();
	}

	/**
	 * Delete the connection
	 */
	@Override
	public void execute() {
		removedSourceWires.clear();
		removedTargetWires.clear();
		
		removedSourceWires.addAll(target.getIncommingWires());
		removedTargetWires.addAll(target.getOutgoingWires());
		
		nodeRoot.removeShape(target);
	}
	
	/**
	 * Restore the connection
	 */
	@Override
	public void undo() {
		
		nodeRoot.addShape(target);
		
		for ( FDWire wire : removedSourceWires ) {
			nodeRoot.addWire(wire);
		}
		
		for ( FDWire wire : removedTargetWires ) {
			nodeRoot.addWire(wire);
		}
	}
}