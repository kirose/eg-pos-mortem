package com.fimet;

import com.fimet.utils.CompilationException;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ICompilerManager extends IManager {
	String compile(String className, String source) throws CompilationException;
	Class<?> compileAndReload(String className, String source) throws CompilationException;
	Class<?> compileAndLoad(String className, String source) throws CompilationException;
}
