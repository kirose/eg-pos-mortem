package com.fimet.utils;


import java.io.File;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.Paths;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class ClassLoaderBin extends ClassLoader {
	private static Logger logger = LoggerFactory.getLogger(ClassLoaderBin.class);
	public ClassLoaderBin() {
		super(ClassLoaderBin.class.getClassLoader());
	}
	public ClassLoaderBin(ClassLoader parent) {
		super(parent);
	}
	@Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
		File path = new File(Paths.BIN, className.replace('.', File.separatorChar) + ".class");
		if (path.exists()) {
			byte[] contents = FileUtils.readBytesContents(path);
			return defineClass(className, contents, 0, contents.length);
		} else {
			//return PluginUtils.loadClass(className);
			return getParent().loadClass(className);
		}
	}
	public boolean wasInstalled(String className) {
		return new File(Paths.BIN, className.replace('.', File.separatorChar) + ".class").exists();
	}
	public void installClass(String className, byte[] clazz, boolean override) throws ClassLoaderException {
		if (override) {
			File file = new File(Paths.BIN, className.replace('.', File.separatorChar) + ".class");
			FileUtils.createSubdirectories(file);
			FileUtils.writeContents(file, clazz);
			logger.info("Class installed: "+className);
		} else {
			if (wasInstalled(className)) {
				throw new ClassLoaderException("The class "+className+" was installed previusly");
			} else {
				File file = new File(Paths.BIN, className.replace('.', File.separatorChar) + ".class");
				FileUtils.createSubdirectories(file);
				FileUtils.writeContents(file, clazz);
				logger.info("Class installed: "+className);
			}
		}
	}
	public void installClass(String className, byte[] clazz) {
		installClass(className, clazz, true);
	}
	public void uninstallClasses() {
		logger.info("deleting..."+Paths.BIN);
		FileUtils.deleteFiles(Paths.BIN);
		logger.info("deleting..."+Paths.SRC);
		FileUtils.deleteFiles(Paths.SRC);
		logger.info("unistalled classes"+Paths.BIN);
	}
}
