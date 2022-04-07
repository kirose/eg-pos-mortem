package com.fimet.commons;

import com.fimet.utils.converter.Converter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ConverterTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ConverterTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ConverterTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testConverter()
    {
    	byte[] txtAscii = "Hola".getBytes();
    	
    	byte[] txtHex = Converter.asciiToHex(txtAscii);
    	assertEquals("486F6C61", new String(txtHex));
    	
    	byte[] txtBinary = Converter.asciiToBinary(txtAscii);
    	assertEquals("01001000011011110110110001100001", new String(txtBinary));
    }
    public void testAsciiToHex() {
		String hex;
		byte[] expected;
		byte[] actual;
		for (int i = -128; i < 128; i++) {
			hex = String.format("%02X", (byte)i);
			expected = hex.getBytes();
			actual = Converter.asciiToHex(new byte[] {(byte)i});
			assertEquals(expected[0], actual[0]);
			assertEquals(expected[1], actual[1]);
		}
	}
}
