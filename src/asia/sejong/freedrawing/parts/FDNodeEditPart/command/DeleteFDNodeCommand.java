package asia.sejong.freedrawing.parts.FDNodeEditPart.command;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;

public class DeleteFDNodeCommand extends Command
{
	private final FDRoot nodeRoot;
	private final FDRect target;
	
//	private Set<FDRect> removedSources;
	private Map<FDRect, FDWire> removedSourceWires;
//	private Set<FDRect> removedTargets;
	private Map<FDRect, FDWire> removedTargetWires;

	public DeleteFDNodeCommand(FDRoot nodeRoot, FDRect target) {
		super("Delete Node");
		this.nodeRoot = nodeRoot;
		this.target = target;
		
		this.removedSourceWires = new HashMap<FDRect, FDWire>();
		this.removedTargetWires = new HashMap<FDRect, FDWire>();
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		removedSourceWires.clear();
		removedTargetWires.clear();
		
//		removedSources = target.getSources();
		for ( FDRect source : target.getSources() ) {
			removedSourceWires.put(source, source.removeTarget(target));
		}
		
//		removedTargets = target.getTargets();
		removedTargetWires.putAll(target.getTargets());
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
		
		for ( FDRect source : removedSourceWires.keySet() ) {
			source.addTarget(target, removedSourceWires.get(source));
		}
		
		for ( FDRect targetOfTarget : removedTargetWires.keySet() ) {
			target.addTarget(targetOfTarget, removedTargetWires.get(targetOfTarget));
		}
	}
}