package com.fimet.utils.converter;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IConverter {
	public int getId();
	public String getName();
	public byte[] convert(byte[] bytes);
	public byte[] deconvert(byte[] bytes);
}
