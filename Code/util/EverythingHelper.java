package util;

import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.vecmath.PdMatrix;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.numeric.PnSparseMatrix;
import util.Util;

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

    public static SimpleVertexStar[] makeVertexStars(PgElementSet geom) {
        SimpleVertexStar[] vertexStars = new SimpleVertexStar[geom.getNumVertices()];
        for (PiVector elem : geom.getElements()) {
            for (int i = 0; i < elem.getSize(); i++) {
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

    public static SimpleTriangle getTriangle(PgElementSet m_geom, int i)
    {
        PiVector elem = m_geom.getElement(i);
        return new SimpleTriangle(m_geom.getVertex(elem.getEntry(0)), m_geom.getVertex(elem.getEntry(1)), m_geom.getVertex(elem.getEntry(2)));
    }

    public static PnSparseMatrix GetGradientMatrix(PgElementSet geom)
    {
        PnSparseMatrix gradientMatrix = new PnSparseMatrix(geom.getNumElements() * 3, geom.getNumVertices(), 3);
        for (int i = 0; i < geom.getNumElements(); i++)
        {
            // Get the triangle and calculate it's 3x3 gradient matrix
            SimpleTriangle triangle = EverythingHelper.getTriangle(geom, i);
            PdMatrix mat = triangle.gradientMatrix();

            // Put the values into the sparse matrix:
            PiVector elem = geom.getElement(i);
            for (int v = 0; v < 3; v ++)
            {
                int col = elem.getEntry(v); // Get the vertex index
                for (int row = 0; row < 3; row++)
                    gradientMatrix.setEntry(i * 3 + row, col, mat.getEntry(row, v));
            }
        }
        return gradientMatrix;
    }

}
