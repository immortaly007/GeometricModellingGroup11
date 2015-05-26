package workshop;

import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BinaryOperator;

/**
 * Created by Immortaly007 on 16-5-2015.
 */

public class EverythingHelper {
    public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> values) {
        return Collections.min(values);
    }

    public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> values) {
        return Collections.max(values);
    }

    public static <T extends Comparable<T>> T clamp(T val, T min, T max)  {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }

    public static <T extends Number> BigDecimal sum(Collection<? extends T> values) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Number v : values) sum = sum.add(new BigDecimal(v.doubleValue()));
        return sum;
    }

    public static <T extends Number> BigDecimal mean(Collection<? extends T> values) {
        return sum(values).divide(new BigDecimal(values.size()), RoundingMode.HALF_UP);
    }

    public static <T extends Number> BigDecimal std(Collection<? extends T> values)
    {
        BigDecimal avg = mean(values);
        BigDecimal std = BigDecimal.ZERO;
        for (Number nv : values) {
            BigDecimal v =  new BigDecimal(nv.doubleValue());
            BigDecimal vMinAvg = v.min(avg);  // vMinAvg = v - avg
            std = std.add(vMinAvg.multiply(vMinAvg)); // std += (v - avg) * (v - avg)
        }
        std = std.divide(new BigDecimal(values.size()), RoundingMode.HALF_UP);
        return new BigDecimal(Math.sqrt(std.doubleValue()));
    }

    public static void filterNaN(Collection<Double> values)
    {
        values.removeIf(a -> a.isNaN());
    }

    public static void filterInfinity(Collection<Double> values)
    {
        values.removeIf(a -> a.isInfinite());
    }

    public static <T extends Number & Comparable<? super T>> String toSummaryString(Collection<? extends T> values)
    {
        return "min=" + EverythingHelper.min(values) + ", max=" + EverythingHelper.max(values) + ", mean=" + EverythingHelper.mean(values).setScale(8, RoundingMode.HALF_UP) + ", std=" + EverythingHelper.std(values).setScale(8, RoundingMode.HALF_UP);
    }

    public static SimpleVertexStar[] makeVertexStars(PgElementSet geom)
    {
        SimpleVertexStar[] vertexStars = new SimpleVertexStar[geom.getVertices().length];
        for (PiVector elem : geom.getElements())
        {
            for (int i = 0; i < elem.getSize(); i++)
            {
                int curVertex = elem.getEntry(i);
                if (vertexStars[curVertex] == null) {
                    vertexStars[curVertex] = new SimpleVertexStar(curVertex, geom);
                }
                int leftNeighborId = i - 1;
                if (leftNeighborId < 0) leftNeighborId = elem.getSize() - 1;
                int rightNeighborId = i + 1;
                if (rightNeighborId >= elem.getSize()) rightNeighborId = 0;

                int leftNeighbor = elem.getEntry(leftNeighborId);
                int rightNeighbor = elem.getEntry(rightNeighborId);
                vertexStars[curVertex].addNeighbor(leftNeighbor);
                vertexStars[curVertex].addNeighbor(rightNeighbor);
            }
        }
        return vertexStars;
    }

    public static PdVector meanCurvature(PgVertexStar star, PgElementSet geom) {
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
            PiVector element = geom.getElement(elementIndex);

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
        PdVector centerVertex = geom.getVertex(centerIndex);

        // loop over each element in the star
        for (int i = 0; i < size; i++) {

            // get all vertex indices required
            int currentIndex = vertexRing.get(i);
            int otherAIndex = vertexRing.get((i + size + 1) % size);
            int otherBIndex = vertexRing.get((i + size - 1) % size);

            // get the actual vertex locations
            PdVector currentVertex = geom.getVertex(currentIndex);
            PdVector otherAVertex = geom.getVertex(otherAIndex);
            PdVector otherBVertex = geom.getVertex(otherBIndex);

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

        // Calculate the area
        double area = 0;
        for (int e : star.getElement().getEntries()) {
            area += triangleElementArea(geom.getElement(e), geom);
        }
        // scale curvature with area and constant, as shown on slide
        curvature.multScalar(3.0 / (2.0 * area));

        // done
        return curvature;
    }

    public static double triangleElementArea(PiVector elem, PgElementSet geom)
    {
        if (elem.getSize() != 3) {
            PsDebug.message("Was geometry triangulated?");
        }
        PdVector v1 = geom.getVertex(elem.getEntry(0));
        PdVector v2 = geom.getVertex(elem.getEntry(1));
        PdVector v3 = geom.getVertex(elem.getEntry(2));
        return triangleArea(v1,v2,v3);
    }

    public static double triangleArea(PdVector v1, PdVector v2, PdVector v3) {
        // source: http://en.wikipedia.org/wiki/Triangle#Computing_the_area_of_a_triangle
        double a = PdVector.dist(v1, v2);
        double b = PdVector.dist(v2, v3);
        double c = PdVector.dist(v1, v3);
        double s = (a + b + c) * 0.5;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
}
