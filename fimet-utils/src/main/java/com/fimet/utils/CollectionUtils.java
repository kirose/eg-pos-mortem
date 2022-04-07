package com.fimet.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public final class CollectionUtils {
	private CollectionUtils() {
	}
	public static <E extends I,I> List<I> cast(List<E> list, Class<I> clazz) {
		if (list == null) {
			return null;
		} else {
			List<I> casted = new ArrayList<I>(list.size());
			for (E e : list) {
				casted.add(e);
			}
			return casted;
		}
	}
}
