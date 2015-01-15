package asia.sejong.freedrawing.parts.FDTextShapeEditPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import asia.sejong.freedrawing.context.ApplicationContext;
import asia.sejong.freedrawing.figures.FDTextShapeFigure;
import asia.sejong.freedrawing.model.FDTextShape;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.model.listener.TextShapeListener;
import asia.sejong.freedrawing.parts.FDShapeEditPart.FDShapeEditPart;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.command.TextChangeCommand;

public abstract class FDTextShapeEditPart extends FDShapeEditPart implements TextShapeListener {
	
	@Override
	public FDTextShape getModel() {
		return (FDTextShape) super.getModel();
	}
	
	@Override
	protected void refreshVisuals() {
		FDTextShape m = (FDTextShape) getModel();
		
		((FDTextShapeFigure)getFigure()).setTextEx(m.getText());
		((FDTextShapeFigure)getFigure()).setFontInfoEx(m.getFontInfo());
		((FDTextShapeFigure)getFigure()).setFontColorEx(m.getFontColor());
		
		super.refreshVisuals();
	}
	
	@Override
	protected void createEditPolicies() {
		
		super.createEditPolicies();
		
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DirectEditPolicy() {
			
			@Override
			protected void showCurrentEditValue(DirectEditRequest request) {
			}
			
			@Override
			protected Command getDirectEditCommand(DirectEditRequest request) {
				String newValue = (String)request.getCellEditor().getValue();
				if ( !newValue.equals(getModel().getText() ) ) {
					return new TextChangeCommand(getModel(), newValue);
				}
				return null;
			}
		});
	}
	
	@Override
	public void performRequest(Request req) {
		if ( req != null && req.getType().equals(RequestConstants.REQ_DIRECT_EDIT) ) {
			
			DirectEditManager dem = new DirectEditManager(this, null, null) {
				
				@Override
				protected void initCellEditor() {
					String text = ((FDTextShape) getModel()).getText();
					if ( text != null ) {
						getCellEditor().setValue(text);
					}
				}
				
				@Override
				protected CellEditor createCellEditorOn(Composite parent) {
					TextCellEditor cellEditor = new TextCellEditor(parent, SWT.MULTI | SWT.CENTER);
					Text text = (Text)cellEditor.getControl();
					Font font = null;
					FontInfo fontInfo = getModel().getFontInfo();
					if ( fontInfo != null ) {
						font = ApplicationContext.getInstance().getFontManager().get(fontInfo);
						text.setFont(font);
					}
					
					return cellEditor;
				}
			};
			
			dem.setLocator(new CellEditorLocator() {
				
				@Override
				public void relocate(final CellEditor celleditor) {
					final IFigure figure = getFigure();
					final Text text = (Text) celleditor.getControl();
					final Rectangle rect = figure.getBounds();
//					figure.translateToAbsolute(rect);
//					final Point size = text.computeSize(SWT.DEFAULT,
//							SWT.DEFAULT, true);
//					final org.eclipse.swt.graphics.Rectangle trim = text
//							.computeTrim(0, 0, 0, 0);
//					rect.translate(trim.x, trim.y);
//					rect.width = Math.max(size.x, rect.width);
//					rect.width += trim.width;
//					rect.height += trim.height;
//					text.setBounds(rect.x, rect.y, rect.width, rect.height);
					text.setBounds(rect.x, rect.y, rect.width, rect.height);
				}
			});
			dem.show();
		}
	}
	
	// ==========================================================================
	// FDTextShapeListener
	
	@Override
	public final void textChanged(String newText) {
		((FDTextShapeFigure)getFigure()).setTextEx(newText);
		refreshVisuals();
	}

	@Override
	public final void fontChanged(FontInfo fontInfo) {
		((FDTextShapeFigure)getFigure()).setFontInfoEx(fontInfo);
		refreshVisuals();
	}
	
	@Override
	public final void fontColorChanged(RGB fontColor) {
		((FDTextShapeFigure)getFigure()).setFontColorEx(fontColor);
		refreshVisuals();
	}
}
