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

public class Task2 extends PjWorkshop {

    PgElementSet m_geom;
    PgElementSet m_triangulated;

    public Task2() {
        super("Task 2");
        init();
    }

    @Override
    public void setGeometry(PgGeometry geom) {
        super.setGeometry(geom);
        m_geom 		= (PgElementSet)super.m_geom;
        m_triangulated = (PgElementSet)super.m_geom.clone();
        PgElementSet.triangulate(m_triangulated);

    }

    public void init() {
        super.init();
    }

    public void calculate() {
        m_geom.assureVertexColors();
        int g = genus();

        double area = 0;
        for (int i = 0; i < m_triangulated.getNumElements(); i++) {
            area += EverythingHelper.triangleElementArea(m_triangulated.getElement(i), m_triangulated);
        }

        ArrayList<Double> meanCurvatures = new ArrayList<>();
        PgVertexStar[] stars = Util.getVertexStars(m_triangulated);

        double maxLength = 0;
        for (int i = 0; i < stars.length; i++) {
            PgVertexStar star = stars[i];
            PdVector curvature = EverythingHelper.meanCurvature(star, m_triangulated);
            double length = curvature.length();
            if (length > maxLength) maxLength = length;
            meanCurvatures.add(length);
        }

        ArrayList<Double> meanCurvatures_copy = new ArrayList<>(meanCurvatures);
        EverythingHelper.filterNaN(meanCurvatures);
        EverythingHelper.filterInfinity(meanCurvatures);

        float min = EverythingHelper.min(meanCurvatures).floatValue();
        float max = EverythingHelper.max(meanCurvatures).floatValue();
        float avg = EverythingHelper.mean(meanCurvatures).floatValue();
        float std = EverythingHelper.std(meanCurvatures).floatValue();
        //PsDebug.message(min + " ; " + max + " ; " + avg + " ; " + std);
        min = Math.max(min, avg - std * 3f);
        max = Math.min(max, avg + std * 3f);

        for (int i = 0; i < stars.length; i++) {
            float value = EverythingHelper.clamp((meanCurvatures_copy.get(i).floatValue() - min) / (max - min), 0f, 1f);
            m_geom.setVertexColor(i, new java.awt.Color(value, value, value));
        }

        PsDebug.message("genus = " + genus());
        PsDebug.message("area = " + area);
        PsDebug.message("Mean curvatures        : " + EverythingHelper.toSummaryString(meanCurvatures));

        m_geom.showElementColors(true);
        m_geom.showVertexColors(true);
        m_geom.showElementColorFromVertices(true);
        m_geom.showSmoothElementColors(true);
    }

    private int genus() {
        float v = m_geom.getNumVertices();
        float e = m_geom.getNumEdges();
        float f = m_geom.getNumElements();
        float euler = (v - e + f);
        return (int)(-1.0 * (euler / 2.0f - 1.0f));
    }
}