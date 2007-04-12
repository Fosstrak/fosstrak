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

package org.accada.reader.rprm.core.hal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

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
	/** the name of the properties file */
	private String propsFile = null;
	
//-------------------------------------------constructors--------------------------------------//	
	
	public ControllerProperties(String readerId){
		this.propsFile = readerId + "Properties.properties";
		log.debug("PropertiesFile:  "+propsFile);
		
	}
	
//---------------------------------------------methods--------------------------------------------//	
	
	/**
	 * Gets the parameter with the specified name from the appropriate properties file.
	 * 
	 * @param param parameter name.
	 * @return the value of the parameter.
	 * @throws Exception.
	 */
	public String getParameter(String param) throws Exception{
		String value = null;
		InputStream in;
		Properties props = new Properties();
		log.debug("Trying to get Parameter "+param+" from file "+propsFile);
		
		//possible Errors are propageted and further processed as HardwareExceptions
		in = this.getClass().getResourceAsStream("/props/" + propsFile);
		if (in == null) {
			log.debug("Properties-File not found.");
			throw new IOException("Properties file not found.");		
		}
		//possible Errors are propageted and further processed as HardwareExceptions		
		props.load(in);
		in.close();
		
		if(props.containsKey(param)) {
			value=props.getProperty(param);
			log.debug("Property found: " + param + " = " + value);
			return value; 
		}
		else{
			String message = "Property not found: " +param;
			log.debug(message);
			throw new Exception(message);			
		}
	}

	
	/**
	 * Sets the specified parameter.
	 * 
	 * @param param The parameter that has to be set.
	 * @param value The new value for the parameter.
	 */
	public void setParameter(String param, String value){
		/* Mit dieser Implemetation werden alle Kommentare im File überschrieben.
		 
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
		String[] names = null;
		InputStream in;
		Properties props = new Properties();
		log.debug("Trying to get Parameters");
	
		//possible Errors are propageted and further processed as HardwareExceptions
		in = this.getClass().getResourceAsStream("/props/"+propsFile);
		//possible Errors are propageted and further processed as HardwareExceptions
		props.load(in);
		in.close();
		Enumeration propNames = props.propertyNames();
		names=new String[props.size()];
		int i=0;
		while (propNames.hasMoreElements()){
			names[i] = (String)propNames.nextElement();
			i++;
		}
		return names;		
	}
}

