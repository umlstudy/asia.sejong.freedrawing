package asia.sejong.freedrawing.parts.FDNodeEditPart.command;

import java.util.ArrayList;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;

public class DeleteFDNodeCommand extends Command
{
	private final FDRoot nodeRoot;
	private final FDRect target;
	
//	private Set<FDRect> removedSources;
	private ArrayList<FDWire> removedSourceWires;
//	private Set<FDRect> removedTargets;
	private ArrayList<FDWire> removedTargetWires;

	public DeleteFDNodeCommand(FDRoot nodeRoot, FDRect target) {
		super("Delete Node");
		this.nodeRoot = nodeRoot;
		this.target = target;
		
		this.removedSourceWires = new ArrayList<FDWire>();
		this.removedTargetWires = new ArrayList<FDWire>();
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		removedSourceWires.clear();
		removedTargetWires.clear();
		
//		removedSources = target.getSources();
		removedSourceWires.addAll(target.getIncommingWires());
		
//		removedTargets = target.getTargets();
		removedTargetWires.addAll(target.getOutgoingWires());
//		for ( FDRect targetOfTarget : target.getTargets() ) {
//			removedTargetWires.put(targetOfTarget, target.removeTarget(targetOfTarget));
//		}
		
		nodeRoot.removeNode(target);
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
		
		nodeRoot.addNode(target);
		
		for ( FDWire wire : removedSourceWires ) {
			nodeRoot.addWire(wire);
		}
		
		for ( FDWire wire : removedTargetWires ) {
			nodeRoot.addWire(wire);
		}
	}
}