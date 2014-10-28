package asia.sejong.freedrawing.parts.FDNodeEditPart.cmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;

public class DeleteFDNodeCommand extends Command
{
	private final FDNodeRoot nodeRoot;
	private final FDNode target;
	
	private Set<FDNode> removedSources;
	private Map<FDNode, List<Point>> removedSourcePoints;
	private Set<FDNode> removedTargets;
	private Map<FDNode, List<Point>> removedTargetPoints;

	public DeleteFDNodeCommand(FDNodeRoot nodeRoot, FDNode target) {
		super("Delete Node");
		this.nodeRoot = nodeRoot;
		this.target = target;
		
		this.removedSourcePoints = new HashMap<FDNode, List<Point>>();
		this.removedTargetPoints = new HashMap<FDNode, List<Point>>();
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		removedSourcePoints.clear();
		removedTargetPoints.clear();
		
		removedSources = target.getSources();
		for ( FDNode source : removedSources ) {
			removedSourcePoints.put(source, source.removeTarget(target));
		}
		
		removedTargets = target.getTargets();
		for ( FDNode targetOfTarget : removedTargets ) {
			removedTargetPoints.put(targetOfTarget, target.removeTarget(targetOfTarget));
		}
		
		nodeRoot.removeNode(target);
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
		
		nodeRoot.addNode(target);
		
		for ( FDNode source : removedSources ) {
			source.addTarget(target, removedSourcePoints.get(target));
		}
		
		for ( FDNode targetOfTarget : removedTargets ) {
			target.addTarget(targetOfTarget, removedTargetPoints.get(targetOfTarget));
		}
	}
}