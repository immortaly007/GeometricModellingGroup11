package workshop;

public class NanFilter implements IFilter<Double> {
	public boolean filter(Double value) {
		return Double.isNaN(value);
	}
}