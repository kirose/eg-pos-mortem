package com.fimet.core.utils;

import com.fimet.IManager;
import com.fimet.IManagerVisitor;

public class ManagerVisitor implements IManagerVisitor {
	public static final ManagerVisitor INSTANCE = new ManagerVisitor();
	@Override
	public <T extends IManager> void visitManager(Class<T> iManagerClass, Object manager) {
		System.out.println(iManagerClass.getSimpleName()+" bind "+manager);
	}

}
