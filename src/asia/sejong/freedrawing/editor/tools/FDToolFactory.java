package asia.sejong.freedrawing.editor.tools;

import java.io.ByteArrayInputStream;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDEllipse;
import asia.sejong.freedrawing.model.FDImage;
import asia.sejong.freedrawing.model.FDLabel;
import asia.sejong.freedrawing.model.FDModelFactory;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.util.IOUtil;

public abstract class FDToolFactory {

	public static FDToolFactory CONNECTION_CREATION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new FDConnectionCreationTool(editor);
		}
	};
	
	public static FDToolFactory PANNING_SELECTION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new FDPanningSelectionTool(editor);
		}
	};
	
	public static FDToolFactory MARQUEE_SELECTION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new FDMarqueeSelectionTool();
		}
	};

	public static FDToolFactory RECT_CREATION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(final FreedrawingEditor editor) {
			return new CreationTool(new CreationFactory() {
				
				@Override
				public Object getObjectType() {
					return FDRect.class;
				}
				
				@Override
				public Object getNewObject() {
					return FDModelFactory.createModel(FDRect.class);
				}
			});
		}
	};
	
	public static FDToolFactory ELLIPSE_CREATION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(final FreedrawingEditor editor) {
			return new CreationTool(new CreationFactory() {
				
				@Override
				public Object getObjectType() {
					return FDEllipse.class;
				}
				
				@Override
				public Object getNewObject() {
					return FDModelFactory.createModel(FDEllipse.class);
				}
			});
		}
	};
	
	public static FDToolFactory IMAGE_CREATION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(final FreedrawingEditor editor) {
			return new CreationTool(new CreationFactory() {
				
				@Override
				public Object getObjectType() {
					return FDImage.class;
				}
				
				@Override
				public Object getNewObject() {
					FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell());
					fileDialog.setFilterExtensions(new String[] {"*.png"});
					String fileLocation = fileDialog.open();
					if ( fileLocation == null ) {
						return null;
					}

					byte[] readBytes = null;
					try {
						readBytes = IOUtil.readAll(fileLocation);
						Display display = editor.getSite().getShell().getDisplay();
						// image validation test
						Image image = new Image(display, new ByteArrayInputStream(readBytes));
						image.dispose();
					} catch ( Exception e ) {
						IOUtil.throwException(e);
					}
						
					FDImage fdImage = (FDImage)FDModelFactory.createModel(FDImage.class);
					fdImage.setImageBytes(readBytes);
					return fdImage;
						
				}
			});
		}
	};
	
	public static FDToolFactory LABEL_CREATION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(final FreedrawingEditor editor) {
			return new CreationTool(new CreationFactory() {
				
				@Override
				public Object getObjectType() {
					return FDLabel.class;
				}
				
				@Override
				public Object getNewObject() {
					return FDModelFactory.createModel(FDLabel.class);
				}
			});
		}
	};

	public abstract AbstractTool createTool(FreedrawingEditor editor);
}
