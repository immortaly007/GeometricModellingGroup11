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
            area += elementArea(m_triangulated.getElement(i));
        }

        ArrayList<Double> meanCurvatures = new ArrayList<>();
        PgVertexStar[] stars = Util.getVertexStars(m_triangulated);

        double maxLength = 0;
        for (int i = 0; i < stars.length; i++) {
            PgVertexStar star = stars[i];
            PdVector curvature = meanCurvature(star);
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

    private double elementArea(PiVector elem) {
        if (elem.getSize() != 3) {
            PsDebug.message("Was geometry triangulated?");
        }
        PdVector v1 = m_triangulated.getVertex(elem.getEntry(0));
        PdVector v2 = m_triangulated.getVertex(elem.getEntry(1));
        PdVector v3 = m_triangulated.getVertex(elem.getEntry(2));
        return triangleArea(v1,v2,v3);
    }

    private PdVector meanCurvature(PgVertexStar star) {
        double area = 0;
        for (int e : star.getElement().getEntries()) {
            area += elementArea( m_triangulated.getElement(e));
        }

        // initialize
        PdVector curvature = new PdVector(0,0,0);
        PiVector mainIndices = star.getVertexLocInd();
        PiVector elements = star.getElement();
        boolean closed = false;

        ArrayList<Integer> vertexRing = new ArrayList<>();
        int centerIndex = 0;
        for (int i = 0; i < elements.getSize(); i++) {

            // get the element
            int elementIndex = elements.getEntry(i);
            int centerLocalIndex = mainIndices.getEntry(i);
            PiVector element = m_triangulated.getElement(elementIndex);

            // save the target vertex, which is part of the ring
            vertexRing.add(element.getEntry((centerLocalIndex + 1) % element.getSize()));

            // save center vertex index
            if (i == 0) {
                centerIndex = element.getEntry(centerLocalIndex);
            }

            // check if ring is closed
            if (i + 1 == elements.getSize()) {
                int closingVertexIndex = element.getEntry((centerLocalIndex + 2) % element.getSize());
                closed = vertexRing.get(0) == closingVertexIndex;
                // if star is not closed, then add the final vertex as well, as it is still unique
                if (!closed) vertexRing.add(closingVertexIndex);
            }
        }

        // lets return zero when we are on the boundary
        if (!closed) return curvature;

        // initialize next loop
        int size = vertexRing.size();
        PdVector centerVertex = m_triangulated.getVertex(centerIndex);

        // loop over each element in the star
        for (int i = 0; i < size; i++) {

            // get all vertex indices required
            int currentIndex = vertexRing.get(i);
            int otherAIndex = vertexRing.get((i + size + 1) % size);
            int otherBIndex = vertexRing.get((i + size - 1) % size);

            // get the actual vertex locations
            PdVector currentVertex = m_triangulated.getVertex(currentIndex);
            PdVector otherAVertex = m_triangulated.getVertex(otherAIndex);
            PdVector otherBVertex = m_triangulated.getVertex(otherBIndex);

            // calculate the curvature as shown on slides
            PdVector temp = PdVector.subNew(centerVertex, currentVertex);
            double angleA = Math.toRadians(PdVector.angle(otherAVertex, centerVertex, currentVertex));
            double angleB = Math.toRadians(PdVector.angle(otherBVertex, centerVertex, currentVertex));
            temp.multScalar(1.0 / Math.tan(angleA) + 1.0 / Math.tan(angleB));

            if (angleA == 0.0 || angleB == 0.0) {
                PsDebug.message("oh dear, some angles were equal to 0, which would result in infinity, skipping vertice");
                continue;
            }

            // add to curvature
            curvature.add(temp);
        }

        // scale curvature with area and constant, as shown on slide
        curvature.multScalar(3.0 / (2.0 * area));

        // done
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