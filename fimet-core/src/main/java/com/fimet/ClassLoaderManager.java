package com.fimet;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fimet.utils.ClassLoaderBin;
import com.fimet.utils.ClassLoaderException;
import com.fimet.utils.ClassLoaderLib;


/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
@Component
public class ClassLoaderManager extends AbstractManager implements IClassLoaderManager {
	//private static Logger logger = LoggerFactory.getLogger(AbstractManager.class);
	@SuppressWarnings("unused")
	@Autowired private PropertiesManager properties;
	private ClassLoaderBin classLoaderBin;
	private ClassLoaderLib classLoaderLib;
	public ClassLoaderManager() {
	}
	@PostConstruct
	public void start() {
		classLoaderLib = new ClassLoaderLib();
		classLoaderBin = new ClassLoaderBin(classLoaderLib);
	}
	@Override
	public Class<?> loadClass(String className) throws ClassLoaderException{
		if (wasInstalled(className)) {
			try {
				return loadClassBin(className);
			} catch (Exception e) {
				try {
					return Class.forName(className);
				} catch (Exception t) {
					throw new ClassLoaderException(t);
				}
			}
		} else {
			try {
				return loadClassLib(className);
			} catch (Exception e) {
				try {
					return Class.forName(className);
				} catch (Exception t) {
					throw new ClassLoaderException(t);
				}
			}
		}
	}	
	public Class<?> loadClassBin(String className) throws ClassNotFoundException {
		return classLoaderBin.loadClass(className);
	}
	public Class<?> loadClassLib(String className) throws ClassNotFoundException {
		return classLoaderLib.loadClass(className);
	}
	@Override
	public ClassLoader getClassLoaderBin() {
		return classLoaderBin;
	}
	@Override
	public ClassLoader getClassLoaderLib() {
		return classLoaderLib;
	}
	/**
	 * If the class was loaded previously classLoader will reload the class
	 */
	@Override
	public void installClass(String className, byte[] contents) throws ClassLoaderException {
		if (wasInstalled(className)) {
			reloadClassLoader();
		}
		try {
			classLoaderBin.installClass(className, contents);
		} catch (Exception e) {
			throw new ClassLoaderException(e);
		}
	}
	@Override
	public void installClass(String className, byte[] clazz, boolean override) {
		classLoaderBin.installClass(className, clazz, override);
	}
	/**
	 * If you want reload a class you to must do:
	 * IClassLoaderManager.reloadClassLoader().loadClass(className);
	 * this code will affect all classes loaded for the previus ClassLoader
	 */
	@Override
	public void reloadClassLoader() {
		classLoaderBin = new ClassLoaderBin(classLoaderLib);
		//classLoaderLib = new ClassLoaderLib();
	}
	@Override
	public void uninstallClasses() {
		classLoaderBin.uninstallClasses();
	}
	@Override
	public boolean wasInstalled(String className) {
		return classLoaderBin.wasInstalled(className);
	}
	@Override
	public void reload() {
		classLoaderBin.uninstallClasses();
	}
	public <I>I loadAndInstantiate(String className, Class<I> interfaz) throws ClassLoaderException {
		try {
			try {
				Class<?> clazz = Class.forName(className);
				return interfaz.cast(clazz.newInstance());
			} catch (Exception e) {}
			if (classLoaderBin.wasInstalled(className)) {
				Class<?> clazz = classLoaderBin.loadClass(className);
				return interfaz.cast(clazz.newInstance());
			}
			Class<?> clazz = classLoaderLib.loadClass(className);
			return interfaz.cast(clazz.newInstance());
		} catch (Exception e) {
			throw new ClassLoaderException(e);
		}
	}
	public <I>I reloadAndInstantiate(String className, Class<I> interfaz) throws ClassLoaderException {
		try {
			if (classLoaderBin.wasInstalled(className)) {
				reloadClassLoader();
				Class<?> clazz = classLoaderBin.loadClass(className);
				return interfaz.cast(clazz.newInstance());
			}
			try {
				Class<?> clazz = classLoaderLib.loadClass(className);
				return interfaz.cast(clazz.newInstance());
			} catch (Exception e) {}
			Class<?> clazz = Class.forName(className);
			return interfaz.cast(clazz.newInstance());
		} catch (Exception e) {
			throw new ClassLoaderException(e);
		}
	}
	@SuppressWarnings("unchecked")
	public <I,E extends I> Class<E> forName(String className,Class<I> interfaz) throws ClassLoaderException {
		try {
			Class<?> clazz = forName(className);
			return (Class<E>)clazz;
		} catch (Exception e) {
			throw new ClassLoaderException(e);
		}
	}
	public Class<?> forName(String className) throws ClassLoaderException {
		try {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(className);
			} catch (Exception e) {}
			if (clazz==null) {
				if (classLoaderBin.wasInstalled(className)) {
					clazz = classLoaderBin.loadClass(className);
				}
			}
			if (clazz==null) {
				clazz = classLoaderLib.loadClass(className);
			}
			return clazz;
		} catch (Exception e) {
			if (e instanceof ClassLoaderException)
				throw (ClassLoaderException)e;
			throw new ClassLoaderException(e);
		}
	}
	@Override
	public Class<?> reloadClass(String className) throws ClassLoaderException {
		reloadClassLoader();
		return loadClass(className);
	}
	@Override
	public String[] getLibraries() {
		return classLoaderLib.getLibraries();
	}
	@Override
	public void stop() {}
}
