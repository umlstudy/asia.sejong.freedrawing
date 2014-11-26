package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.ConnectionAnchor;

public interface NodeFigure {

	ConnectionAnchor getSourceConnectionAnchor();
	
	ConnectionAnchor getTargetConnectionAnchor();
}
