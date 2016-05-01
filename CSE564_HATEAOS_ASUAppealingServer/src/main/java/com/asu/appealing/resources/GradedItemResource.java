package com.asu.appealing.resources;

import com.asu.appealing.activities.CreateGradedItemActivity;
import com.asu.appealing.activities.InvalidGradedItemException;
import com.asu.appealing.activities.NoSuchGradedItemException;
import com.asu.appealing.activities.UpdateException;
import com.asu.appealing.activities.ReadGradedItemActivity;
import com.asu.appealing.activities.UpdateGradedItemActivity;
import com.asu.appealing.representations.AsuUri;
import com.asu.appealing.representations.GradedItemRepresentation;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/gradedItem")
public class GradedItemResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradedItemResource.class);

    private @Context UriInfo uriInfo;

    public GradedItemResource() {
        LOG.info("GradedItemResource constructor");
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public GradedItemResource(UriInfo uriInfo) {
        LOG.info("GradedItemResource constructor with mock uriInfo {}", uriInfo);
        this.uriInfo = uriInfo;  
    }
    
    @GET
    @Path("/{gradedItemId}")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response getGradedItem() {
        LOG.info("Retrieving a GradedItem Resource");
        
        Response response;
        
        try {
            GradedItemRepresentation responseRepresentation = new ReadGradedItemActivity().retrieveByUri(new AsuUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(responseRepresentation).build();
        } catch(NoSuchGradedItemException nsge) {
            LOG.debug("No such graded item");
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong retriveing the graded item");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.debug("Retrieved the graded item resource", response);
        
        return response;
    }
    
    @POST
    @Consumes("application/vnd-cse564-appeals+xml")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response createGradedItem(String gradedItemRepresentation) {
        LOG.info("Creating a GradedItem Resource");
        
        Response response;
        
        try {
            GradedItemRepresentation responseRepresentation = new CreateGradedItemActivity().create(GradedItemRepresentation.fromXmlString(gradedItemRepresentation).getGradedItem(), new AsuUri(uriInfo.getRequestUri()));
            response = Response.created(responseRepresentation.getUpdateLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidGradedItemException ige) {
            LOG.debug("Invalid GradedItem - Problem with the graded item representation {}", gradedItemRepresentation);
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            LOG.debug("Someting went wrong creating the graded item resource");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.debug("Resulting response for creating the graded item resource is {}", response);
        
        return response;
    }


    @POST
    @Path("/{appealId}")
    @Consumes("application/vnd-cse564-appeals+xml")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response updateGradedItem(String gradedItemRepresentation) {
        LOG.info("Updating a graded item Resource");
        
        Response response;
        
        try {
            GradedItemRepresentation responseRepresentation = new UpdateGradedItemActivity().update(GradedItemRepresentation.fromXmlString(gradedItemRepresentation).getGradedItem(), new AsuUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(responseRepresentation).build();
        } catch (InvalidGradedItemException ige) {
            LOG.debug("Invalid graded item in the XML representation {}", gradedItemRepresentation);
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (NoSuchGradedItemException nsge) {
            LOG.debug("No such graded item resource to update");
            response = Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.debug("Problem updating the graded item resource");
            response = Response.status(Status.CONFLICT).build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong updating the graded item resource");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        } 
        
        LOG.debug("Resulting response for updating the graded item resource is {}", response);
        
        return response;
     }
}
