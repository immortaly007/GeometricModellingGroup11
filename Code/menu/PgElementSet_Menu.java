package menu;

import jv.geom.PgPointSet_Menu;
import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.object.PsDialog;
import jv.object.PsObject;
import jv.project.PvDisplayIf;
import jv.project.PvViewerIf;
import jvx.project.PjWorkshop;
import jvx.project.PjWorkshop_Dialog;
import workshop.*;
import workshop.Assignment1.Task1;
import workshop.Assignment1.Task2;
import workshop.Assignment1.Task3IterativeAveraging;
import workshop.Assignment1.Task3MeanCurvatureFlow;

public class PgElementSet_Menu extends PgPointSet_Menu {
	
	private enum Assignment1{
		Task1                   ("Task 1..."),
		Task2                   ("Task 2..."),
		Task3IterativeAveraging ("Task 3 (Iterative Averaging)..."),
		Task3MeanCurvatureFlow  ("Task 3 (Mean Curvature Flow)..."),
		// Additional entries...
		;
		
		protected final String name;
        Assignment1(String name) { this.name  = name; }
		
		public static Assignment1 fromName(String name){
			for (Assignment1 entry : Assignment1.values()) {
				if(entry.name.equals(name)) return entry;
			}
			return null;
		}
	}

    private enum Assignment2{
        Task1                   ("Task 1 (Gradients)"),
        Task2                   ("Task 2 (Mesh editing)"),
        // Additional entries...
        ;

        protected final String name;
        Assignment2(String name) { this.name  = name; }

        public static Assignment2 fromName(String name){
            for (Assignment2 entry : Assignment2.values()) {
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
		
		String assignment1Menu = "Assignment 1";
		addMenu(assignment1Menu);
		for (Assignment1 entry : Assignment1.values()) {
			addMenuItem(assignment1Menu, entry.name);
		}

        String assignment2Menu = "Assignment 2";
        addMenu(assignment2Menu);
        for (Assignment2 entry : Assignment2.values()) {
            addMenuItem(assignment2Menu, entry.name);
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

		Assignment1 entry = Assignment1.fromName(aMethod);
		if(entry == null) return false;
		switch (entry) {
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