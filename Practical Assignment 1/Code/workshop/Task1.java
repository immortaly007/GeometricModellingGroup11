package workshop;

import jv.geom.PgEdgeStar;
import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.project.PjWorkshop;
import util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class Task1 extends PjWorkshop{
    PgElementSet m_geom;
    PgElementSet m_geomSave;

    public Task1() {
        super("Task 1");
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

    public void calculate() {
        // Calculate all shape regularities
        ArrayList<Double> shapeRegularities = new ArrayList<Double>();

        int n = m_geom.getNumElements();
        for (int i = 0; i < n; i++) {
        	PiVector e = m_geom.getElement(i);

        	if (e.getSize() < 3) {
        		PsDebug.error("Expected number of elements >= 3");
        		return;
        	}
        	if (e.getSize() > 4) {
        		PsDebug.error("Expected number of elements <= 4");
        		return;
        	}

            PdVector v1 = m_geom.getVertex(e.getEntry(0));
            PdVector v2 = m_geom.getVertex(e.getEntry(1));
            PdVector v3 = m_geom.getVertex(e.getEntry(2));
			double sr = calculateShapeRegularity(v1, v2, v3);
            shapeRegularities.add(sr);

            if (e.getSize() == 4) {
	            PdVector v4 = m_geom.getVertex(e.getEntry(3));
	            sr = calculateShapeRegularity(v3, v4, v1);
	            shapeRegularities.add(sr);
            }

        }
        EverythingHelper.filterNaN(shapeRegularities);
        PsDebug.message("Shape regularity: " + EverythingHelper.toSummaryString(shapeRegularities));

        // Calculate all valences
        ArrayList<Integer> valences = new ArrayList<Integer>();
        PgEdgeStar[] edgeStars = m_geom.makeEdgeStars();
        for (PgEdgeStar star : edgeStars)
        {
            valences.add(star.getValence());
        }

        PsDebug.message("Valences        : " + EverythingHelper.toSummaryString(valences));
    }

    protected double calculateShapeRegularity(PdVector v1, PdVector v2, PdVector v3) {
		PdVector cache = new PdVector();
		cache.sub(v2, v1); double a = cache.length();
		cache.sub(v3, v2); double b = cache.length();
		cache.sub(v1, v3); double c = cache.length();
		double s = (a + b + c)/2.0;
		return 4.0*(s - a)*(s - b)*(s - c)/(a*b*c);
    }
}
