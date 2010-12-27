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

import org.fosstrak.webadapters.epcis.config.Config;
import javax.servlet.ServletContext;
import org.fosstrak.webadapters.epcis.logic.CaptureBusinessLogic;
import org.fosstrak.webadapters.epcis.logic.QueryBusinessLogic;
import org.fosstrak.webadapters.epcis.logic.RESTfulEPCISBusinessLogic;
import org.fosstrak.webadapters.epcis.logic.SubscriptionBusinessLogic;
import org.fosstrak.webadapters.epcis.model.EPCISResource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import static org.fosstrak.webadapters.epcis.config.URIConstants.*;
import static org.fosstrak.webadapters.epcis.config.QueryParamConstants.*;
import static javax.ws.rs.core.MediaType.*;

/**
 * EPCIS Webadapter
 *
 * @author Mathias Mueller mathias.mueller(at)unifr.ch, <a href="http://www.guinard.org">Dominique Guinard</a>
 *
 */
@Path(EPCIS_ROOT)
@Produces({Config.TEXT_HTML_WEBKIT_SAFE, APPLICATION_JSON, APPLICATION_XHTML_XML, TEXT_XML, MediaType.APPLICATION_XML, Config.APPLICATION_JSONP, Config.APPLICATION_JAVASCRIPT})
public class RESTfulEPCIS implements ICaptureResource, IQueryResource, IRESTfulEPCISResource, ISubscriptionResource {

    @Context
    ServletContext servletContext;

    /**
     * Returns a representation of the EPCIS Webadapter home resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @GET
    public EPCISResource getRESTfulEPCIS(@Context UriInfo context) {
        System.setProperty("sqlite.system.home", servletContext.getRealPath("/"));
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();
        return logic.getRESTfulEPCIS(context);
    }

    /**
     * Returns a representation of the config resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(CONFIG)
    @GET
    public EPCISResource getConfig(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getConfig(context);
    }

    /**
     * Returns a representation of the reset EPCIS Webadapter resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    @Path(CONFIG_RESET_RESTFULEPCIS)
    @GET
    public EPCISResource getResetRestfulEpcis(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getResetRestfulEpcis(context);
    }

    /**
     * POST method for reseting the EPCIS Webadapter Database
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    @Path(CONFIG_RESET_RESTFULEPCIS)
    @POST
    public EPCISResource doResetRestfulEpcis(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.doResetRestfulEpcis(context);
    }

    /**
     * Returns a representation of the reload finder resource according to the requested mime type
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    @Path(CONFIG_RELOAD_FINDER)
    @GET
    public EPCISResource getReloadFinder(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getReloadFinder(context);
    }

    /**
     * POST method for reloading the EPCIS Webadapter Finder
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    @Path(CONFIG_RELOAD_FINDER)
    @POST
    public EPCISResource doReloadFinder(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.doReloadFinder(context);
    }

    /**
     * Returns a representation of the config EPCIS URL resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(CONFIG_EPCIS)
    @GET
    public EPCISResource getEpcisUrl(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getEpcisUrl(context);
    }

    /**
     * PUT method for updating or creating an instance of EPCIS URL
     *
     * @param content representation for the resource
     *
     * @param context
     * @param url
     * @return an HTTP EPCISResource with content of the updated or created resource.
     */
    @Path(CONFIG_EPCIS)
    @PUT
//  @Consumes(TEXT_PLAIN)
    public EPCISResource setEpcisUrl(@Context UriInfo context, String url) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.setEpcisUrl(context, url);
    }

    /**
     * Returns a representation of the config FEED URL resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResourced
     */
    @Path(CONFIG_FEED)
    @GET
    public EPCISResource getFeedUrl(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getFeedUrl(context);
    }

    /**
     * PUT method for updating or creating an instance of FEED URL
     *
     * @param content representation for the resource
     *
     * @param context
     * @param url
     * @return an HTTP EPCISResource with content of the updated or created resource.
     */
    @Path(CONFIG_FEED)
    @PUT
//  @Consumes(TEXT_PLAIN)
    public EPCISResource setFeedUrl(@Context UriInfo context, String url) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.setFeedUrl(context, url);
    }

    /**
     * Returns a representation of the about resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(ABOUT)
    @GET
    public EPCISResource getAbout(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();
        return logic.getAbout(context);
    }

    /**
     * Returns a representation of the version resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(ABOUT_VERSION)
    @GET
    public EPCISResource getVersion(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getVersion(context);
    }

    /**
     * Returns a representation of the EPCIS Webadapter version resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(VERSION_REST)
    @GET
    public EPCISResource getRest(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getRest(context);
    }

    /**
     * Returns a representation of the EPCIS standard version resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(VERSION_STANDARD)
    @GET
    public EPCISResource getStandard(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getStandard(context);
    }

    /**
     * Returns a representation of the EPCIS vendor (implementor) version resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(VERSION_VENDOR)
    @GET
    public EPCISResource getVendor(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getVendor(context);
    }

    /**
     * Returns a representation of the authors resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(ABOUT_AUTHORS)
    @GET
    public EPCISResource getAuthors(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getAuthors(context);
    }

    /**
     * Returns a representation of the author resource according to the requested mime type
     *
     * @param context
     * @param id
     * @return an instance of EPCISResource
     */
    @Path(ABOUT_AUTHOR)
    @GET
    public EPCISResource getAuthor(@Context UriInfo context, @PathParam(ID) String id) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getAuthor(context, id);
    }

    /**
     * Returns a representation of the querynames resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(ABOUT_QUERYNAMES)
    @GET
    public EPCISResource getQueryNames(@Context UriInfo context) {
        RESTfulEPCISBusinessLogic logic = new RESTfulEPCISBusinessLogic();

        return logic.getQueryNames(context);
    }

    /**
     * Returns a representation of the selected read points resource returning all available read points according to the requested mime type
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    @Path(FINDER_ALL_READ_POINTS)
    @GET
    public EPCISResource getAllReadPoints(@Context UriInfo context) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getAllReadPoints(context);
    }

    /**
     * Returns a representation of the business locations resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(FINDER_BUSINESS_LOCATIONS)
    @GET
    public EPCISResource getBusinessLocations(@Context UriInfo context) {
        QueryBusinessLogic logic = new QueryBusinessLogic();
        
        return logic.getBusinessLocations(context);
    }

    /**
     * Returns a representation of the selected business location resource according to the requested mime type
     *
     *
     * @param context
     * @param businessLocation
     *
     * @return an instance of EPCISResource
     */
    @Path(FINDER_BUSINESS_LOCATION)
    @GET
    public EPCISResource getSelectedBusinessLocation(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getSelectedBusinessLocation(context, businessLocation);
    }

    /**
     * Returns a representation of the read points resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @return an instance of EPCISResource
     */
    @Path(FINDER_READ_POINTS)
    @GET
    public EPCISResource getReadPoints(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getReadPoints(context, businessLocation);
    }

    /**
     * Returns a representation of the selected read point resource according to the requested mime type
     *
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     *
     * @return an instance of EPCISResource
     */
    @Path(FINDER_READ_POINT)
    @GET
    public EPCISResource getSelectedReadPoint(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getSelectedReadPoint(context, businessLocation, readPoint);
    }

    /**
     * Returns a representation of the event times resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @return an instance of EPCISResource
     */
    @Path(FINDER_EVENT_TIMES)
    @GET
    public EPCISResource getEventTimes(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getEventTimes(context, businessLocation, readPoint);
    }

    /**
     * Returns a representation of the selected event time resource according to the requested mime type
     *
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     *
     * @return an instance of EPCISResource
     */
    @Path(FINDER_EVENT_TIME)
    @GET
    public EPCISResource getSelectedEventTime(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getSelectedEventTime(context, businessLocation, readPoint, eventTime);
    }

    /**
     * Returns a representation of the event resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(FINDER_EVENT)
    @GET
    public EPCISResource getEvent(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("0") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getEvent(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's record time resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_RECORD_TIME)
    @GET
    public EPCISResource getRecordTime(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getRecordTime(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's time zone offset resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_TIME_ZONE_OFFSET)
    @GET
    public EPCISResource getTimeZoneOffset(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getTimeZoneOffset(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's business step resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_BUSINESS_STEP)
    @GET
    public EPCISResource getBusinessStep(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getBusinessStep(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's action resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_ACTION)
    @GET
    public EPCISResource getAction(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getAction(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's event time resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_EVENT_TIME)
    @GET
    public EPCISResource getEventTime(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getEventTime(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's read point resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_READ_POINT)
    @GET
    public EPCISResource getReadPoint(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getReadPoint(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's business location resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_BUSINESS_LOCATION)
    @GET
    public EPCISResource getBusinessLocation(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getBusinessLocation(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's disposition resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_DISPOSITION)
    @GET
    public EPCISResource getDisposition(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getDisposition(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's event type resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_EVENT_TYPE)
    @GET
    public EPCISResource getEventType(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getEventType(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's epc's resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_EPCS)
    @GET
    public EPCISResource getEpcs(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getEpcs(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of a event epc resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @param epc
     * @return an instance of EPCISResource
     */
    @Path(EVENT_EPC)
    @GET
    public EPCISResource getEpc(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index, @PathParam(EPC) String epc) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getEpc(context, businessLocation, readPoint, eventTime, index, epc);
    }

    /**
     * Returns a representation of the event's business transaction's resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_BUSINESS_TRANSACTIONS)
    @GET
    public EPCISResource getBusinessTransactions(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getBusinessTransactions(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of a event business transaction resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @param businessTransaction
     * @return an instance of EPCISResource
     */
    @Path(EVENT_BUSINESS_TRANSACTION)
    @GET
    public EPCISResource getBusinessTransaction(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index, @PathParam(BUSINESS_TRANSACTION) String businessTransaction) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getBusinessTransaction(context, businessLocation, readPoint, eventTime, index, businessTransaction);
    }

    /**
     * Returns a representation of the event's parent id resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_PARENT_ID)
    @GET
    public EPCISResource getParentID(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getParentID(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's epc class resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_EPC_CLASS)
    @GET
    public EPCISResource getEPCClass(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getEPCClass(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event's quantity resource according to the requested mime type
     *
     * @param context
     * @param businessLocation
     * @param readPoint
     * @param eventTime
     * @param index
     * @return an instance of EPCISResource
     */
    @Path(EVENT_QUANTITY)
    @GET
    public EPCISResource getQuantity(@Context UriInfo context, @PathParam(BUSINESS_LOCATION) String businessLocation, @PathParam(READ_POINT) String readPoint, @PathParam(EVENT_TIME) String eventTime, @QueryParam(INDEX_QP)
            @DefaultValue("1") String index) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getQuantity(context, businessLocation, readPoint, eventTime, index);
    }

    /**
     * Returns a representation of the event query creator resource according to the requested mime type
     *
     * @param context
     * @param eventTime
     * @param recordTime
     * @param eventType
     * @param action
     * @param bizStep
     * @param disposition
     * @param readPoint
     * @param readPointWD
     * @param bizLocation
     * @param bizLocationWD
     * @param bizTransaction
     * @param epc
     * @param parentID
     * @param anyEPC
     * @param epcClass
     * @param quantity
     * @param fieldname
     * @param orderBy
     * @param orderDirection
     * @param eventCountLimit
     * @param maxEventCount
     * @return an instance of EPCISResource
     */
    @Path(EVENTQUERY)
    @GET
    public EPCISResource getEventQuery(@Context UriInfo context, @QueryParam(EVENT_TIME_REST) String eventTime, @QueryParam(RECORD_TIME_REST) String recordTime, @QueryParam(EVENT_TYPE_REST) String eventType, @QueryParam(ACTION_REST) String action, @QueryParam(BUSINESS_STEP_REST) String bizStep, @QueryParam(DISPOSITION_REST) String disposition, @QueryParam(READ_POINT_REST) String readPoint, @QueryParam(READ_POINT_DESCENDANT_REST) String readPointWD, @QueryParam(BUSINESS_LOCATION_REST) String bizLocation, @QueryParam(BUSINESS_LOCATION_DESCENDANT_REST) String bizLocationWD, @QueryParam(BUSINESS_TRANSACTION_TYPE_REST) String bizTransaction, @QueryParam(EPC_REST) String epc, @QueryParam(PARENT_ID_REST) String parentID, @QueryParam(ANY_EPC_REST) String anyEPC, @QueryParam(EPC_CLASS_REST) String epcClass, @QueryParam(QUANTITY_REST) String quantity, @QueryParam(FIELDNAME_REST) String fieldname, @QueryParam(ORDER_BY_REST) String orderBy, @QueryParam(ORDER_DIRECTION_REST) String orderDirection, @QueryParam(EVENT_COUNT_LIMIT_REST) String eventCountLimit, @QueryParam(MAX_EVENT_COUNT_REST) String maxEventCount) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        return logic.getEventQuery(context, eventTime, recordTime, eventType, action, bizStep, disposition, readPoint, readPointWD, bizLocation, bizLocationWD, bizTransaction, epc, parentID, anyEPC, epcClass, quantity, fieldname, orderBy, orderDirection, eventCountLimit, maxEventCount);
    }
    /**
     * Returns a representation of the query results resource according to the requested mime type
     *
     * @param context
     * @param eventTime
     * @param recordTime
     * @param eventType
     * @param action
     * @param bizStep
     * @param disposition
     * @param readPoint
     * @param readPointWD
     * @param bizLocation
     * @param bizLocationWD
     * @param bizTransaction
     * @param epc
     * @param parentID
     * @param anyEPC
     * @param epcClass
     * @param quantity
     * @param fieldname
     * @param orderBy
     * @param orderDirection
     * @param eventCountLimit
     * @param maxEventCount
     * @return an instance of EPCISResource
     */
    @Context
    HttpServletRequest myContext;

    @Path(EVENTQUERY_RESULTS)
    @GET
    public EPCISResource getResults(@Context UriInfo context, @QueryParam(EVENT_TIME_REST) String eventTime, @QueryParam(RECORD_TIME_REST) String recordTime, @QueryParam(EVENT_TYPE_REST) String eventType, @QueryParam(ACTION_REST) String action, @QueryParam(BUSINESS_STEP_REST) String bizStep, @QueryParam(DISPOSITION_REST) String disposition, @QueryParam(READ_POINT_REST) String readPoint, @QueryParam(READ_POINT_DESCENDANT_REST) String readPointWD, @QueryParam(BUSINESS_LOCATION_REST) String bizLocation, @QueryParam(BUSINESS_LOCATION_DESCENDANT_REST) String bizLocationWD, @QueryParam(BUSINESS_TRANSACTION_TYPE_REST) String bizTransaction, @QueryParam(EPC_REST) String epc, @QueryParam(PARENT_ID_REST) String parentID, @QueryParam(ANY_EPC_REST) String anyEPC, @QueryParam(EPC_CLASS_REST) String epcClass, @QueryParam(QUANTITY_REST) String quantity, @QueryParam(FIELDNAME_REST) String fieldname, @QueryParam(ORDER_BY_REST) String orderBy, @QueryParam(ORDER_DIRECTION_REST) String orderDirection, @QueryParam(EVENT_COUNT_LIMIT_REST) String eventCountLimit, @QueryParam(MAX_EVENT_COUNT_REST) String maxEventCount) {
        QueryBusinessLogic logic = new QueryBusinessLogic();

        if (myContext != null) {
            if (myContext.getHeader("Accept").toLowerCase().contains(MediaType.TEXT_XML)) {
                return logic.getResultsXmlOnly(context, eventTime, recordTime, eventType, action, bizStep, disposition, readPoint, readPointWD, bizLocation, bizLocationWD, bizTransaction, epc, parentID, anyEPC, epcClass, quantity, fieldname, orderBy, orderDirection, eventCountLimit, maxEventCount);
            }
        }
        return logic.getResults(context, eventTime, recordTime, eventType, action, bizStep, disposition, readPoint, readPointWD, bizLocation, bizLocationWD, bizTransaction, epc, parentID, anyEPC, epcClass, quantity, fieldname, orderBy, orderDirection, eventCountLimit, maxEventCount);
    }

    /**
     * POST method for adding a subscription
     * EPCISResource for subscribing to a FEED
     *
     * @param context
     * @param eventTime
     * @param recordTime
     * @param eventType
     * @param action
     * @param bizStep
     * @param disposition
     * @param readPoint
     * @param readPointWD
     * @param bizLocation
     * @param bizLocationWD
     * @param bizTransaction
     * @param epc
     * @param parentID
     * @param anyEPC
     * @param epcClass
     * @param quantity
     * @param fieldname
     * @param orderBy
     * @param orderDirection
     * @param eventCountLimit
     * @param maxEventCount
     * @return an HTTP EPCISResource with link to the existing or created feed.
     */
    @Path(EVENTQUERY_SUBSCRIPTION)
    @PUT
    public EPCISResource addSubscription(@Context UriInfo context, @QueryParam(EVENT_TIME_REST) String eventTime, @QueryParam(RECORD_TIME_REST) String recordTime, @QueryParam(EVENT_TYPE_REST) String eventType, @QueryParam(ACTION_REST) String action, @QueryParam(BUSINESS_STEP_REST) String bizStep, @QueryParam(DISPOSITION_REST) String disposition, @QueryParam(READ_POINT_REST) String readPoint, @QueryParam(READ_POINT_DESCENDANT_REST) String readPointWD, @QueryParam(BUSINESS_LOCATION_REST) String bizLocation, @QueryParam(BUSINESS_LOCATION_DESCENDANT_REST) String bizLocationWD, @QueryParam(BUSINESS_TRANSACTION_TYPE_REST) String bizTransaction, @QueryParam(EPC_REST) String epc, @QueryParam(PARENT_ID_REST) String parentID, @QueryParam(ANY_EPC_REST) String anyEPC, @QueryParam(EPC_CLASS_REST) String epcClass, @QueryParam(QUANTITY_REST) String quantity, @QueryParam(FIELDNAME_REST) String fieldname, @QueryParam(ORDER_BY_REST) String orderBy, @QueryParam(ORDER_DIRECTION_REST) String orderDirection, @QueryParam(EVENT_COUNT_LIMIT_REST) String eventCountLimit, @QueryParam(MAX_EVENT_COUNT_REST) String maxEventCount) {
        SubscriptionBusinessLogic logic = new SubscriptionBusinessLogic();

        return logic.addSubscription(context, eventTime, recordTime, eventType, action, bizStep, disposition, readPoint, readPointWD, bizLocation, bizLocationWD, bizTransaction, epc, parentID, anyEPC, epcClass, quantity, fieldname, orderBy, orderDirection, eventCountLimit, maxEventCount);
    }

    /**
     * Returns a representation of the existing subscribtions list resource according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(SUBSCRIPTIONS)
    @GET
    public EPCISResource getSubscriptions(@Context UriInfo context) {
        SubscriptionBusinessLogic logic = new SubscriptionBusinessLogic();

        return logic.getSubscriptions(context);
    }

    /**
     * Returns a representation a subscription resource (the link to a feed) according to the requested mime type
     *
     * @param context
     * @param id
     * @return an instance of EPCISResource
     */
    @Path(SUBSCRIPTIONS_ID)
    @GET
    public EPCISResource getSubscription(@Context UriInfo context, @PathParam(ID) String id) {
        SubscriptionBusinessLogic logic = new SubscriptionBusinessLogic();

        return logic.getSubscription(context, id);
    }

    /**
     * POST method for adding an entry to the corrsponding feed
     * Returns a representation of the according subscription resource (the link to a feed) according to the requested mime type
     *
     * @param context
     * @param id
     * @param entry
     * @return an instance of EPCISResource
     */
    @Path(SUBSCRIPTIONS_ID)
    @Consumes(TEXT_XML)
    @POST
    public EPCISResource addEntryToSubscription(@Context UriInfo context, @PathParam(ID) String id, String entry) {
        SubscriptionBusinessLogic logic = new SubscriptionBusinessLogic();

        return logic.addEntryToSubscription(context, id, entry);
    }

    /**
     * DELETE method for deleting a feed
     * Returns a representation of a link back to the subscriptions according to the requested mime type
     *
     * @param context
     * @param id
     * @param data
     * @return an instance of EPCISResource
     */
    @Path(SUBSCRIPTIONS_ID)
    @DELETE
    public EPCISResource unsubscribeSubscription(@Context UriInfo context, @PathParam(ID) String id, @DefaultValue("unsubscribe") String data) {
        SubscriptionBusinessLogic logic = new SubscriptionBusinessLogic();

        return logic.unsubscribeSubscription(context, id, data);
    }

    /**
     * Returns a representation of the capture resource (howto) according to the requested mime type
     *
     * @param context
     * @return an instance of EPCISResource
     */
    @Path(CAPTURE)
    @GET
    public EPCISResource getCapture(@Context UriInfo context) {
        CaptureBusinessLogic logic = new CaptureBusinessLogic();

        return logic.getCapture(context);
    }

    /**
     * POST method for adding a captured event
     *
     * @param context
     * @param event representation of an EPCIS Event whose XML Schema is definded in the EPCGlobal's standard for the EPCIS
     * @return an HTTP EPCISResource with content of the added event resource.
     */
    @Path(CAPTURE)
    @POST
    public EPCISResource addCapture(@Context UriInfo context, String event) {
        CaptureBusinessLogic logic = new CaptureBusinessLogic();

        return logic.addCapture(context, event);
    }

    /**
     * Returns a representation of the capture simulator resource
     *
     *
     * @param context
     *
     * @return an instance of EPCISResource
     */
    @Path(CAPTURE_SIMULATOR)
    @GET
    public EPCISResource getCaptureSimulation(@Context UriInfo context) {
        CaptureBusinessLogic logic = new CaptureBusinessLogic();

        return logic.getCaptureSimulation(context);
    }

    /**
     * POST method for simulating a captured event in encoded in query params
     *
     *
     * @param context
     * @param eventTime
     * @param timeZoneOffset
     * @param businessStep
     * @param disposition
     * @param readPoint
     * @param businessLocation
     * @param businessTransaction
     * @param eventType
     * @param epc
     * @param action
     * @param parentId
     * @param epcClass
     * @param quantity
     *
     * @return
     */
    @Path(CAPTURE_SIMULATOR)
    @Consumes(APPLICATION_FORM_URLENCODED)
    @POST
    public EPCISResource addCaptureSimulation(@Context UriInfo context, @DefaultValue("")
            @FormParam(EVENT_TIME_REST) String eventTime, @DefaultValue("")
            @FormParam(TIME_ZONE_OFFSET) String timeZoneOffset, @DefaultValue("")
            @FormParam(BUSINESS_STEP_REST) String businessStep, @DefaultValue("")
            @FormParam(DISPOSITION_REST) String disposition, @DefaultValue("")
            @FormParam(READ_POINT_REST) String readPoint, @DefaultValue("")
            @FormParam(BUSINESS_LOCATION_REST) String businessLocation, @DefaultValue("")
            @FormParam(BUSINESS_TRANSACTION_TYPE_REST) String businessTransaction, @DefaultValue("")
            @FormParam(EVENT_TYPE_REST) String eventType, @DefaultValue("")
            @FormParam(EPC_REST) String epc, @DefaultValue("")
            @FormParam(ACTION_REST) String action, @DefaultValue("")
            @FormParam(PARENT_ID_REST) String parentId, @DefaultValue("")
            @FormParam(EPC_CLASS_REST) String epcClass, @DefaultValue("")
            @FormParam(QUANTITY_REST) String quantity) {
        CaptureBusinessLogic logic = new CaptureBusinessLogic();

        return logic.addCaptureSimulation(context, eventTime, timeZoneOffset, businessStep, disposition, readPoint, businessLocation, businessTransaction, eventType, epc, action, parentId, epcClass, quantity);
    }
}
