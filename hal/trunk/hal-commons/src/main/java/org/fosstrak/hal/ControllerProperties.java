/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Accada (www.accada.org).
 *
 * Accada is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Accada is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Accada; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.accada.reader.hal;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.accada.reader.hal.util.ResourceLocator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The <code>ControllerProperties</code> class encapsulates the methods responsible for
 * the parameter setting. It can be seen as a helper class that frees the user from implementing
 * the related methods and function calls in his concrete <code>AutoIdController</code> imlementation.
 * As a convention the properties file associated with a certain reader has to be named according to its
 * class name.
 * 
 * @version 19.01.2004
 * @author Stefan Schlegel (schlstef@student.ethz.ch)
 * 
 * Modified 06.10.2004 (Roland Schneider schnerol):
 * Instead of using the class name to locate the properties file newly the reader ID defines
 * the properties file name. see new constructor. 
 * 
 */

public class ControllerProperties {

//-------------------------------------------fields------------------------------------------//
	/** the logger */
	static private Log log = LogFactory.getLog(ControllerProperties.class); 
	/** the name of the configuration file */
	private String configFile = null;
   private String defaultConfigFile = null;
	/** the configuration */
	private XMLConfiguration conf = null;
	
//-------------------------------------------constructors--------------------------------------//	
	
	public ControllerProperties(String configFile, String defaultConfigFile){
		this.configFile = configFile;
      this.defaultConfigFile = defaultConfigFile;
		log.debug("PropertiesFile: " + configFile + " and " + defaultConfigFile);
		
	}
	
//---------------------------------------------methods--------------------------------------------//	
	
	/**
	 * Gets the parameter with the specified name from the appropriate properties file.
	 * 
	 * @param param parameter name.
	 * @return the value of the parameter or null if parameter not available.
	 * @throws Exception.
	 */
	public String getParameter(String param) throws Exception {
		String value = null;
		if (conf == null) {
         loadConfig();
		}
		log.debug("Trying to get Parameter " + param);
      value = conf.getString(param, null);
      if (value != null) {
         log.debug("Property found: " + param + " = " + value);
      } else {
         log.debug("Property '" + param + "' not found, returning null.");
      }
      return value;
	}

	
	/**
	 * Sets the specified parameter.
	 * 
	 * @param param The parameter that has to be set.
	 * @param value The new value for the parameter.
	 */
	public void setParameter(String param, String value){
		/* Mit dieser Implemetation wurden alle Kommentare im File überschrieben.
		 * Properties file has changed from java properties to xml!
		 
		Properties propertyFile = new Properties();
		InputStream input = null;
		OutputStream output = null;
		
		try{
			input = new FileInputStream("./props/"+propsFile);
			propertyFile.load(input);
			input.close();
			if (propertyFile.containsKey(param)){
				output = new FileOutputStream("./props/"+propsFile);
				propertyFile.setProperty(param,value);
				propertyFile.store(output,null);
				output.close();
			}
			else {
				String message ="Unkown Parameter: "+ param + ". Value: "+value+ " not set." 
				log.debug(message);
				throw new Exception(message);
			}
		}catch(IOException ex){
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		*/
	}
	
	
	/**
	 * Gets the names of the configurable parameters.
	 * 
	 * @return The parameter names.
	 * 
	 * @throws Exception
	 */
	public String[] getParameterNames() throws Exception{
		String[] names = new String[] {""};

      if (conf == null) {
         loadConfig();
      }
		log.debug("Trying to get Parameters");
		Iterator keyiterator = conf.getKeys();
		ArrayList<String> arraylist = new ArrayList<String>();
		String element = null;
		Object object = null;
		Class stringclass = Class.forName("java.lang.String");
		while (keyiterator.hasNext()) {
		   object = keyiterator.next();
		   if (stringclass.isInstance(object)) {
		      element = (String) object;
		      arraylist.add(element);
		   }
		}
		names = arraylist.toArray(names);

		return names;		
	}
   
   /**
    * Loads the configuration file
    * 
    * @param propFile The name of the configuration file
    * @throws IOException
    */
   private void loadConfig() throws IOException {
      conf = new XMLConfiguration();
      URL url = ResourceLocator.getURL(configFile, defaultConfigFile, this.getClass());
      try {
         conf.load(url);
      } catch (ConfigurationException e) {
         log.error("Could not find properties file: " + configFile);
         throw new IOException("Properties file not found.");     
      }
   }
}

