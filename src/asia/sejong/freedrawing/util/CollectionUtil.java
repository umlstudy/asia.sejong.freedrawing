package asia.sejong.freedrawing.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {

	public static List<Double> getList(double[] items) {
		List<Double> rslt = new ArrayList<Double>(items.length);
		for ( double item : items ) {
			rslt.add(item);
		}
		return rslt;
	}
}
