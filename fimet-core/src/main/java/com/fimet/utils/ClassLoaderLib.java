package com.fimet.utils;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.Paths;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class ClassLoaderLib extends ClassLoader {
	private static Logger logger = LoggerFactory.getLogger(ClassLoaderLib.class);
	private URLClassLoader urlClassLoader;
	public ClassLoaderLib() {
		super(ClassLoaderLib.class.getClassLoader());
	}
	public ClassLoaderLib(ClassLoader parent) {
		super(parent);
	}
	@PostConstruct
	private URLClassLoader newURLClassLoader() {
		URL[] urls = getUrlLibraries();
		if (urls != null && urls.length > 0) {
			urlClassLoader = new URLClassLoader(
				urls,
				ClassLoaderLib.class.getClassLoader()//Permite cargar clases que solo el padre tiene acceso
			);
			return urlClassLoader;
		} else {
			return null;
		}
	} 
	private URL[] getUrlLibraries() {
		if (Paths.LIB.exists() && Paths.LIB.isDirectory()) {
			Queue<File> queue = new ArrayDeque<File>();
			queue.add(Paths.LIB);
			File node = null;
			File[] jars;
			List<URL> urls = new ArrayList<URL>();
			while (!queue.isEmpty()) {
				node = queue.poll();
				if (node.isDirectory()) {
					jars = node.listFiles();
					if (jars != null && jars.length > 0) {
						for (File file : jars) {
							if (file.isFile() && file.getName().toLowerCase().endsWith("jar")) {
								try {
									urls.add(file.toURI().toURL());
								} catch (MalformedURLException e) {
									logger.warn("Malformed URL for "+file.getAbsolutePath(),e);
								}
							}
						}
					}
				} else if (node.getName().toLowerCase().endsWith(".jar")) {
					try {
						urls.add(node.toURI().toURL());
					} catch (MalformedURLException e) {
						logger.warn("Malformed URL for "+node.getAbsolutePath(),e);
					}
				}
			}
			if (urls.isEmpty()) {
				return null;
			} else {
				return urls.toArray(new URL[urls.size()]);
			}
		}
		return null;
	}
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		if (urlClassLoader != null) {
			return urlClassLoader.loadClass(className);
		} else {
			return Class.forName(className);
		}
	}
	public String[] getLibraries() {
		if (Paths.LIB.exists() && Paths.LIB.isDirectory()) {
			Queue<File> queue = new ArrayDeque<File>();
			queue.add(Paths.LIB);
			File dir = null;
			File[] jars;
			List<String> libraries = new LinkedList<String>();
			while (!queue.isEmpty()) {
				dir = queue.poll();
				jars = dir.listFiles();
				if (jars != null && jars.length > 0) {
					for (File file : jars) {
						if (file.isDirectory()) {
							queue.add(file);
						} else if (file.getName().toLowerCase().endsWith(".jar")) {
							String relative = FileUtils.makeRelativeTo(file, Paths.LIB);
							libraries.add(relative.substring(0,relative.length()-4));
						}
					}
				}
			}
			if (libraries.isEmpty()) {
				return null;
			} else {
				return libraries.toArray(new String[libraries.size()]);
			}
		}
		return null;
	}
}
