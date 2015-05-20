package workshop;

import jv.geom.PgElementSet;
import jv.vecmath.PiVector;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
}
