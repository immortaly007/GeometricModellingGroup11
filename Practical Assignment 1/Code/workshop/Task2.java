package workshop;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.project.PjWorkshop;
import util.Util;

public class Task2 extends PjWorkshop {

    PgElementSet m_geom;
    PgElementSet m_geomSave;

    public Task2() {
        super("Task 2");
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
        int g = genus();

        double area = 0;
        for (int i = 0; i < m_geom.getNumElements(); i++) {
            area += elementArea(m_geom.getElement(i));
        }

        ArrayList<PdVector> meanCurvatures = new ArrayList<PdVector>();
        PgVertexStar[] stars = Util.getVertexStars(m_geom);
        int index = 0;
        for (PgVertexStar star : stars) {
            index++;
            meanCurvatures.add(meanCurvature(star));
        }

        PsDebug.message("genus = " + genus());
        PsDebug.message("area = " + area);
    }

    private double elementArea(PiVector elem) {
        if (elem.getSize() == 3) {
            PdVector v1 = m_geom.getVertex(elem.getEntry(0));
            PdVector v2 = m_geom.getVertex(elem.getEntry(1));
            PdVector v3 = m_geom.getVertex(elem.getEntry(2));
            return triangleArea(v1,v2,v3);
        } else {
            PdVector v1 = m_geom.getVertex(elem.getEntry(0));
            PdVector v2 = m_geom.getVertex(elem.getEntry(1));
            PdVector v3 = m_geom.getVertex(elem.getEntry(2));
            PdVector v4 = m_geom.getVertex(elem.getEntry(3));
            return triangleArea(v1,v2,v3) + triangleArea(v1,v3,v4);
        }
    }

    private PdVector meanCurvature(PgVertexStar star) {
        double area = 0;
        for (int e : star.getElement().getEntries()) {
            area += elementArea( m_geom.getElement(e));
        }

        PdVector curvature = new PdVector(0,0,0);
        curvature.multScalar(3.0 / 2.0 * area);

        return curvature;
    }

    private double triangleArea(PdVector v1, PdVector v2, PdVector v3) {
        // source: http://en.wikipedia.org/wiki/Triangle#Computing_the_area_of_a_triangle
        double a = PdVector.dist(v1, v2);
        double b = PdVector.dist(v2, v3);
        double c = PdVector.dist(v1, v3);
        double s = (a + b + c) * 0.5;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    private int genus() {
        float v = m_geom.getNumVertices();
        float e = m_geom.getNumEdges();
        float f = m_geom.getNumElements();
        float euler = (v - e + f);
        return (int)(-1.0 * (euler / 2.0f - 1.0f));
    }
}