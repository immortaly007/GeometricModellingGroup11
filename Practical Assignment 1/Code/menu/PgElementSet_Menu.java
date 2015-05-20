package menu;

import jv.geom.PgPointSet_Menu;
import jv.geom.PgElementSet;
import jv.geom.PgPointSet;
import jv.object.PsDebug;
import jv.object.PsDialog;
import jv.objectGui.PsMethodMenu;
import jv.object.PsObject;
import jv.project.PgGeometryIf;
import jv.project.PvDisplayIf;
import jv.project.PvViewerIf;
import jv.vecmath.PdVector;
import jvx.project.PjWorkshop;
import jvx.project.PjWorkshop_Dialog;
import workshop.*;

public class PgElementSet_Menu extends PgPointSet_Menu {
	
	private enum MenuEntry{
		MyWorkshop              ("MyWorkshop..."),
		Task1                   ("Task 1..."),
		Task2                   ("Task 2..."),
		Task3IterativeAveraging ("Task 3 (Iterative Averaging)..."),
		Task3MeanCurvatureFlow  ("Task 3 (Mean Curvature Flow)..."),
		// Additional entries...
		;
		
		protected final String name;
		MenuEntry(String name) { this.name  = name; }
		
		public static MenuEntry fromName(String name){
			for (MenuEntry entry : MenuEntry.values()) {
				if(entry.name.equals(name)) return entry;
			}
			return null;
		}
	}	
	
	protected PgElementSet m_elementSet;
	
	protected PvViewerIf m_viewer;

	public void init(PsObject anObject) {
		super.init(anObject);
		m_elementSet = (PgElementSet)anObject;
		
		String menuDev = "My Workshops";
		addMenu(menuDev);
		for (MenuEntry entry : MenuEntry.values()) {
			addMenuItem(menuDev, entry.name);
		}
	}
	
	public boolean applyMethod(String aMethod) {
		if (super.applyMethod(aMethod))
			return true;

		if (PsDebug.NOTIFY) PsDebug.notify("trying method = "+aMethod);

		PvDisplayIf currDisp = null;
		if (getViewer() == null) {
			if (PsDebug.WARNING) PsDebug.warning("missing viewer");
		} else {
			currDisp = getViewer().getDisplay();
			if (currDisp == null) PsDebug.warning("missing display.");
		}

		MenuEntry entry = MenuEntry.fromName(aMethod);
		if(entry == null) return false;
		switch (entry) {
			case MyWorkshop:
				ShowShizzle(currDisp, new MyWorkshop());
				break;
			case Task1:
				ShowShizzle(currDisp, new Task1());
				break;
			case Task2:
				ShowShizzle(currDisp, new Task2());
				break;
			case Task3IterativeAveraging:
				ShowShizzle(currDisp, new Task3IterativeAveraging());
				break;
			case Task3MeanCurvatureFlow:
				ShowShizzle(currDisp, new Task3MeanCurvatureFlow());
				break;
			// Additional entries...

		}
		
		return true;
	}

	public void ShowShizzle(PvDisplayIf currDisp, PjWorkshop toshow) {
		toshow.setGeometry(m_elementSet);
		if (currDisp == null) {
			if (PsDebug.WARNING) PsDebug.warning("missing display.");
		} else
			toshow.setDisplay(currDisp);
		PsDialog dialog;
		dialog = new PjWorkshop_Dialog(false);
		dialog.setParent(toshow);
		dialog.update(toshow);
		dialog.setVisible(true);
	}
	
	public PvViewerIf getViewer() { return m_viewer; }

	public void setViewer(PvViewerIf viewer) { m_viewer = viewer; }		
	
}	