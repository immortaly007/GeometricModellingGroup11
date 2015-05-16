package workshop;

import jv.object.PsDebug;

import java.util.Collections;
import java.util.Collection;
import java.util.List;

/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class EverythingHelper {

    public static double sum(List<Double> values)
    {
        double sum = 0;
        for (double v : values)
            sum += v;
        return sum;
    }

    public static double mean(List<Double> values)
    {
        return sum(values) / (double)values.size();
    }

    public static double std(List<Double> values)
    {
        double avg = mean(values);
        double std = 0;
        for (double v : values)
            std += (v - avg) * (v - avg);
        return Math.sqrt(std);
    }

    public static void filterNaN(List<Double> values)
    {
        values.removeIf(a -> a.isNaN());
    }

    public static String toSummaryString(List<Double> values)
    {
        return "min=" + Collections.min(values) + ", max=" + Collections.max(values) + ", mean=" + EverythingHelper.mean(values) + ", std=" + EverythingHelper.std(values);

    }
}
