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
        for (PiVector elem : m_geom.getElements())
        {
            PdVector v1 = m_geom.getVertex(elem.getEntry(0));
            PdVector v2 = m_geom.getVertex(elem.getEntry(1));
            PdVector v3 = m_geom.getVertex(elem.getEntry(2));

            PdVector cache = new PdVector();
            cache.sub(v2, v1); double a = cache.length();
            cache.sub(v3, v2); double b = cache.length();
            cache.sub(v3, v1); double c = cache.length();

            double s = (a + b + c) / 2.0;
            double shapeRegularity = (4.0 * (s - a) * (s - b) * (s - c)) / (a * b * c);
            if (a == 0 || b == 0 || c == 0)
                shapeRegularity = 0;

            PsDebug.message("a=" + a + ", b=" + b + ", c=" + c + ", sr=" + shapeRegularity);
            shapeRegularities.add(shapeRegularity);
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

}
