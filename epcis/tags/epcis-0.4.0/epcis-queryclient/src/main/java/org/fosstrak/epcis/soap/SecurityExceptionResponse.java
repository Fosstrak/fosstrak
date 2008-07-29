package org.fosstrak.epcis.soap;

import javax.xml.ws.WebFault;

/**
 * This class was generated by Apache CXF (incubator) 2.0.4-incubator Wed Jan 30
 * 15:43:44 CET 2008 Generated source version: 2.0.4-incubator
 */

@WebFault(name = "SecurityException", targetNamespace = "urn:epcglobal:epcis-query:xsd:1")
public class SecurityExceptionResponse extends Exception {
    public static final long serialVersionUID = 20080130154344L;

    private org.fosstrak.epcis.model.SecurityException securityException;

    public SecurityExceptionResponse() {
        super();
    }

    public SecurityExceptionResponse(String message) {
        super(message);
    }

    public SecurityExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityExceptionResponse(String message, org.fosstrak.epcis.model.SecurityException securityException) {
        super(message);
        this.securityException = securityException;
    }

    public SecurityExceptionResponse(String message, org.fosstrak.epcis.model.SecurityException securityException,
            Throwable cause) {
        super(message, cause);
        this.securityException = securityException;
    }

    public org.fosstrak.epcis.model.SecurityException getFaultInfo() {
        return this.securityException;
    }
}
