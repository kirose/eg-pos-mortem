package com.fimet.utils;

import java.util.Arrays;

public final class PanUtils {

	private PanUtils() {}
	public static boolean validarDigitoVerificador(String pan) {

		if (pan == null || pan.length() == 0) {
			throw new NullPointerException();
		}
		StringBuffer sb = new StringBuffer();
		byte[] panBuf = pan.getBytes();
		int multiplier = 1;
		for (int index = panBuf.length - 1; index >= 0; index--) {
			sb.append((panBuf[index] - 48) * multiplier);
			multiplier = multiplier == 1 ? 2 : 1;
		}
		panBuf = sb.toString().getBytes();
		int cd = 0;
		for (int index = 0; index < panBuf.length; index++) {
			cd += panBuf[index] - 48;
		}
		if (cd % 10 == 0) {
			return true;
		}
		return false;

	}
	public static void main(String[] args) {
		System.out.println(calculateLastDigit("6274670000000006"));
		System.out.println(calculateLastDigit("4000000000000002"));
		System.out.println(calculateLastDigit("5583063000653800"));
		System.out.println(calculateLastDigit("5000000000000009"));
		System.out.println(calculateLastDigit("4101773000653800"));
		System.out.println(calculateLastDigit("6274670000000006"));
		System.out.println(calculateLastDigit("9900003000653804"));
		System.out.println(calculateLastDigit("9900003000653801232"));
		StringBuffer sb = new StringBuffer();
		
		System.out.println(sb.append(2).append(15).toString());
	}
	public static char calculateLastDigit(String pan) {
		if (pan == null) {
			throw new RuntimeException("PAN is null");
		}
		if (pan.length() < 15) {
			throw new RuntimeException("Invalid Length of PAN: "+pan);
		}
		StringBuffer sb = new StringBuffer();
		byte[] panBuf = pan.getBytes();
		int multiplier = 2;
		int digit = 0;
		String cdStr = null;
		for (int index = 14; index >= 0; index--){
			sb.append((panBuf[index] - 48) * multiplier);
			multiplier = multiplier == 1 ? 2 : 1;
		}
		panBuf = sb.toString().getBytes();
		int cd = 0;
		for (int index = 0; index < panBuf.length; index++) {
			cd += panBuf[index] - 48;
		}
		cdStr = String.valueOf(cd);
		if (Integer.valueOf(cdStr.substring(cdStr.length() - 1)).intValue() == 0) {
			digit = 0;
		} else {
			digit = 10 - Integer.valueOf(cdStr.substring(cdStr.length() - 1)).intValue();
		}
		return (char) (48+digit);
	}
	private static final String[] jcbBins = { "30", "36", "38" };
	public static boolean isValidLongitudPAN(String emisor, String pan) {

		int longitudpan = pan.length();
		if (longitudpan == 13)
		{
			if ((!emisor.equals("1009")) && 
					(!emisor.equals("1021")) && 
					(!Arrays.asList(jcbBins).contains(pan.substring(0, 2))))
			{
				return false;
			}
			return true;
		}
		if (emisor.equals("1008"))
		{
			if ((longitudpan >= 14) && (longitudpan <= 19)) {
				return true;
			}
			return false;
		}
		if ((longitudpan < 14) || (longitudpan > 16))
		{
			return false;
		}
		if ((emisor.equals("1005")) && (longitudpan != 16))
		{
			return false;
		}
		if ((emisor.equals("1003")) && (longitudpan != 16))
		{
			return false;
		}
		return true;

	}
}
