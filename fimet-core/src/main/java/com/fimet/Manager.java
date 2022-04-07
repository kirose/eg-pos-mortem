package com.fimet;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fimet.utils.ArrayUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.ExtensionXml;
import com.fimet.xml.FimetXml;
import com.fimet.xml.ManagerXml;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
@Component
public final class Manager {
	private static Logger logger = LoggerFactory.getLogger(Manager.class);
	private static Manager INSTANCE;
	@Autowired private IPropertiesManager properties;
	@Autowired private IClassLoaderManager loader; 
	@Autowired private ApplicationContext context;
	private Map<String, String> managersString = new LinkedHashMap<>();
	private Map<Class<?>,List<?>> extensions = new HashMap<>();
	private Map<String,List<String>> extensionsString = new LinkedHashMap<>();
	private Map<Class<?>, Object> singletons = new HashMap<>();
	private String source;
	private FimetXml model;
	public Manager() {
		INSTANCE = this;
	}
	public static ApplicationContext getContext() {
		return INSTANCE.context;
	}
	@SuppressWarnings("unchecked")
	private void init() {
		if (!extensionsString.isEmpty()) {
			for (Entry<String, List<String>> e : extensionsString.entrySet()) {
				Class<? extends IExtension> iExtensionClass = (Class<? extends IExtension>)findIExtensionClass(e.getKey());
				extensions.put(iExtensionClass, e.getValue());
			}
		}
		extensionsString = null;
	}
	public FimetXml getModel() {
		return model;
	}
	@PostConstruct
	private void loadConfiguration() {
		try {
			model = XmlUtils.fromPath("fimet.xml", FimetXml.class);
			List<ExtensionXml> ext = model.getExtensions();
			if (ext!=null&&!ext.isEmpty()) {
				for (ExtensionXml e : ext) {
					if (extensionsString.containsKey(e.getName())) {
						extensionsString.get(e.getName()).add(e.getClassName());
					} else {
						List<String> list = new ArrayList<>();
						list.add(e.getClassName());
						extensionsString.put(e.getName(), list);
					}
				}
			}
			if (model.getManagers() != null && !model.getManagers().isEmpty()) {
				List<ManagerXml> managers = model.getManagers();
				for (ManagerXml mgr : managers) {
					this.managersString.put(mgr.getName(), mgr.getClassName());
				}
			}
		} catch (Exception e) {
			logger.error("Loading fimet.xml",e);
			throw new FimetException("Cannot found fimet.xml file",e);
		}
		init();
	}
	private Class<?> findExtensionClass(Class<?> extension, String className) {
		try {
			Class<?> clazz = loader.forName(className);
			if (clazz == null) {
				throw new FimetException("Cannot found class "+className);
			} else if (!IExtension.class.isAssignableFrom(extension)) {
				throw new FimetException("Interface "+extension.getName()+" is not assignable from "+IManager.class.getName());
			} else if (!extension.isAssignableFrom(clazz)) {
				throw new FimetException("Class "+clazz.getName()+" is not assignable from "+extension.getName());
			} else {
				return clazz;
			}
		} catch (Throwable ex) {
			logger.error("Error Load Extension",ex);
			throw new FimetException("Error Load Extension",ex);
		}
	}
	private Class<?> findIExtensionClass(String className) {
		try {
			Class<?> extension = loader.forName(className);			
			if (extension == null) {
				throw new FimetException("Cannot found class "+className);
			} else if (!IExtension.class.isAssignableFrom(extension)) {
				throw new FimetException("Interface "+extension.getName()+" is not assignable from "+IExtension.class.getName());
			} else {
				return extension;
			}
		} catch (Throwable ex) {
			logger.error("Extension Error",ex);
			throw new FimetException("Extension Error",ex);
		}
	}
	public boolean isLoaded(Class<?> clazz) {
		return context.containsBean(clazz.getName());
	}
	public boolean isManaged(Class<?> iManagerClass) {
		return context.containsBean(iManagerClass.getName());
	}
	@SuppressWarnings("unchecked")
	public <T, U extends T>T get(Class<T> clazz, Class<U> defaultClass) {
		if (managersString.containsKey(clazz.getName())) {
			String classImplStr = managersString.get(clazz.getName());
			try {
				Class<?> classImpl = Class.forName(classImplStr);
				return (T)context.getBean(classImpl);
			} catch (ClassNotFoundException e) {
				logger.warn("Class not found",e);
			}
		}
		T bean = (T)context.getBean(clazz);
		if (bean != null)
			return bean;
		if (defaultClass!=null)
			return context.getBean(defaultClass);
		return null;
	}
	public static <T, U extends T>T getManager(Class<T> clazz, Class<U> defaultClass) {
		return INSTANCE.get(clazz, null);
	}
	public static <T>T getManager(Class<T> clazz) {
		return INSTANCE.get(clazz, null);
	}
	public static void reloadAll() {
		String[] names = INSTANCE.context.getBeanDefinitionNames();
		for (String name : names) {
			Object bean = INSTANCE.context.getBean(name);
			if (bean instanceof IManager) {
				((IManager)bean).reload();
			}
		}
	}
	public static String getSource() {
		return INSTANCE.source;
	}
	public static String getProperty(String name) {
		return INSTANCE.properties.getString(name);
	}
	public static String getProperty(String name, String defaultValue) {
		return INSTANCE.properties.getString(name, defaultValue);
	}
	public static Integer getPropertyInteger(String name) {
		return INSTANCE.properties.getInteger(name);
	}
	public static Integer getPropertyInteger(String name, Integer defaultValue) {
		return INSTANCE.properties.getInteger(name, defaultValue);
	}
	public static Long getPropertyLong(String name) {
		return INSTANCE.properties.getLong(name);
	}
	public static Long getPropertyLong(String name, Long defaultValue) {
		return INSTANCE.properties.getLong(name, defaultValue);
	}
	public static Boolean getPropertyBoolean(String name) {
		return INSTANCE.properties.getBoolean(name);
	}
	public static Boolean getPropertyBoolean(String name, Boolean defaultValue) {
		return INSTANCE.properties.getBoolean(name, defaultValue);
	}
	@SuppressWarnings("unchecked")
	public <E extends T, T extends IExtension> List<T> getExtensions(Class<T> iClazz) {
		if (extensions.containsKey(iClazz)) {
			List<?> list = extensions.get(iClazz);
			if (list != null && !list.isEmpty()) {
				if (list.get(0) instanceof String) {
					List<T> instances = new ArrayList<>(list.size());
					for (Object object : list) {
						String className = (String)object;
						Class<?> extensionClass = findExtensionClass(iClazz, className);
						T instance;
						try {
							instance = (T)extensionClass.newInstance();
							instances.add(instance);
						} catch (Exception e) {
							throw new FimetException("Instantiation exception for "+extensionClass.getName(),e);
						}
					}
					extensions.put(iClazz, instances);	
					return ArrayUtils.copy(instances);
				} else {
					List<T> instances = new ArrayList<>(list.size());
					for (Object object : list) {
						instances.add((T)object);
					}
					return instances; 
				}
			}
			return null;
		} else {
			return null;			
		}
	}
	@SuppressWarnings("unchecked")
	public <T> T getSingleton(Class<T> clazz) {
		if (singletons.containsKey(clazz)) {
			return (T)singletons.get(clazz);
		} else {
			try {
				T instance = clazz.newInstance();
				singletons.put(clazz, instance);
				return instance;
			} catch (Throwable t) {
				logger.error("Error singleton instantiation, "+clazz.getName(),t);
				throw new FimetException("Error singleton instantiation, "+clazz.getName(), t);
			}
		}
	}
	public static IPropertiesManager getPropertiesManager() {
		return INSTANCE.properties;
	}
	public static void stop() {
		String[] names = INSTANCE.context.getBeanDefinitionNames();
		for (String name : names) {
			Object bean = INSTANCE.context.getBean(name);
			if (bean instanceof IManager) {
				((IManager)bean).stop();
			}
		}
	}
}
