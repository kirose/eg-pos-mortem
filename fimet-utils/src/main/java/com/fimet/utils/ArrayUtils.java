package com.fimet.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public final class ArrayUtils {
	private ArrayUtils() {
	}
	public static <E> List<E> copy(List<E> original) {
		if (original == null || original.isEmpty()) {
			return new ArrayList<>();
		}
		List<E> out = new ArrayList<>(original.size());
		for (E e : original) {
			out.add(e);
		}
		return out;
	}
	public static <I,O> List<O> copyAs(List<I> original, Class<O> clazz) {
		if (original == null || original.isEmpty()) {
			return new ArrayList<>();
		}
		List<O> out = new ArrayList<>(original.size());
		for (I e : original) {
			out.add(clazz.cast(e));
		}
		return out;
	}
	public static <T> List<T> copyValuesAsList(Map<?, T> map) {
		if (map!=null) {
			List<T> copy = new ArrayList<>(map.size());
			for (Map.Entry<?, T> e : map.entrySet()) {
				copy.add(e.getValue());
			}
			return copy;
		}
		return null;
	}	
}
