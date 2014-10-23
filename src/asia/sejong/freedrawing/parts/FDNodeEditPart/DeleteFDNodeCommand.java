package asia.sejong.freedrawing.parts.FDNodeEditPart;

import java.util.Set;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;

public class DeleteFDNodeCommand extends Command
{
	private final FDNodeRoot nodeRoot;
	private final FDNode target;

	public DeleteFDNodeCommand(FDNodeRoot nodeRoot, FDNode target) {
		super("Delete Node");
		this.nodeRoot = nodeRoot;
		this.target = target;
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		
		Set<FDNode> sources = target.getSources();
		for ( FDNode source : sources ) {
			source.removeTarget(target);
		}
		
		Set<FDNode> targets = target.getTargets();
		for ( FDNode targetOfTarget : targets ) {
			target.removeTarget(targetOfTarget);
		}
		
		nodeRoot.removeNode(target);
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
	}
}