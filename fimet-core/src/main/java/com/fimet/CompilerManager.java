package com.fimet;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.annotation.PostConstruct;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fimet.utils.CompilationException;
import com.fimet.utils.FileUtils;

@Component
public class CompilerManager extends AbstractManager implements ICompilerManager {
	private static Logger logger = LoggerFactory.getLogger(CompilerManager.class);
	@Autowired private PropertiesManager properties;
	@Autowired private IClassLoaderManager classLoaderManager;
	private String javaHome;
	private String extdirs;
	private String classpath;
	public CompilerManager() {
	}
	@PostConstruct
	@Override
	public void start() {
		classpath = System.getProperty("java.class.path");
		if (classpath.startsWith("fimet-server")&&classpath.endsWith(".jar")) {
			extdirs = properties.getString("fimet.extdirs", Paths.LIB.getAbsolutePath());
		}
		javaHome = properties.getString("fimet.java.home", System.getProperty("java.home"));
		logger.info(
			"Load cononfig"
			+"\n\tclasspath:"+classpath
			+"\n\textdirs:"+extdirs
			+"\n\tJava Home:"
			+"\n\tFrom:"+System.getProperty("java.home")
			+"\n\tTo:"+javaHome
		);
		System.setProperty("java.home", javaHome);
	}
	@Override
	public Class<?> compileAndReload(String className, String source) {
		compileAndInstall(className, source);
		return classLoaderManager.reloadClass(className);
	}
	@Override
	public Class<?> compileAndLoad(String className, String source) {
		compileAndInstall(className, source);
		return classLoaderManager.loadClass(className);
	}
	@Override
	public String compile(String className, String source) {
		return compileAndInstall(className, source);
	}
	private String compileAndInstall(String className, String source) {
		try {
			File sourceFile = new File(Paths.SRC, className.replace('.', File.separatorChar)+".java");
			File classFile = new File(Paths.SRC, className.replace('.', File.separatorChar)+".class");
			sourceFile.getParentFile().mkdirs();
			Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
	

			
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			if (compiler == null) {
				throw new CompilationException("Cannot found compiler, you must set a valid jdk route for java.home property see fimet.xml");
			}
			
	        //List<String> optionList = new ArrayList<String>();
	        //optionList.add("-verbose");
	        //optionList.add("-cp");
	        //optionList.add(System.getProperty("java.class.path"));
	        //optionList.add("-extdirs");
	        //optionList.add(PropertiesManager.LIB.getAbsolutePath());
	        //optionList.add("-d");
	        //optionList.add(destination.getAbsolutePath());
			//DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
	        //StandardJavaFileManager sjfm = compiler.getStandardFileManager(diagnostics, null, null);
			//Iterable<? extends JavaFileObject> fileObjects = sjfm.getJavaFileObjects(sourceFile);
			//JavaCompiler.CompilationTask task = compiler.getTask(null, null, null, optionList,null,fileObjects);
			//if (!task.call()) {
	        //	StringBuilder e = new StringBuilder();
	        //    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
	        //        e.append("Error on line "+diagnostic.getLineNumber()+" in "+diagnostic.getSource()+" with error "+diagnostic.getMessage(new Locale("en"))+"\n");
	        //    }
	        //    logger.error("Compilation error for class "+className+"\n see log for complete errors("+diagnostics.getDiagnostics().size()+"):\n"+e);
	        //    throw new CompilationException("Compilation error for class "+className+"\n see log for complete errors("+diagnostics.getDiagnostics().size()+"):\n"+e);
			//} else {
			//	logger.debug("Class compiled: "+className);
			//	byte[] contents = FileUtils.readBytesContents(classFile);
			//	FileUtils.delete(classFile);
			//	classLoaderManager.installClass(className, contents, true);
			//	return className;
			//}
			
			ByteArrayOutputStream streamErr = new ByteArrayOutputStream();
			//String javaCP = System.getProperty("java.class.path")+File.separatorChar+"SomeJarPath";
			//int status = compiler.run(null, null, streamErr, new String[] {sourceFile.getPath(),"-classpath",javaCP});
			String[] args = extdirs!=null?
					new String[] {sourceFile.getPath(),"-cp",classpath,"-extdirs", extdirs}
					:
					new String[] {sourceFile.getPath(),"-cp",classpath};
			int status = compiler.run(null, null, streamErr, args);
			if (status != 0) {
				String errors = new String(streamErr.toByteArray());
				if (errors != null && errors.length() > 0) {
					logger.error("Compilation error:\n"+errors);
				}
				throw new CompilationException("Compilation error for class "+className+"\n see log for complete errors:\n"+errors.substring(0,Math.min(100, errors.length())));
			} else {
				logger.debug("Class compiled: "+className);
				byte[] contents = FileUtils.readBytesContents(classFile);
				FileUtils.delete(classFile);
				classLoaderManager.installClass(className, contents, true);
				return className;
			}
		} catch (Exception e) {
			if (e instanceof CompilationException) {
				throw ((CompilationException)e);
			}
			throw new CompilationException("Compilation error for class "+className, e);
		}
	}
	@Override
	public void stop() {}
}
