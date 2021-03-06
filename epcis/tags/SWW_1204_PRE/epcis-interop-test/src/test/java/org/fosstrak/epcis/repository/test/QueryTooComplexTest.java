/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org).
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.fosstrak.epcis.repository.test;

import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.fosstrak.epcis.queryclient.QueryControlClient;
import org.fosstrak.epcis.soap.QueryTooComplexExceptionResponse;

/**
 * Tests for QueryTooLargeException (SE49). Note 'maxQueryExecutionTime'
 * property in application.properties must be set to 0 and context must be
 * reloaded.
 * 
 * @author Marco Steybe
 */
public class QueryTooComplexTest extends TestCase {

    private static final String PATH = "src/test/resources/queries/webservice/requests/";

    private static QueryControlClient client = new QueryControlClient();

    /**
     * Tests if QueryTooComplexException is raised.
     * 
     * @throws Exception
     *             Any exception, caught by the JUnit framework.
     */
    public void testSE49() throws Exception {
        System.out.println("SETUP: 'maxQueryExecutionTime' property must be set to 0!");
        final String query = "Test-EPCIS10-SE49-Request-1-Poll.xml";
        InputStream fis = new FileInputStream(PATH + query);
        try {
            client.poll(fis);
            fis.close();
            fail("QueryTooComplexException expected");
        } catch (QueryTooComplexExceptionResponse e) {
            // ok
            fis.close();
        }
    }
}
