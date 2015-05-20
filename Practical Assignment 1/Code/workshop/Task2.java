package workshop;

import java.util.ArrayList;

import javafx.scene.paint.Color;
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

        PsDebug.message("maxLength = " + maxLength);
        for (int i = 0; i < stars.length; i++) {
            m_geom.setVertexColor(i, new java.awt.Color(
                    (float)(meanCurvatures.get(i) / maxLength),
                    (float)(meanCurvatures.get(i) / maxLength),
                    (float)(meanCurvatures.get(i) / maxLength)));
        }

        EverythingHelper.filterNaN(meanCurvatures);
        EverythingHelper.filterInfinity(meanCurvatures);

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
        PsDebug.message("--------------------------------------------------");
        double area = 0;
        for (int e : star.getElement().getEntries()) {
            area += elementArea( m_triangulated.getElement(e));
        }

        // initialize
        PdVector curvature = new PdVector(0,0,0);
        PiVector mainIndices = star.getVertexLocInd();
        PiVector elements = star.getElement();

        PiVector first = m_triangulated.getElement(elements.getEntry(0));
        int firstCenterIndex = mainIndices.getEntry(0);
        PiVector last = m_triangulated.getElement(elements.getEntry(elements.getSize() - 1));
        int lastCenterIndex = mainIndices.getEntry(mainIndices.getSize() - 1);
        boolean closed = last.getEntry((lastCenterIndex + 2) % first.getSize()) == first.getEntry((firstCenterIndex + 1) % first.getSize());

        PsDebug.message("CLOSED? " + closed);

        // loop over each element in the star
        for (int i = 0; i < elements.getSize(); i++) {
            // get the element and the center vertex local index
            int elementIndex = elements.getEntry(i);
            int centerLocalIndex = mainIndices.getEntry(i);
            PiVector element = m_triangulated.getElement(elementIndex);

            // get all vertex indices required
            int centerIndex = element.getEntry(centerLocalIndex);
            int currentIndex = element.getEntry((centerLocalIndex + 1) % element.getSize());
            int otherAIndex = element.getEntry((centerLocalIndex + 2) % element.getSize());

            // get the other opposite vertex
            int nextI = (i + 1) % elements.getSize();
            int nextElementIndex = elements.getEntry(nextI);
            int nextCenterLocalIndex = mainIndices.getEntry(nextI);
            PiVector nextElement = m_triangulated.getElement(nextElementIndex);
            int otherBIndex = nextElement.getEntry((nextCenterLocalIndex + 1) % nextElement.getSize());

            if (!closed && (i == 0 || i + 1 == elements.getSize())) {

                PsDebug.message("oh dear, skip because we are on the boundary");
                // apparently we are dealing with a non-closed star and an opposing vertex to A does not exist.
                continue;
            }

            // get the actual vertex locations
            PdVector centerVertex = m_triangulated.getVertex(centerIndex);
            PdVector currentVertex = m_triangulated.getVertex(currentIndex);
            PdVector otherAVertex = m_triangulated.getVertex(otherAIndex);
            PdVector otherBVertex = m_triangulated.getVertex(otherBIndex);

            //PsDebug.message("--------------------------------------------------");
            //PsDebug.message(centerVertex.toShortString() + " ~ " + currentVertex.toShortString() + " ~ " + otherAVertex.toShortString() + " ~ " + otherBVertex.toShortString());

            // calculate the curvature as shown on slides
            PdVector temp = PdVector.subNew(centerVertex, currentVertex);
            double angleA = Math.toRadians(PdVector.angle(otherAVertex, centerVertex, currentVertex));
            double angleB = Math.toRadians(PdVector.angle(otherBVertex, centerVertex, currentVertex));
            //PsDebug.message("temp = " + temp.toShortString() );
            PsDebug.message("A = " + angleA + "; B = " + angleB + "; cotA = " + (1.0 / Math.tan(angleA)) + "; cotB = " + (1.0 / Math.tan(angleB)));
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

    private int getOpposingVertex(PiVector elements, int shared1, int shared2, int opposite) {
        // find the element with the 2 sharing vertices, and return the opposing vertex
        // we do this, because we do no assumption on the order of the elements in the star, since the javadoc does not specify this
        //PsDebug.message(shared1 + " ~ " + shared2 + " ~ " + opposite);
        for (int i = 0; i < elements.getSize(); i++) {
            int elementIndex = elements.getEntry(i);
            PiVector element = m_triangulated.getElement(elementIndex);
            //PsDebug.message("  (" + element.getEntry(0) + " ~ " + element.getEntry(1) + " ~ " + element.getEntry(2) + ")");
            if (element.contains(shared1) && element.contains(shared2) && !element.contains(opposite)) {
                //PsDebug.message("aww yes");
                for (int q = 0; q < 3; q++) {
                    int vertexIndex = element.getEntry(q);
                    if (vertexIndex != shared1 && vertexIndex != shared2) {
                        return vertexIndex;
                    }
                }
            }
        }
        return -1;
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