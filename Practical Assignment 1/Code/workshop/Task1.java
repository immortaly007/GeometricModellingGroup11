package workshop;

import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.project.PjWorkshop;

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
        m_geom.getElements();
        m_geom.getEdgeStars();

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

            double innerCircle = 0.5 * Math.sqrt(((b + c - a) * (c + a - b) * (a + b - c)) / (a + b + c));
            double outerCircle = (a * b * c) / Math.sqrt((a + b + c) * (b + c - a) * (c + a - b) * (a + b - c));
            if (a == 0 || b == 0 || c == 0)
            {
                innerCircle = 0;
                outerCircle = Math.max(Math.max(a, b), c) / 2.0;
            }
            double shapeRegularity = innerCircle / outerCircle;
            double s = (a + b + c) / 2.0;
            double shapeRegularity2 = (4.0 * (s - a) * (s - b) * (s - c)) / (a * b * c);

            PsDebug.message("a=" + a + ", b=" + b + ", c=" + c + ", innerCircle=" + innerCircle + ", outerCircle=" + outerCircle + ", sr=" + shapeRegularity + ", sr2=" + shapeRegularity2);
            shapeRegularities.add(shapeRegularity);
        }
        m_geom.getArea();
        PsDebug.message("Ik zal ff comitten");
    }

}
