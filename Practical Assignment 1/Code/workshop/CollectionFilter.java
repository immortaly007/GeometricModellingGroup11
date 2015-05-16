package workshop;

import java.util.Iterator;
import java.util.Collection;

public class CollectionFilter {
    public static<T> Collection<T> filter(Collection<T> values, IFilter<T> filter) {
    	Iterator<T> i = values.iterator();
    	while(i.hasNext()) {
    		if (filter.filter(i.next())) i.remove();
    	}
    	return values;
    }
}
