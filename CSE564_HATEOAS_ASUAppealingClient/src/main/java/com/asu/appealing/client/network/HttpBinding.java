package com.asu.appealing.client.network;

import com.asu.appealing.client.activities.CannotUpdateAppealException;
import com.asu.appealing.client.activities.MalformedAppealException;
import com.asu.appealing.model.Appeal;
import com.asu.appealing.representations.AppealRepresentation;
import java.net.URI;

import com.asu.appealing.client.activities.CannotCancelException;
import com.asu.appealing.client.activities.MalformedGradedItemException;
import com.asu.appealing.client.activities.NotFoundException;
import com.asu.appealing.client.activities.ServiceFailureException;
import static com.asu.appealing.model.AppealBuilder.appeal;
import com.asu.appealing.model.GradedItem;
import com.asu.appealing.representations.GradedItemRepresentation;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class HttpBinding {

    private static final String ASU_MEDIA_TYPE = "application/vnd-cse564-appeals+xml";

    public AppealRepresentation createAppeal(Appeal appeal, URI appealUri) throws MalformedAppealException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(ClientResponse.class, new AppealRepresentation(appeal));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedAppealException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 201) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while creating appeal resource [%s]", status, appealUri.toString()));
    }
    
    public AppealRepresentation retrieveAppeal(URI appealUri) throws NotFoundException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(ASU_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();

        if (status == 404) {
            throw new NotFoundException ();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response while retrieving appeal resource [%s]", appealUri.toString()));
    }

    public AppealRepresentation updateAppeal(Appeal appeal, URI appealUri) throws MalformedAppealException, ServiceFailureException, NotFoundException,
            CannotUpdateAppealException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(ClientResponse.class, new AppealRepresentation(appeal));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedAppealException();
        } else if (status == 404) {
            throw new NotFoundException();
        } else if (status == 409) {
            throw new CannotUpdateAppealException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while udpating appeal resource [%s]", status, appealUri.toString()));
    }

    public AppealRepresentation deleteAppeal(URI appealUri) throws ServiceFailureException, CannotCancelException, NotFoundException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(ASU_MEDIA_TYPE).delete(ClientResponse.class);

        int status = response.getStatus();
        if (status == 404) {
            throw new NotFoundException();
        } else if (status == 405) {
            throw new CannotCancelException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while deleting appeal resource [%s]", status, appealUri.toString()));
    }
  
    public GradedItemRepresentation createGradedItem(GradedItem gradedItem, URI gradedItemUri) throws MalformedGradedItemException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(gradedItemUri).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(ClientResponse.class, new GradedItemRepresentation(gradedItem));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedGradedItemException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 201) {
            return response.getEntity(GradedItemRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while creating graded item resource [%s]", status, gradedItemUri.toString()));
    }
    
    public GradedItemRepresentation retrieveGradedItem(URI gradedItemUri) throws NotFoundException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(gradedItemUri).accept(ASU_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();

        if (status == 404) {
            throw new NotFoundException ();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(GradedItemRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response while retrieving graded item resource [%s]", gradedItemUri.toString()));
    }
    
    public GradedItemRepresentation updateGradedItem(GradedItem gradedItem, URI gradedItemUri) throws MalformedGradedItemException, ServiceFailureException, NotFoundException {
        Client client = Client.create();
        ClientResponse response = client.resource(gradedItemUri).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(ClientResponse.class, new GradedItemRepresentation(gradedItem));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedGradedItemException();
        } else if (status == 404) {
            throw new NotFoundException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(GradedItemRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while udpating graded item resource [%s]", status, gradedItemUri.toString()));
    }
}
