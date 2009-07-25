/*
 *  
 *  Fosstrak LLRP Commander (www.fosstrak.org)
 * 
 *  Copyright (C) 2008 ETH Zurich
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/> 
 *
 */

package org.fosstrak.capturingapp;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.fosstrak.capturingapp.util.CaptureAppWorker;
import org.fosstrak.capturingapp.wsdl.ArrayOfString;
import org.fosstrak.capturingapp.wsdl.CaptureAppPortType;
import org.fosstrak.capturingapp.wsdl.EmptyParms;


/**
 * entry point for the Fosstrak capturing application service. the service has been 
 * implemented as a fully blown WS-service. Therefore if someone likes to 
 * implement the service with an interface providing creation, deletion and 
 * modification of capture applications, feel free to go ahead...<br/>
 * The service currently exports the names of the capture applications. 
 * @author sawielan
 *
 */
public class CaptureAppPortTypeImpl implements CaptureAppPortType {

	// the executor thread pool.
	private static ExecutorService pool = java.util.concurrent.
			Executors.newCachedThreadPool();
	
	// a hash map maintaining the different capture applications.
	private static Map<String, CaptureAppWorker> captureApps = new ConcurrentHashMap<String, CaptureAppWorker> ();
	
	// logger.
    private static final Logger log = Logger.getLogger(CaptureAppPortTypeImpl.class);

    /** the name of the configuration file. */
    public static final String CONFIG_FILE = "/captureapplication.properties";
    
    /** the class name of the default handler. */
    public static final String DEFAULT_HANDLER_CLASS_NAME = 
    	"org.fosstrak.capturingapp.DefaultECReportHandler";
    
    // flag whether the capture app is initialized or not.
    private static boolean initialized = false;
    
    /**
     * create a new capture application port type.
     * @throws Exception upon some exception.
     */
    public CaptureAppPortTypeImpl() throws Exception {
    	initialize();
    }
    
    /**
     * submit a runnable to the thread pool and execute it.
     * @param runnable the runnable to execute.
     * @return a future value with a handle on the executor.
     */
    @SuppressWarnings("unchecked")
	public static Future submitToThreadPool(Runnable runnable) {
    	return pool.submit(runnable);
    	
    }
    
    /**
     * initialize the WS.
     * @throws Exception when the configuration file could not be found or 
     * if there is an error in the configuration.
     */
    private static void initialize() throws Exception {
    	if (initialized) {
    		log.error("already initialized.");
    		return;
    	}
    	
    	log.info("initialize capture applications");
    	
    	Properties props = new Properties();
    	try {
			props.load(
					CaptureAppPortTypeImpl.class.getResourceAsStream(
							CONFIG_FILE));
			
			final int n = Integer.parseInt(props.getProperty("n"));
			// create capture apps for all the configurations...
			for (int i = 0; i<n; i++) {
				
				final int port = Integer.parseInt(
						props.getProperty("cap." + i + ".port", "-1"));
				
				final String name = props.getProperty(
						"cap." + i + ".name", "cap." + i + ".name");
				
				final String epcis = props.getProperty(
						"cap." + i + ".epcis", "tcp://localhost:1234");	
				
				final String changeSet = props.getProperty(
						"cap." + i + ".changeset", null);
				
				String handlerClzzName = props.getProperty(
						"cap." + i + ".handler", null);
				
				log.info(String.format("create new capture app: (%s,%d,%s)",
						name, port, epcis));
				captureApps.put(name, new CaptureAppWorker(
						name,
						new org.fosstrak.capturingapp.CaptureApp(port,
							epcis)));
				
				if (null == handlerClzzName) {
					handlerClzzName = DEFAULT_HANDLER_CLASS_NAME;
				}
				log.info("Using handler class: " + handlerClzzName);
				
				try {
					Class cls = Class.forName(handlerClzzName); 
					Object obj = null;
					if (null == changeSet) {
						obj = cls.newInstance();
					} else {
						log.debug(String.format("using changeSet: %s", changeSet));
						Constructor ctor = cls.getConstructor(String.class);
						obj = ctor.newInstance(changeSet);
					}
					
					if (obj instanceof ECReportsHandler) {
						captureApps.get(name).getCaptureApp().
							registerHandler((ECReportsHandler)obj);
						 
					} else {
						throw new Exception("Invalid type: " + obj.getClass());
					}					
				} catch (Exception e) {
					log.error("Could not create handler: " + e.getMessage());
				}
			}
			
			// start the capture apps
			for (CaptureAppWorker worker : captureApps.values()) {
				log.info(String.format("starting capture app: (%s,%d,%s)",
						worker.getIdentifier(), 
						worker.getCaptureApp().getPort(),
						worker.getCaptureApp().getEpcisRepositoryURL()));
				worker.start();
			}
			
		} catch (IOException e) {
			log.error("could not load config file - aborting.");
			e.printStackTrace();
			initialized = false;
			throw e;
		}
    	
    	initialized = true;
    }

    // --------- \\ WS definition
    
    /* (non-Javadoc)
     * @see org.fosstrak.captureapp.wsdl.CaptureAppPortType#getCaptureAppNames(org.fosstrak.captureapp.wsdl.EmptyParms  parms )*
     */
    public org.fosstrak.capturingapp.wsdl.ArrayOfString getCaptureAppNames(EmptyParms parms)  {
    	ArrayOfString aos = new ArrayOfString();
    	for (CaptureAppWorker worker : captureApps.values()) {
    		aos.getString().add(worker.getIdentifier());
    	}
    	return aos;
    }
    
    // --------- \\ end of WS definition
    
    protected void finalize() throws Throwable {
    	log.info("finalizer called.");
    	for (CaptureAppWorker worker : captureApps.values()) {
    		worker.stop();
    	}
    	super.finalize();
    } 

}
