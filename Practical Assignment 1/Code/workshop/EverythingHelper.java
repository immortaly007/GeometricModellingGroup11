package workshop;

import jv.geom.PgElementSet;
import jv.vecmath.PiVector;

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
}
