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
package org.fosstrak.webadapters.epcis.rest;

import org.fosstrak.webadapters.epcis.model.EPCISResource;
import javax.ws.rs.core.UriInfo;

/**
 *
 * Interface for the EPCIS Webadapter business logic
 *
 * @author Mathias Mueller mathias.mueller(at)unifr.ch
 *
 *
 */
public interface IRESTfulEPCISResource {

    /**
     * Returns a representation of the EPCIS Webadapter home resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getRESTfulEPCIS(UriInfo context);

    /**
     * Returns a representation of the EPCIS Webadapter config resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getConfig(UriInfo context);

    /**
     * Returns a representation of the reset EPCIS Webadapter resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getResetRestfulEpcis(UriInfo context);

    /**
     * POST method for reseting the EPCIS Webadapter Database
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource doResetRestfulEpcis(UriInfo context);

    /**
     * Returns a representation of the reload finder resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getReloadFinder(UriInfo context);

    /**
     * POST method for reloading the EPCIS Webadapter Finder
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource doReloadFinder(UriInfo context);

    /**
     * Returns a representation of the config EPCIS URL resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getEpcisUrl(UriInfo context);

    /**
     * PUT method for updating or creating an instance of EPCIS URL
     *
     *
     * @param context
     * @param url
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource setEpcisUrl(UriInfo context, String url);

    /**
     * Returns a representation of the config FEED URL resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getFeedUrl(UriInfo context);

    /**
     * PUT method for updating or creating an instance of FEED URL
     *
     *
     * @param context
     * @param url
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource setFeedUrl(UriInfo context, String url);

    /**
     * Returns a representation of the about resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getAbout(UriInfo context);

    /**
     * Returns a representation of the version resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getAuthors(UriInfo context);

    /**
     * Returns a representation of the author resource according to the requested mime type
     *
     *
     * @param context
     * @param id
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getAuthor(UriInfo context, String id);

    /**
     * Returns a representation of the querynames resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getQueryNames(UriInfo context);

    /**
     * Returns a representation of the business locations resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getVersion(UriInfo context);

    /**
     * Returns a representation of the EPCIS Webadapter version resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getRest(UriInfo context);

    /**
     * Returns a representation of the EPCIS standard version resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getStandard(UriInfo context);

    /**
     * Returns a representation of the EPCIS vendor (implementor) version resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    public EPCISResource getVendor(UriInfo context);
}
