package com.fimet;


//import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
@Component
public final class Manager {
	//private static Logger logger = LoggerFactory.getLogger(Manager.class);
	private static Manager INSTANCE;
	@Autowired private IPropertiesManager properties;
	@Autowired private ApplicationContext context;
	public Manager() {
		INSTANCE = this;
	}
	public static ApplicationContext getContext() {
		return INSTANCE.context;
	}
	public boolean isLoaded(Class<?> clazz) {
		return context.containsBean(clazz.getName());
	}
	public boolean isManaged(Class<?> iManagerClass) {
		return context.containsBean(iManagerClass.getName());
	}
	public <T, U extends T>T get(Class<T> clazz, Class<U> defaultClass) {
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
