package asia.sejong.freedrawing.parts.FDNodeEditPart.cmd;

import java.util.Set;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;

public class DeleteFDNodeCommand extends Command
{
	private final FDNodeRoot nodeRoot;
	private final FDNode target;
	
	private Set<FDNode> removedSource;
	private Set<FDNode> removedTarget;

	public DeleteFDNodeCommand(FDNodeRoot nodeRoot, FDNode target) {
		super("Delete Node");
		this.nodeRoot = nodeRoot;
		this.target = target;
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		
		removedSource = target.getSources();
		for ( FDNode source : removedSource ) {
			source.removeTarget(target);
		}
		
		removedTarget = target.getTargets();
		for ( FDNode targetOfTarget : removedTarget ) {
			target.removeTarget(targetOfTarget);
		}
		
		nodeRoot.removeNode(target);
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
		
		nodeRoot.addNode(target);
		
		for ( FDNode source : removedSource ) {
			source.addTarget(target);
		}
		
		removedTarget = target.getTargets();
		for ( FDNode targetOfTarget : removedTarget ) {
			target.addTarget(targetOfTarget);
		}
	}
}