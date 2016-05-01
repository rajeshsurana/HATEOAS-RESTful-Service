package com.asu.appealing.client;

import com.asu.appealing.model.Appeal;
import com.asu.appealing.model.AppealBuilder;
import com.asu.appealing.model.AppealStatus;
import com.asu.appealing.model.GradedItem;
import com.asu.appealing.model.GradedItemBuilder;
import com.asu.appealing.representations.AppealRepresentation;
import com.asu.appealing.representations.AsuUri;
import com.asu.appealing.representations.GradedItemRepresentation;

import java.net.URI;
import com.asu.appealing.representations.Link;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    private static final String ASU_MEDIA_TYPE = "application/vnd-cse564-appeals+xml";
    private static final long TWO_SECONDS = 2000; 
    
    private static final String ENTRY_POINT_URI_APPEAL = "http://localhost:8080/CSE564_HATEAOS_ASUAppealingServer/webresources/appeal";
    private static final String ENTRY_POINT_URI_GRADEDITEM = "http://localhost:8080/CSE564_HATEAOS_ASUAppealingServer/webresources/gradedItem";
    
    public static void main(String[] args) throws Exception {
        URI serviceUri = new URI(ENTRY_POINT_URI_GRADEDITEM);
        populateGradedItems(serviceUri);
        serviceUri = new URI(ENTRY_POINT_URI_APPEAL);
        AppealBuilder appealBuilder = new AppealBuilder();
        GradedItemBuilder gradedItemBuilder = new GradedItemBuilder();
        happyCase(serviceUri, appealBuilder, gradedItemBuilder);
        pause(TWO_SECONDS);
        abandonedCase(serviceUri, appealBuilder, gradedItemBuilder);
        pause(TWO_SECONDS);
        followUpCase(serviceUri, appealBuilder, gradedItemBuilder);
        pause(TWO_SECONDS);
        badStartCase(serviceUri, appealBuilder);
        pause(TWO_SECONDS);
        badIDCase(serviceUri, appealBuilder);        
    }

    private static void pause(long backOffTimeInMillis) {
        try {
            Thread.sleep(backOffTimeInMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void populateGradedItems(URI serviceUri) throws Exception {
        System.out.println("\n\n***************************************** Populate graded Items START *****************************************************");
        LOG.info("SETUP: First we will do some basic setup before we start appeal process.");
        LOG.info("SETUP: Professor posts score of few students with Service URI {}", serviceUri);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        GradedItemBuilder gradedItemBuilder = new GradedItemBuilder();
        for(int i=0; i<3; i++){
            GradedItem gradedItem = gradedItemBuilder.build();
            LOG.info("SETUP: Post graded item = {}", gradedItem);
            GradedItemRepresentation gradedItemRepresentation = client.resource(serviceUri).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(GradedItemRepresentation.class, new ClientGradedItem(gradedItem));
            LOG.debug("SETUP: Created gradedItemRepresentation {} denoted by the URI {}", gradedItemRepresentation, gradedItemRepresentation.getSelfLink().getUri().toString());
        }
        System.out.println("***************************************** Populate graded Items ENDS *****************************************************\n");
    }
    
    private static void badStartCase(URI serviceUri, AppealBuilder appealBuilder ) throws Exception {
        System.out.println("\n\n***************************************** BAD START CASE STARTS *****************************************************");
        LOG.info("Starting Bad Start Case Test with BAD URI {}", serviceUri + "bad-uri");
        
        // Student try to create an appeal with invalid entry URI
        LOG.info("\n\nStep 1. Student try to create the Appeal with invalid entry URI");
        System.out.println(String.format("About to start BAD START CASE test. Student creating Appeal at [%s] via POST", serviceUri.toString() + "bad-uri"));
        Appeal appeal = appealBuilder.build();
        LOG.debug("Created draft appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        Link badLink = new Link("bad", new AsuUri(serviceUri.toString() + "bad-uri"), ASU_MEDIA_TYPE);
        LOG.debug("Create bad link {}", badLink);
        ClientResponse badStartResponse = client.resource(badLink.getUri()).accept(badLink.getMediaType()).type(badLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.debug("Created Bad Start Response {}", badStartResponse);
        System.out.println(String.format("Tried to create appeal with bad URI at [%s] via POST, outcome [%d]", badLink.getUri().toString(), badStartResponse.getStatus()));
        System.out.println("***************************************** BAD START CASE ENDS *****************************************************\n\n");  
    }
    
    private static void badIDCase(URI serviceUri, AppealBuilder appealBuilder ) throws Exception {
        System.out.println("\n\n***************************************** BAD ID CASE STARTS *****************************************************");
        LOG.info("Starting Bad ID Case Test with Service URI {}", serviceUri);
        
        // Student creates a DRAFT appeal
        LOG.info("\n\nStep 1. Student creates the Draft Appeal");
        System.out.println(String.format("About to start BAD ID test. Student creating Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appealBuilder.build();
        LOG.debug("Created draft appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal created at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        
        // Student submits the appeal
        LOG.info("\n\nStep 2. Student submits the Appeal");
        System.out.println(String.format("Student about to submit appeal at [%s] via POST", appealRepresentation.getSubmitLink().getUri().toString()));
        Link submitLink = appealRepresentation.getSubmitLink();
        LOG.debug("Created appeal submit link {}", submitLink);
        appeal.setStatus(AppealStatus.SUBMITTED);
        AppealRepresentation submittedRepresentation = client.resource(submitLink.getUri()).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created submittedRepresentation {} denoted by the URI {}", submittedRepresentation, submittedRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal submitted at [%s]", submittedRepresentation.getSelfLink().getUri().toString()));
        
        // Student follow up the appeal
        LOG.info("\n\nStep 3. Student follow up the Appeal with bad ID");
        System.out.println(String.format("Student about to follow up appeal at with bad ID [%s] via POST", serviceUri.toString() + "/9999999999"));
        Link badIDFollowUpLink = new Link("badID", new AsuUri(serviceUri.toString() + "/9999999999"), ASU_MEDIA_TYPE);
        LOG.debug("Created bad id follow up appeal link {}", badIDFollowUpLink);
        appeal.setStatus(AppealStatus.FOLLOWUP);
        ClientResponse badIDFollowUpResponse = client.resource(badIDFollowUpLink.getUri()).accept(badIDFollowUpLink.getMediaType()).type(badIDFollowUpLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.debug("Created Bad Id Follow Up Response {}", badIDFollowUpResponse);
        System.out.println(String.format("Tried to follow up appeal with bad ID URI at [%s] via POST, outcome [%d]", badIDFollowUpLink.getUri().toString(), badIDFollowUpResponse.getStatus()));
       
        System.out.println("***************************************** BAD ID CASE ENDS *****************************************************\n\n");
    }
    
    private static void abandonedCase(URI serviceUri, AppealBuilder appealBuilder, GradedItemBuilder gradedItemBuilder ) throws Exception {
        System.out.println("\n\n***************************************** ABANDONED CASE STARTS *****************************************************");
        LOG.info("Starting Abandoned Case Test with Service URI {}", serviceUri);
        
        // Student creates a DRAFT appeal
        LOG.info("\n\nStep 1. Student creates the Draft Appeal");
        System.out.println(String.format("About to start ABANDINED CASE test. Student creating Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appealBuilder.build();
        LOG.debug("Created draft appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal created at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
       
        // Student abandons/deletes the appeal
        LOG.info("\n\nStep 2. Student abandons/deletes the appeal");
        System.out.println(String.format("Trying to delete the appeal from [%s] via DELETE.", appealRepresentation.getSelfLink().getUri().toString()));
        Link deleteLink = appealRepresentation.getCancelLink();
        ClientResponse finalResponse = client.resource(deleteLink.getUri()).delete(ClientResponse.class);
        System.out.println(String.format("Tried to delete appeal, HTTP status [%d]", finalResponse.getStatus()));
        if(finalResponse.getStatus() == 200) {
            System.out.println(String.format("Appeal has been successfully abandoned/deleted"));
        }
        gradedItemBuilder.build();
        System.out.println("***************************************** ABANDONED CASE ENDS *****************************************************\n\n");
    }
    
    private static void followUpCase(URI serviceUri, AppealBuilder appealBuilder, GradedItemBuilder gradedItemBuilder ) throws Exception {
        System.out.println("\n\n***************************************** FOLLOW UP CASE STARTS *****************************************************");
        LOG.info("Starting Follow Up Case Test with Service URI {}", serviceUri);
        
        // Student creates a DRAFT appeal
        LOG.info("\n\nStep 1. Student creates the Draft Appeal");
        System.out.println(String.format("About to start FOLLOW UP CASE test. Student creating Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appealBuilder.build();
        LOG.debug("Created draft appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal created at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        
        // Student submits the appeal
        LOG.info("\n\nStep 2. Student submits the Appeal");
        System.out.println(String.format("Student about to submit appeal at [%s] via POST", appealRepresentation.getSubmitLink().getUri().toString()));
        Link submitLink = appealRepresentation.getSubmitLink();
        LOG.debug("Created appeal submit link {}", submitLink);
        appeal.setStatus(AppealStatus.SUBMITTED);
        AppealRepresentation submittedRepresentation = client.resource(submitLink.getUri()).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created submittedRepresentation {} denoted by the URI {}", submittedRepresentation, submittedRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal submitted at [%s]", submittedRepresentation.getSelfLink().getUri().toString()));
        
        // Student follow up the appeal
        LOG.info("\n\nStep 3. Student follow up the Appeal as he don't hear from Professor");
        System.out.println(String.format("Student about to follow up appeal at [%s] via GET", submittedRepresentation.getFollowupLink().getUri().toString()));
        Link followUpLink = submittedRepresentation.getFollowupLink();
        LOG.debug("Created appeal follow up link {}", followUpLink);
        AppealRepresentation followUpRepresentation = client.resource(submitLink.getUri()).accept(ASU_MEDIA_TYPE).get(AppealRepresentation.class);
        LOG.debug("Created followUpRepresentation {} denoted by the URI {}", followUpRepresentation, followUpRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal followed up at [%s]", followUpRepresentation.getSelfLink().getUri().toString()));
        System.out.println(String.format("Appeal status returned in follow up - [%s]", followUpRepresentation.getAppeal().getStatus()));
        
        // Professor Reject the Appeal
        LOG.info("\n\nStep 4. Professor Reject the Appeal");
        System.out.println(String.format("Professor about to reject appeal at [%s] via POST", appealRepresentation.getSelfLink().getUri().toString()));
        Link rejectLink = appealRepresentation.getSelfLink();
        LOG.debug("Created appeal reject link {}", rejectLink);
        appeal.setStatus(AppealStatus.REJECTED);
        AppealRepresentation rejectedRepresentation = client.resource(rejectLink.getUri()).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created approvedRepresentation {} denoted by the URI {}", rejectedRepresentation, rejectedRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal rejected at [%s]", rejectedRepresentation.getSelfLink().getUri().toString()));
        
        // Professor Updates the comment
        LOG.info("\n\nStep 5. Professor Updates graded Item comment");
        System.out.println(String.format("Professor about to update graded Item at [%s] via POST", rejectedRepresentation.getGradedItemLink().getUri().toString()));
        Link gradedItemUpdateLink = rejectedRepresentation.getGradedItemLink();
        LOG.debug("Created gradedItem Update link {}", gradedItemUpdateLink);
        GradedItem gradedItem = gradedItemBuilder.build();
        gradedItem.setComment("It is not stated clearly.");
        GradedItemRepresentation updatedGradeRepresentation = client.resource(gradedItemUpdateLink.getUri()).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(GradedItemRepresentation.class, new ClientGradedItem(gradedItem));
        LOG.debug("Created updatedGradeRepresentation {} denoted by the URI {}", updatedGradeRepresentation, updatedGradeRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("GradedItem updated at [%s]", updatedGradeRepresentation.getSelfLink().getUri().toString()));
        
        // Student retrieve the appeal and its status
        LOG.info("\n\nStep 6. Student retrieve the appeal");
        System.out.println(String.format("Student about to retrieve appeal at [%s] via GET", submittedRepresentation.getSelfLink().getUri().toString()));
        Link appealLink = submittedRepresentation.getSelfLink();
        AppealRepresentation readAppealRepresentation = client.resource(appealLink.getUri()).accept(ASU_MEDIA_TYPE).get(AppealRepresentation.class);
        System.out.println(String.format("Appeal Processed, status [%s]", readAppealRepresentation.getStatus()));
        
        // Student retrieve the graded item 
        LOG.info("\n\nStep 7. Student retrieve the graded item");
        System.out.println(String.format("Student about to retrieve graded item at [%s] via GET", readAppealRepresentation.getGradedItemLink().getUri().toString()));
        Link gradedItemLink = readAppealRepresentation.getGradedItemLink();
        GradedItemRepresentation readGradedItemRepresentation = client.resource(gradedItemLink.getUri()).accept(ASU_MEDIA_TYPE).get(GradedItemRepresentation.class);
        System.out.println(String.format("Updated Graded Item retrieved with comment = [%s] and score  = [%d]", readGradedItemRepresentation.getComment(), readGradedItemRepresentation.getScore()));
               
        System.out.println("***************************************** FOLLOW UP CASE ENDS *****************************************************\n\n");
    
    }
    
    private static void happyCase(URI serviceUri, AppealBuilder appealBuilder, GradedItemBuilder gradedItemBuilder ) throws Exception {
        System.out.println("\n\n***************************************** HAPPY CASE STARTS *****************************************************");
        LOG.info("Starting Happy Case Test with Service URI {}", serviceUri);
        
        // Student creates a DRAFT appeal
        LOG.info("\n\nStep 1. Student creates the Draft Appeal");
        System.out.println(String.format("About to start HAPPY CASE test. Student creating Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appealBuilder.build();
        LOG.debug("Created draft appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal created at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        
        // Student submits the appeal
        LOG.info("\n\nStep 2. Student submits the Appeal");
        System.out.println(String.format("Student about to submit appeal at [%s] via POST", appealRepresentation.getSubmitLink().getUri().toString()));
        Link submitLink = appealRepresentation.getSubmitLink();
        LOG.debug("Created appeal submit link {}", submitLink);
        appeal.setStatus(AppealStatus.SUBMITTED);
        AppealRepresentation submittedRepresentation = client.resource(submitLink.getUri()).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created submittedRepresentation {} denoted by the URI {}", submittedRepresentation, submittedRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal submitted at [%s]", submittedRepresentation.getSelfLink().getUri().toString()));
        
        // Professor Approve the Appeal
        LOG.info("\n\nStep 3. Professor Approve the Appeal");
        System.out.println(String.format("Professor about to approve appeal at [%s] via POST", appealRepresentation.getSelfLink().getUri().toString()));
        Link approveLink = appealRepresentation.getSelfLink();
        LOG.debug("Created appeal approve link {}", approveLink);
        appeal.setStatus(AppealStatus.APPROVED);
        AppealRepresentation approvedRepresentation = client.resource(approveLink.getUri()).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created approvedRepresentation {} denoted by the URI {}", approvedRepresentation, approvedRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal approved at [%s]", approvedRepresentation.getSelfLink().getUri().toString()));
        
        // Professor Updates the score and comment
        LOG.info("\n\nStep 4. Professor Updates graded Item");
        System.out.println(String.format("Professor about to update graded Item at [%s] via POST", approvedRepresentation.getGradedItemLink().getUri().toString()));
        Link gradedItemUpdateLink = approvedRepresentation.getGradedItemLink();
        LOG.debug("Created gradedItem Update link {}", gradedItemUpdateLink);
        GradedItem gradedItem = gradedItemBuilder.build();
        gradedItem.setScore(60);
        gradedItem.setComment("Score Updated.");
        GradedItemRepresentation updatedGradeRepresentation = client.resource(gradedItemUpdateLink.getUri()).accept(ASU_MEDIA_TYPE).type(ASU_MEDIA_TYPE).post(GradedItemRepresentation.class, new ClientGradedItem(gradedItem));
        LOG.debug("Created updatedGradeRepresentation {} denoted by the URI {}", updatedGradeRepresentation, updatedGradeRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("GradedItem updated at [%s]", updatedGradeRepresentation.getSelfLink().getUri().toString()));
        
        // Student retrieve the appeal and its status
        LOG.info("\n\nStep 5. Student retrieve the appeal");
        System.out.println(String.format("Student about to retrieve appeal at [%s] via GET", submittedRepresentation.getSelfLink().getUri().toString()));
        Link appealLink = submittedRepresentation.getSelfLink();
        AppealRepresentation readAppealRepresentation = client.resource(appealLink.getUri()).accept(ASU_MEDIA_TYPE).get(AppealRepresentation.class);
        System.out.println(String.format("Appeal Processed, status [%s]", readAppealRepresentation.getStatus()));
        
        // Student retrieve the graded item 
        LOG.info("\n\nStep 6. Student retrieve the graded item");
        System.out.println(String.format("Student about to retrieve graded item at [%s] via GET", readAppealRepresentation.getGradedItemLink().getUri().toString()));
        Link gradedItemLink = readAppealRepresentation.getGradedItemLink();
        GradedItemRepresentation readGradedItemRepresentation = client.resource(gradedItemLink.getUri()).accept(ASU_MEDIA_TYPE).get(GradedItemRepresentation.class);
        System.out.println(String.format("Updated Graded Item retrieved with comment = [%s] and score  = [%d]", readGradedItemRepresentation.getComment(), readGradedItemRepresentation.getScore()));
               
        System.out.println("***************************************** HAPPY CASE ENDS *****************************************************\n\n");
    }    
}
