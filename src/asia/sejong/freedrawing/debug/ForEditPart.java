package asia.sejong.freedrawing.debug;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

public class ForEditPart {

	public static void traceRequest(EditPart editPart, Request request, Command command) {
		System.out.println(String.format("DEBUG >> Source : %s, Request : %s, Command : %s", editPart.getClass().getSimpleName(), request.getType(), command == null ? "null" : command.getLabel()));		
	}
}
