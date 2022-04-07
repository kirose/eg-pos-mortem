package com.fimet.core;

import com.fimet.IStoreManagerExt3;
import com.fimet.core.utils.ManagerVisitor;
import com.fimet.utils.ManagerUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ManagerTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ManagerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ManagerTest.class );
    }
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
		ManagerUtils.acceptManager(IStoreManagerExt3.class, "com.fimet.ExtStoreManager", ManagerVisitor.INSTANCE);
    }
}
