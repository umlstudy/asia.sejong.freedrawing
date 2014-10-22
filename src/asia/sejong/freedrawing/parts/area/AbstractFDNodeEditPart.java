package asia.sejong.freedrawing.parts.area;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import asia.sejong.freedrawing.commands.CreateConnectionCommand;
import asia.sejong.freedrawing.model.area.AbstractFDElement;
import asia.sejong.freedrawing.parts.connection.AbstractFDConnectionEditPart;
import asia.sejong.freedrawing.util.DebugUtil;

public abstract class AbstractFDNodeEditPart  extends AbstractFDElementEditPart implements NodeEditPart {
	
	public AbstractFDElement getModel() {
		return (AbstractFDElement) super.getModel();
	}
	
	/**
	 * Return an instance of {@link ChopboxAnchor} so that the connection originates along
	 * the {@link PersonFigure}'s bounding box. This is called once a connection has been
	 * established.
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	/**
	 * Return an instance of {@link ChopboxAnchor} so that the connection creation
	 * originates along the {@link PersonFigure}'s bounding box. 
	 * This is called when the user is interactively creating a connection.
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			EditPart part = ((ReconnectRequest) request).getConnectionEditPart();
			if (!(part instanceof ConnectionEditPart)) {
				return null;
			}
			
			AbstractFDConnectionEditPart connPart = (AbstractFDConnectionEditPart) part;
			CreateConnectionCommand connCmd = connPart.recreateCommand();
			if (!connCmd.isValidSource(getModel())) {
				return null;
			}
			return new ChopboxAnchor(getFigure());
		} else {
			// 货肺款 目池记
			return new ChopboxAnchor(getFigure());
		}
	}

	/**
	 * Return an instance of {@link ChopboxAnchor} so that the connection terminates along
	 * the {@link PersonFigure}'s bounding box. This is called once a connection has been
	 * established.
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	/**
	 * If this request is creating a connection from a {@link MarriageEditPart} to the
	 * receiver, then return an instance of {@link ChopboxAnchor} so that the connection
	 * creation snaps to the figure and terminates along the {@link PersonFigure}'s
	 * bounding box. If the connection source is NOT from a {@link MarriageEditPart} then
	 * return <code>null</code> so that the connection does not appear to connect to the receiver. 
	 * This is called when the user is interactively creating a connection.
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (request instanceof CreateConnectionRequest) {
			Command cmd = ((CreateConnectionRequest) request).getStartCommand();
			if (!(cmd instanceof CreateConnectionCommand))
				return null;
			System.out.println(this.getClass().getSimpleName() + "_________ " + this.hashCode() );
			if (!((CreateConnectionCommand) cmd).isValidTarget(getModel()))
				return null;
			return new ChopboxAnchor(getFigure());
		}
		if (request instanceof ReconnectRequest) {
			EditPart part = ((ReconnectRequest) request).getConnectionEditPart();
			if (!(part instanceof AbstractFDConnectionEditPart))
				return null;
			AbstractFDConnectionEditPart connPart = (AbstractFDConnectionEditPart) part;
			CreateConnectionCommand connCmd = connPart.recreateCommand();
			if (!connCmd.isValidTarget(getModel()))
				return null;
			return new ChopboxAnchor(getFigure());
		}
		return null;
	}
	
	public Command getCommand(Request request) {
		DebugUtil.printLogStart();
		System.out.println(" EditPart ? " + this.getClass().getSimpleName() + "**** "+ this.hashCode());
		return super.getCommand(request);
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can add itself as a listener to the underlying model object
	 */
	public void addNotify() {
		super.addNotify();
//		getModel().addFDConnectionListener(this);
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can stop listening to events from the underlying model object
	 */
	public void removeNotify() {
//		getModel().removeFDConnectionListener(this);
		super.removeNotify();
	}
}