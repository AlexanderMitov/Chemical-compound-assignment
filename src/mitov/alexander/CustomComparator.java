package mitov.alexander;
import java.util.Comparator;
import java.util.Map;

public class CustomComparator<T> implements Comparator<T>
{
	private Map<T, Integer> ordering;
	public CustomComparator(Map<T, Integer> ordering)
	{
		if(ordering == null) throw new NullPointerException();
		this.ordering = ordering;
	}
	public int compare(T first, T second)
	{
		if(first == null || second == null) throw new NullPointerException();
		if(!ordering.containsKey(first) || !ordering.containsKey(second)) throw new IllegalArgumentException();
		if(ordering.get(first) < ordering.get(second)) return -1;
		else if(ordering.get(first) > ordering.get(second)) return 1;
		else return 0;
	}
}