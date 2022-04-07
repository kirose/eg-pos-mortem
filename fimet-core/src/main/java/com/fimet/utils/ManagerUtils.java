package com.fimet.utils;

import java.lang.reflect.AnnotatedType;

import com.fimet.IManager;
import com.fimet.IManagerVisitor;

public final class ManagerUtils {
	private ManagerUtils() {}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static<T extends IManager> void acceptManager(Class<T> iManager, Object manager, IManagerVisitor visitor) {
		if (iManager == IManager.class) {
			return;
		} else if (IManager.class.isAssignableFrom(iManager)) {
			visitor.visitManager(iManager, manager);
		}
		AnnotatedType[] parents = iManager.getAnnotatedInterfaces();
		if (parents!=null && parents.length > 0) {
			for (AnnotatedType parent : parents) {
				if (parent.getType() != IManager.class
					&& parent.getType() instanceof Class
					&& IManager.class.isAssignableFrom((Class)parent.getType())) {
					acceptManager((Class<T>)parent.getType(), iManager, visitor);
				}
			}
		}
	}
}
