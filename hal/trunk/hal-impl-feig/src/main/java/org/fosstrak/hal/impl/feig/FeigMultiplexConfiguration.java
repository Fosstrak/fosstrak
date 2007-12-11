package org.accada.hal.impl.feig;

import java.net.URL;
import java.util.HashMap;

import org.accada.hal.HardwareException;
import org.accada.hal.util.ResourceLocator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class FeigMultiplexConfiguration {

   /** the logger */
   static private Log log = LogFactory.getLog(FeigMultiplexConfiguration.class);

   /**
	 *
	 */
	//private ControllerProperties props = null;
	private XMLConfiguration props = null;

	/**
	 *
	 */
	private FeigController controller;

	/**
	 *
	 */
	private String configurationName;
   private String defaultConfigurationName;

	/**
	 * Number of read points
	 */
	private int readPointNumber = 1;

	/**
	 * Logical read points
	 */
	private HashMap<String, int[]> readPoints;

	/**
	 *
	 * @param controller
	 * @param configurationName
	 */
	public FeigMultiplexConfiguration(FeigController controller,
			String configurationName, String defaultConfigurationName) {
		this.controller = controller;
		this.configurationName = configurationName;
      this.defaultConfigurationName = defaultConfigurationName;
      log.debug("Configuration file: " + configurationName);
	}

	/**
	 *
	 * @throws HardwareException
	 */
	public void initialize() throws HardwareException {

      log.debug("Initializing FeigMultiplexConfig.");
	   props = new XMLConfiguration();
	   props.setListDelimiter(',');
	   URL fileurl = ResourceLocator.getURL(configurationName, defaultConfigurationName,
            this.getClass());
      try {
         props.load(fileurl);
         readPoints = new HashMap<String, int[]>();
	      for (int i = 0; i <= props.getMaxIndex("readpoint"); i++) {
	         // key to current read point
	         String key = "readpoint(" + i + ")";
	         // read point name
	         String readpointName = props.getString(key + ".name");
	         log.debug("Property found: " + key + ".name = " + readpointName);
	         // read point mapping
            String[] readpointMap = null;
            try {
               readpointMap = props.getStringArray(key + ".map");
            } catch (Exception e) {}
            if ((readpointMap == null) || (readpointMap.length == 0)) {
               readpointMap = new String[] { "0", "0", "0" };
            }
	         if (readpointMap.length != 3) {
	            log.error("Error in multiplexer property "
                     + "file: Readpoint map vector length is not 3");
	            throw new HardwareException("Error in multiplexer property "
	                  + "file: Readpoint map vector length is not 3");
	         }
	         log.debug("Property found: " + key + ".map = " + readpointMap[0]
	            + "," + readpointMap[1] + "," + readpointMap[2]);
	         int[] channels = new int[3];
            for (int j = 0; j < 3; j++) {
               channels[j] = Integer.parseInt(readpointMap[j]);
               if (channels[j] < 0 || channels[j] > 8) {
                  log.error("Error in multiplexer property "
                        + "file: Output channel is out of range (0-8)");
                  throw new HardwareException("Error in multiplexer property "
                        + "file: Output channel is out of range (0-8)");
               }
            }
            readPoints.put(readpointName, channels);
            readPointNumber = i + 1;
	      }
	   } catch (ConfigurationException e) {
         log.error("Properties file not found.");
         throw new HardwareException("Properties file not found.");
      }

	}

	/**
	 *
	 * @param readpointName
	 * @return
	 * @throws HardwareException
	 */
	protected boolean selectReadPoint(String readpointName) {

      // no multiplex configuration if only one read point configured
	   if (readPointNumber == 1) {
         return true;
      }

		int[] channels = readPoints.get(readpointName);
		if (channels == null) {
			return false;
		}
		for (int i=0; i< channels.length; i++) {
			try {
            if (channels[i] != 0) {
               controller.selectChannel(i + 1, channels[i]);
            }
			} catch (HardwareException e) {
				return false;
			}
		}

		return true;
	}

	/**
	 *
	 * @return
	 */
	protected String[] getReadPoints() {
	   return readPoints.keySet().toArray(new String[0]);
		//return (String[]) readPoints.keySet().toArray();
	}
}