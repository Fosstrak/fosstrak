package org.accada.hal.impl.feig;

import org.accada.hal.AsynchronousIdentifyListener;
import org.accada.hal.HardwareException;
import org.accada.hal.Observation;
import org.accada.hal.Trigger;
import org.accada.hal.UnsignedByteArray;
import org.accada.hal.UnsupportedOperationException;

public class FeigUSBController implements FeigController {

	public String[] getAllParameterNames() throws HardwareException,
			UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getReadPointNoiseLevel(String readPointName, boolean normalize) {
		// TODO Auto-generated method stub
		return 0;
	}

   public boolean supportsGetReadPointNoiseLevel() {
      return false;
   }

	public int getReadPointPowerLevel(String readPointName, boolean normalize) {
		// TODO Auto-generated method stub
		return 0;
	}

   public boolean supportsGetReadPointPowerLevel() {
      return false;
   }

	public String getHALName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParameter(String param) throws HardwareException,
			UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

   public boolean supportsParameters() {
      return false;
   }

	public String[] getReadPointNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public Observation[] identify(String[] readPointNames)
			throws HardwareException {
		// TODO Auto-generated method stub
		return null;
	}

   /* (non-Javadoc)
    * @see org.accada.hal.HardwareAbstraction#startAsynchronousIdentify(java.lang.String[], org.accada.hal.AsynchronousIdentifyListener, java.lang.String, java.lang.String)
    */
   public void startAsynchronousIdentify(String[] readPointNames,
         Trigger trigger) throws HardwareException,
         UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
   }

   /* (non-Javadoc)
    * @see org.accada.hal.HardwareAbstraction#stopAsynchronousIdentify(org.accada.hal.AsynchronousIdentifyListener)
    */
   public void stopAsynchronousIdentify()
         throws HardwareException, UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
   }


   /* (non-Javadoc)
    * @see org.accada.hal.HardwareAbstraction#isAsynchronousIdentifyRunning(org.accada.hal.AsynchronousIdentifyListener)
    */
   public boolean isAsynchronousIdentifyRunning() throws HardwareException,
         UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
   }


   /* (non-Javadoc)
    * @see org.accada.hal.HardwareAbstraction#addAsynchronousIdentifyListener(org.accada.hal.AsynchronousIdentifyListener)
    */
   public void addAsynchronousIdentifyListener(AsynchronousIdentifyListener listener)
         throws HardwareException, UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
   }


   /* (non-Javadoc)
    * @see org.accada.hal.HardwareAbstraction#removeAsynchronousIdentifyListener(org.accada.hal.AsynchronousIdentifyListener)
    */
   public void removeAsynchronousIdentifyListener(AsynchronousIdentifyListener listener)
         throws HardwareException, UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
   }


   /* (non-Javadoc)
    * @see org.accada.hal.HardwareAbstraction#supportsAsynchronousIdentify()
    */
   public boolean supportsAsynchronousIdentify() {
      return false;
   }


	public boolean isAsynchronousIdentifyRunning(
			AsynchronousIdentifyListener listener) throws HardwareException,
			UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isReadPointReady(String readPointName) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean supportsIsReadPointReady() {
	   return false;
	}

	public void kill(String readPointName, String id, String[] passwords) throws HardwareException,
			UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	public void writeId(String readPointName, String id, String[] passwords)
			throws HardwareException, UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	public UnsignedByteArray readBytes(String readPointName, String id, int memoryBank,
			int offset, int length, String[] passwords)
			throws HardwareException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void reset() throws HardwareException {
		// TODO Auto-generated method stub

	}

	public boolean supportsReset() {
	   return false;
	}

	public void setParameter(String param, String value)
			throws HardwareException, UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	public void shutDownReadPoint(String readPointName) {
		// TODO Auto-generated method stub

	}

   public boolean supportsShutDownReadPoint() {
      return false;
   }

	public void startUpReadPoint(String readPointName) {
		// TODO Auto-generated method stub

	}

   public boolean supportsStartUpReadPoint() {
      return false;
   }

	public void stopAsynchronousIdentify(AsynchronousIdentifyListener listener)
			throws HardwareException, UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	public boolean supportsKill() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean supportsWriteId() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean supportsReadBytes() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean supportsWriteBytes() {
		// TODO Auto-generated method stub
		return false;
	}

	public void writeBytes(String readPointName, String id, int memoryBank,
			int offset, UnsignedByteArray data, String[] passwords)
			throws HardwareException, UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	public boolean selectChannel(int address, int channel) throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

}