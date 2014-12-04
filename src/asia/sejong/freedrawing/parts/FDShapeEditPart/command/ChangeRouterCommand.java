package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDRoot;

public class ChangeRouterCommand extends Command {
	
	private final FDRoot root;

	public ChangeRouterCommand(FDRoot root) {
		super("Change router");
		this.root = root;
	}
	
	@Override
	public void execute() {
		Integer connectionRouter = root.getConnectionRouter();
		if ( connectionRouter == FDRoot.ROUTER_MANUAL ) {
			root.setConnectionRouter(FDRoot.ROUTER_SHORTEST_PATH);
		} else {
			root.setConnectionRouter(FDRoot.ROUTER_MANUAL);
		}
	}
	
	@Override
	public void undo() {
		Integer connectionRouter = root.getConnectionRouter();
		if ( connectionRouter == FDRoot.ROUTER_MANUAL ) {
			root.setConnectionRouter(FDRoot.ROUTER_SHORTEST_PATH);
		} else {
			root.setConnectionRouter(FDRoot.ROUTER_MANUAL);
		}
	}
}
