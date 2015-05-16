package workshop;

import java.util.Collections;
import java.util.List;

/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class EverythingHelper {
    public static double min(List<Double> values)
    {
        return Collections.min(values);
    }

    public static double max(List<Double> values)
    {
        return Collections.max(values);
    }

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
}
