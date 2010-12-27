/*
 * Copyright (C) 2010 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org) and
 * was developed as part of the webofthings.com initiative.
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
package org.fosstrak.webadapters.epcis.http;

import org.fosstrak.webadapters.epcis.ws.generated.DuplicateSubscriptionExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.ImplementationExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.InvalidURIExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.NoSuchNameExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.NoSuchSubscriptionExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.QueryParameterExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.QueryTooComplexExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.QueryTooLargeExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.SecurityExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.SubscribeNotPermittedExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.SubscriptionControlsExceptionResponse;
import org.fosstrak.webadapters.epcis.ws.generated.ValidationExceptionResponse;
import java.text.ParseException;
import static java.net.HttpURLConnection.*;

/**
 * Responsible for the EPCIS Exception to HTTP Status Code mapping
 * @author Mathias Mueller mathias.mueller(at)unifr.ch
 *
 */
public class HTTPStatusCodeMapper {

    public static final int OK                              = HTTP_OK;
    public static final int CREATED                         = HTTP_CREATED;
    public static final int NO_CONTENT                      = HTTP_NO_CONTENT;
    public static final int BAD_REQUEST                     = HTTP_BAD_REQUEST;
    public static final int UNAUTHORIZED                    = HTTP_UNAUTHORIZED;
    public static final int NOT_FOUND                       = HTTP_NOT_FOUND;
    public static final int INTERNAL_SERVER_ERROR           = HTTP_INTERNAL_ERROR;
    public static final int NOT_IMPLEMENTED                 = HTTP_NOT_IMPLEMENTED;
    public static final int SERVICE_UNAVAILABLE             = HTTP_UNAVAILABLE;
    public static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    public static final int CONFLICT                        = HTTP_CONFLICT;

    /**
     * Returns the HTTP Error Code for the Exception
     *
     *
     * @param ex
     *
     * @return
     */
    public static int mapExceptionToHttpStatusCode(Exception ex) {

        if (ex instanceof ImplementationExceptionResponse) {
            return NOT_IMPLEMENTED;
        }

        if (ex instanceof NoSuchNameExceptionResponse) {
            return BAD_REQUEST;
        }

        if (ex instanceof QueryParameterExceptionResponse) {
            return BAD_REQUEST;
        }

        if (ex instanceof QueryTooComplexExceptionResponse) {
            return BAD_REQUEST;
        }

        if (ex instanceof QueryTooLargeExceptionResponse) {
            return BAD_REQUEST;
        }

        if (ex instanceof SecurityExceptionResponse) {
            return UNAUTHORIZED;
        }

        if (ex instanceof ValidationExceptionResponse) {
            return CONFLICT;
        }

        if (ex instanceof ParseException) {
            return BAD_REQUEST;
        }

        if (ex instanceof InvalidURIExceptionResponse) {
            return REQUESTED_RANGE_NOT_SATISFIABLE;
        }

        if (ex instanceof SubscriptionControlsExceptionResponse) {
            return BAD_REQUEST;
        }

        if (ex instanceof NoSuchSubscriptionExceptionResponse) {
            return BAD_REQUEST;
        }

        if (ex instanceof DuplicateSubscriptionExceptionResponse) {
            return BAD_REQUEST;
        }

        if (ex instanceof SubscribeNotPermittedExceptionResponse) {
            return BAD_REQUEST;
        }

        return INTERNAL_SERVER_ERROR;
    }
}
