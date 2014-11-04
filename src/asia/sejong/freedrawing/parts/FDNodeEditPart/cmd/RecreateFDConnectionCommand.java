package asia.sejong.freedrawing.parts.FDNodeEditPart.cmd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

public class RecreateFDConnectionCommand extends CreateFDConnectionCommand {

	private List<Point> bendpoints;
	
	public RecreateFDConnectionCommand(List<Point> bendpoints) {
		setLabel("Create " + getConnectionName());
		
		this.bendpoints = new ArrayList<Point>(bendpoints);
	}

	public void execute() {
		source.addTarget(target, bendpoints);
	}
	
	public void undo() {
		source.removeTarget(target);
	}

	public String getConnectionName() {
		return "BendpointConnection";
	}
//	
//	public FDNode getTarget() {
//		return target;
//	}
//
//	public FDNode getSource() {
//		return source;
//	}
}
