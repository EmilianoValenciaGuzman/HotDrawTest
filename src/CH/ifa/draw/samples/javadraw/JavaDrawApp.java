/*
 * @(#)JavaDrawApp.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	  by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.samples.javadraw;


import CH.ifa.draw.framework.*;
import CH.ifa.draw.samples.pert.PertDependency;
import CH.ifa.draw.samples.pert.PertFigure;
import CH.ifa.draw.samples.pert.PertFigureCreationTool;
import CH.ifa.draw.standard.*;
import CH.ifa.draw.figures.*;
import CH.ifa.draw.util.*;
import CH.ifa.draw.application.*;
import CH.ifa.draw.contrib.*;
import CH.ifa.draw.contrib.zoom.ZoomDrawingView;

import javax.swing.*;
import java.io.*;
import java.net.URL;

/**
 * @version <$CURRENT_VERSION$>
 */
public  class JavaDrawApp extends MDI_DrawApplication {

	private Animator            fAnimator;
	private static String       fgSampleImagesPath = "/CH/ifa/draw/samples/javadraw/sampleimages";
	private static String       fgSampleImagesResourcePath = fgSampleImagesPath + "/";

	JavaDrawApp() {
		super("JHotDraw");
	}

	/**
	 * Expose constructor for benefit of subclasses.
	 * 
	 * @param title The window title for this application's frame.
	 */
	public JavaDrawApp(String title) {
		super(title);
	}

	/**
	 * Factory method which create a new instance of this
	 * application.
	 *
	 * @return	newly created application
	 */
	protected DrawApplication createApplication() {
		return new JavaDrawApp();
	}

	protected DrawingView createDrawingView() {
		return new ZoomDrawingView(this);
	}

	//-- application life cycle --------------------------------------------

	public void destroy() {
		super.destroy();
		endAnimation();
	}

	//-- DrawApplication overrides -----------------------------------------
	private final static String PERTIMAGES = "/CH/ifa/draw/samples/pert/images/";

	protected void createTools(JToolBar palette) {
		super.createTools(palette);
                
		/*Tool tool = new UndoableTool(new TextTool(this, new TextFigure()));
		palette.add(createToolButton(IMAGES + "ATEXT", "Connected Text Tool", tool));
		tool = new CreationTool(this, new PertFigure());
		palette.add(createToolButton(IMAGES + "ATEXT", "Forma Entidad", tool));

		*/
		Tool tool = new TextTool(this, new TextFigure());
		palette.add(createToolButton(IMAGES+"TEXT", "Text Tool", tool));

		tool = new EntityRelationshipTool(this);
		palette.add(createToolButton(PERTIMAGES+"PERT", "Task Tool", tool));

		tool = new ConnectionTool(this, new MToMConnection());
		palette.add(createToolButton(IMAGES + "CONN", "One to One Coneection", tool));

		//tool = new ConnectionTool(this, new ElbowConnection());
		//palette.add(createToolButton(IMAGES + "OCONN", "Elbow Connection Tool", tool));

	}

	protected Tool createSelectionTool() {
		return new MySelectionTool(this);
	}

	protected void createMenus(JMenuBar mb) {
		super.createMenus(mb);
		addMenuIfPossible(mb, createAnimationMenu());
		addMenuIfPossible(mb, createImagesMenu());
		addMenuIfPossible(mb, createWindowMenu());
	}

	protected JMenu createAnimationMenu() {
		CommandMenu menu = new CommandMenu("Animation");
		Command cmd = new AbstractCommand("Start Animation", this) {
			public void execute() {
				startAnimation();
			}
		};
		menu.add(cmd);

		cmd = new AbstractCommand("Stop Animation", this) {
			public void execute() {
				endAnimation();
			}
		};
		menu.add(cmd);
		return menu;
	}

	protected JMenu createWindowMenu() {
		CommandMenu menu = new CommandMenu("Window");
		Command cmd = new AbstractCommand("New View", this) {
			public void execute() {
				newView();
			}
		};
		menu.add(cmd);

		cmd = new AbstractCommand("New Window", this, false) {
			public void execute() {
				newWindow(createDrawing());
			}
		};
		menu.add(cmd);

		menu.addSeparator();
		menu.add(new WindowMenu("Window List", (MDIDesktopPane)getDesktop(), this));
		return menu;
	}

	protected JMenu createImagesMenu() {
		CommandMenu menu = new CommandMenu("Images");
		URL url = getClass().getResource(fgSampleImagesPath);
		File imagesDirectory = new File(url.getFile());

		try {
			String[] list = imagesDirectory.list();
			for (int i = 0; i < list.length; i++) {
				String name = list[i];
				String path = fgSampleImagesResourcePath+name;
				menu.add(new UndoableCommand(
					new InsertImageCommand(name, path, this)));
			}
		}
		catch (Exception e) {
			// do nothing
		}
		return menu;
	}

	protected Drawing createDrawing() {
		Drawing dwg = new BouncingDrawing();
        dwg.setTitle(getDefaultDrawingTitle());
		return dwg;
		//return new StandardDrawing();
	}

	//---- animation support --------------------------------------------

	public void startAnimation() {
		if (view().drawing() instanceof Animatable && fAnimator == null) {
			fAnimator = new Animator((Animatable)view().drawing(), view());
			fAnimator.start();
		}
	}

	public void endAnimation() {
		if (fAnimator != null) {
			fAnimator.end();
			fAnimator = null;
		}
	}

	protected JMenu createDebugMenu() {
		CommandMenu menu = (CommandMenu)super.createDebugMenu();

		Command cmd = new AbstractCommand("Clipping Update", this) {
			public void execute() {
				this.view().setDisplayUpdate(new ClippingUpdateStrategy());
			}
		};
		menu.add(cmd);

		return menu;
	}

	//-- main -----------------------------------------------------------

	public static void main(String[] args) {
		JavaDrawApp window = new JavaDrawApp();
		window.open();
	}
}
