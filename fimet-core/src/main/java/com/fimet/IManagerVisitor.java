package com.fimet;

public interface IManagerVisitor {
	/**
	 * 
	 * @param iManagerClass
	 * @param manager super top level String class name or Interface
	 */
	<T extends IManager> void visitManager(Class<T> iManagerClass, Object manager);
}
