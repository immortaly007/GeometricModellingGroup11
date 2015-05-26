package workshop;

import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.project.PjWorkshop;
import util.Util;

import java.util.ArrayList;

/**
 * Created by Immortaly007 on 20-5-2015.
 */
public class Task3MeanCurvatureFlow extends PjWorkshop {
    PgElementSet m_geom;
    PgElementSet m_geomSave;

    public Task3MeanCurvatureFlow() {
        super("Task 3");
        init();
    }

    @Override
    public void setGeometry(PgGeometry geom) {
        super.setGeometry(geom);
        m_geom 		= (PgElementSet)super.m_geom;
        m_geomSave 	= (PgElementSet)super.m_geomSave;
    }

    public void init() {
        super.init();
    }

    public void apply(double stepwidth)
    {
        // Triangulate the geometry
        PgElementSet.triangulate(m_geom);

        // Calculate the mean curvature for every vertex
        ArrayList<PdVector> meanCurvatures = EverythingHelper.calculateMeanCurvatures(m_geom);
    }
}
