
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package org.accada.ale.wsdl.alelr.epcglobal;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.accada.ale.server.readers.LogicalReaderManager;

/**
 * This class was generated by Apache CXF (incubator) 2.0.4-incubator
 * Sun Mar 02 20:44:38 CET 2008
 * Generated source version: 2.0.4-incubator
 * 
 */                     
public class ALELRServicePortTypeImpl implements ALELRServicePortType {

    private static final Logger LOG = Logger.getLogger(ALELRServicePortTypeImpl.class.getName());

    private static boolean isInitialized = false;
    
    private static void initialize() {
    	if (!LogicalReaderManager.isInitialized()) {
    		LOG.info("starting logical reader api");
    		try {
				LogicalReaderManager.initialize();
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("ERROR WHEN INITIALIZING LOGICAL READER API");
			}
			isInitialized = true;
    	}
    }
    
    public ALELRServicePortTypeImpl() {
    	if (!isInitialized) { initialize(); }
    }
    
    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#setReaders(org.accada.ale.wsdl.alelr.epcglobal.SetReaders  parms )*
     */
    public org.accada.ale.wsdl.alelr.epcglobal.SetReadersResult setReaders(SetReaders parms) throws ValidationExceptionResponse , InUseExceptionResponse , ImplementationExceptionResponse , NonCompositeReaderExceptionResponse , SecurityExceptionResponse , NoSuchNameExceptionResponse , ImmutableReaderExceptionResponse , ReaderLoopExceptionResponse    {
    	if (!isInitialized) { initialize(); }
    	try {
			LogicalReaderManager.setReaders(parms.getName(), parms.getReaders().getReader());
		} catch (org.accada.ale.wsdl.ale.epcglobal.NoSuchNameExceptionResponse e) {
			throw new NoSuchNameExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		}
		return null;
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#undefine(org.accada.ale.wsdl.alelr.epcglobal.Undefine  parms )*
     */
    public org.accada.ale.wsdl.alelr.epcglobal.UndefineResult undefine(Undefine parms) throws InUseExceptionResponse , ImplementationExceptionResponse , SecurityExceptionResponse , NoSuchNameExceptionResponse , ImmutableReaderExceptionResponse    {
    	if (!isInitialized) { initialize(); }
    	try {
			LogicalReaderManager.undefine(parms.getName());
		} catch (org.accada.ale.wsdl.ale.epcglobal.NoSuchNameExceptionResponse e) {
			throw new NoSuchNameExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		}
    	return null;
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#setProperties(org.accada.ale.wsdl.alelr.epcglobal.SetProperties  parms )*
     */
    public org.accada.ale.wsdl.alelr.epcglobal.SetPropertiesResult setProperties(SetProperties parms) throws ValidationExceptionResponse , InUseExceptionResponse , ImplementationExceptionResponse , SecurityExceptionResponse , NoSuchNameExceptionResponse , ImmutableReaderExceptionResponse    {
    	if (!isInitialized) { initialize(); }
    	try {
			LogicalReaderManager.setProperties(parms.getName(), parms.getProperties().getProperty());
		} catch (org.accada.ale.wsdl.ale.epcglobal.NoSuchNameExceptionResponse e) {
			throw new NoSuchNameExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		}
		return null;
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#update(org.accada.ale.wsdl.alelr.epcglobal.Update  parms )*
     */
    public org.accada.ale.wsdl.alelr.epcglobal.UpdateResult update(Update parms) throws ValidationExceptionResponse , InUseExceptionResponse , ImplementationExceptionResponse , SecurityExceptionResponse , NoSuchNameExceptionResponse , ImmutableReaderExceptionResponse , ReaderLoopExceptionResponse    { 
    	if (!isInitialized) { initialize(); }
    	try {
			LogicalReaderManager.update(parms.getName(), parms.getSpec());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.NoSuchNameExceptionResponse e) {
			throw new NoSuchNameExceptionResponse(e.getMessage());
		}
		return null;
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#addReaders(org.accada.ale.wsdl.alelr.epcglobal.AddReaders  parms )*
     */
    public org.accada.ale.wsdl.alelr.epcglobal.AddReadersResult addReaders(AddReaders parms) throws ValidationExceptionResponse , InUseExceptionResponse , ImplementationExceptionResponse , NonCompositeReaderExceptionResponse , SecurityExceptionResponse , NoSuchNameExceptionResponse , ImmutableReaderExceptionResponse , ReaderLoopExceptionResponse    { 
    	if (!isInitialized) { initialize(); }
    	try {
			LogicalReaderManager.addReaders(parms.getName(), parms.getReaders().getReader());
		} catch (org.accada.ale.wsdl.ale.epcglobal.NoSuchNameExceptionResponse e) {
			throw new NoSuchNameExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		}
		return null;
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#define(org.accada.ale.wsdl.alelr.epcglobal.Define  parms )*
     */
    public org.accada.ale.wsdl.alelr.epcglobal.DefineResult define(Define parms) throws ValidationExceptionResponse , ImplementationExceptionResponse , DuplicateNameExceptionResponse , SecurityExceptionResponse    { 
    	if (!isInitialized) { initialize(); }
    	try {
			LogicalReaderManager.define(parms.getName(), parms.getSpec());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.DuplicateNameExceptionResponse e) {
			throw new DuplicateNameExceptionResponse(e.getMessage());
		}
		return null;
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#getPropertyValue(org.accada.ale.wsdl.alelr.epcglobal.GetPropertyValue  parms )*
     */
    public java.lang.String getPropertyValue(GetPropertyValue parms) throws ImplementationExceptionResponse , SecurityExceptionResponse , NoSuchNameExceptionResponse    { 
    	if (!isInitialized) { initialize(); }
    	try {
			return LogicalReaderManager.getPropertyValue(parms.getName(), parms.getPropertyName());
		} catch (org.accada.ale.wsdl.ale.epcglobal.NoSuchNameExceptionResponse e) {
			throw new NoSuchNameExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		}
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#removeReaders(org.accada.ale.wsdl.alelr.epcglobal.RemoveReaders  parms )*
     */
    public org.accada.ale.wsdl.alelr.epcglobal.RemoveReadersResult removeReaders(RemoveReaders parms) throws InUseExceptionResponse , ImplementationExceptionResponse , NonCompositeReaderExceptionResponse , SecurityExceptionResponse , NoSuchNameExceptionResponse , ImmutableReaderExceptionResponse    { 
    	if (!isInitialized) { initialize(); }
    	try {
			LogicalReaderManager.removeReaders(parms.getName(), parms.getReaders().getReader());
		} catch (org.accada.ale.wsdl.ale.epcglobal.NoSuchNameExceptionResponse e) {
			throw new NoSuchNameExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		}
		return null;
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#getVendorVersion(org.accada.ale.wsdl.alelr.epcglobal.EmptyParms  parms )*
     */
    public java.lang.String getVendorVersion(EmptyParms parms) throws ImplementationExceptionResponse { 
    	if (!isInitialized) { initialize(); }
    	try {
			return LogicalReaderManager.getVendorVersion();
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			e.printStackTrace();
			throw new ImplementationExceptionResponse(e.getMessage());
		}
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#getLRSpec(org.accada.ale.wsdl.alelr.epcglobal.GetLRSpec  parms )*
     */
    public org.accada.ale.xsd.ale.epcglobal.LRSpec getLRSpec(GetLRSpec parms) throws ImplementationExceptionResponse , SecurityExceptionResponse , NoSuchNameExceptionResponse    {
    	if (!isInitialized) { initialize(); }
		try {
			return LogicalReaderManager.getLRSpec(parms.getName());
		} catch (org.accada.ale.wsdl.ale.epcglobal.NoSuchNameExceptionResponse e) {
			throw new NoSuchNameExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		}
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#getStandardVersion(org.accada.ale.wsdl.alelr.epcglobal.EmptyParms  parms )*
     */
    public java.lang.String getStandardVersion(EmptyParms parms) throws ImplementationExceptionResponse    { 
    	if (!isInitialized) { initialize(); }
    	try {
			return LogicalReaderManager.getStandardVersion();
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			e.printStackTrace();
			throw new ImplementationExceptionResponse(e.getMessage());
		}
    }

    /* (non-Javadoc)
     * @see org.accada.ale.wsdl.alelr.epcglobal.ALELRServicePortType#getLogicalReaderNames(org.accada.ale.wsdl.alelr.epcglobal.EmptyParms  parms )*
     */
    public org.accada.ale.wsdl.alelr.epcglobal.ArrayOfString getLogicalReaderNames(EmptyParms parms) throws ImplementationExceptionResponse , SecurityExceptionResponse    {
    	if (!isInitialized) { initialize(); }
    	ArrayOfString aof = new ArrayOfString();
    	try {
			aof.getString().addAll(LogicalReaderManager.getLogicalReaderNames());
		} catch (org.accada.ale.wsdl.ale.epcglobal.SecurityExceptionResponse e) {
			throw new SecurityExceptionResponse(e.getMessage());
		} catch (org.accada.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse e) {
			throw new ImplementationExceptionResponse(e.getMessage());
		}
        return aof;
    }
    
}
