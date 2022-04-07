package com.fimet;


import com.fimet.utils.ClassLoaderException;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IClassLoaderManager extends IManager {
	
	public boolean wasInstalled(String className);
	public Class<?> reloadClass(String className) throws ClassLoaderException;
	public Class<?> loadClass(String className) throws ClassLoaderException;
	public ClassLoader getClassLoaderBin();
	public ClassLoader getClassLoaderLib();
	public void installClass(String className, byte[] clazz, boolean override) throws ClassLoaderException;
	public void installClass(String className, byte[] contents) throws ClassLoaderException;
	public void reloadClassLoader();
	public void uninstallClasses();
	public Class<?> forName(String className) throws ClassLoaderException;
	public <I,E extends I> Class<E> forName(String className,Class<I> interfaz) throws ClassLoaderException;
	public <I>I reloadAndInstantiate(String className, Class<I> interfaz) throws ClassLoaderException;
	public <I>I loadAndInstantiate(String className, Class<I> interfaz) throws ClassLoaderException;
	public String[] getLibraries();
}
