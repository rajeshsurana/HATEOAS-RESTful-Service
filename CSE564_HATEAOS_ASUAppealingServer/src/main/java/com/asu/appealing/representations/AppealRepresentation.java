package com.asu.appealing.representations;

import com.asu.appealing.activities.InvalidAppealException;
import com.asu.appealing.activities.UriExchange;
import com.asu.appealing.model.Appeal;
import com.asu.appealing.model.AppealStatus;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "appeal", namespace = Representation.ASU_NAMESPACE)
public class AppealRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealRepresentation.class);

    @XmlElement(name = "studentId", namespace = Representation.ASU_NAMESPACE)
    private int studentId;
    @XmlElement(name = "assignmentId", namespace = Representation.ASU_NAMESPACE)
    private int assignmentId;
    @XmlElement(name = "description", namespace = Representation.ASU_NAMESPACE)
    private String description;
    @XmlElement(name = "status", namespace = Representation.ASU_NAMESPACE)
    private AppealStatus status;

    /**
     * For JAXB :-(
     */
    AppealRepresentation() {
        LOG.info("Executing AppealRepresentation constructor");
    }

    public static AppealRepresentation fromXmlString(String xmlRepresentation) {
        LOG.info("Creating an Appeal object from the XML = {}", xmlRepresentation);
                
        AppealRepresentation appealRepresentation = null;     
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            appealRepresentation = (AppealRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
        } catch (Exception e) {
            throw new InvalidAppealException(e);
        }
        
        LOG.debug("Generated the object {}", appealRepresentation);
        return appealRepresentation;
    }
    
    public static AppealRepresentation createResponseAppealRepresentation(Appeal appeal, AsuUri appealUri) {
        LOG.info("Creating a Response Appeal for appeal = {} and appeal URI", appeal.toString(), appealUri.toString());
        
        AppealRepresentation appealRepresentation = null; 
        
        if(appeal.getStatus() == AppealStatus.DRAFT) {
            LOG.debug("The appeal status is {}", AppealStatus.DRAFT);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link(RELATIONS_URI + "cancel", appealUri),  
                    new Link(RELATIONS_URI + "update", appealUri),
                    new Link(RELATIONS_URI + "submit", appealUri),
                    new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.SUBMITTED) {
            LOG.debug("The appeal status is {}", AppealStatus.SUBMITTED);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link(RELATIONS_URI + "followup", appealUri),
                    new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.APPROVED) {
            LOG.debug("The appeal status is {}", AppealStatus.APPROVED);
            appealRepresentation = new AppealRepresentation(appeal,
                    new Link(Representation.SELF_REL_VALUE, appealUri),
                    new Link(RELATIONS_URI + "gradedItem", UriExchange.gradedItemForAppeal(appealUri))); 
        } else if(appeal.getStatus() == AppealStatus.REJECTED) {
            LOG.debug("The appeal status is {}", AppealStatus.REJECTED);
            appealRepresentation = new AppealRepresentation(appeal,
                    new Link(Representation.SELF_REL_VALUE, appealUri),
                    new Link(RELATIONS_URI + "gradedItem", UriExchange.gradedItemForAppeal(appealUri)));            
        } else {
            LOG.debug("The appeal status is in an unknown status");
            throw new RuntimeException("Unknown Appeal Status");
        }
        
        LOG.debug("The appeal representation created for the Create Response Appeal Representation is {}", appealRepresentation);
        
        return appealRepresentation;
    }

    public AppealRepresentation(Appeal appeal, Link... links) {
        LOG.info("Creating an Appeal Representation for appeal = {} and links = {}", appeal.toString(), links.toString());
        
        try {
            this.studentId = appeal.getStudentId();
            this.assignmentId = appeal.getAssignmentId();
            this.description = appeal.getDescription();
            this.status = appeal.getStatus();
            this.links = java.util.Arrays.asList(links);
        } catch (Exception ex) {
            throw new InvalidAppealException(ex);
        }
        
        LOG.debug("Created the AppealRepresentation {}", this);
    }

    @Override
    public String toString() {
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Appeal getAppeal() {
        if (studentId == 0 || assignmentId == 0 || description == null) {
            throw new InvalidAppealException();
        }

        return new Appeal(studentId, assignmentId, description, status);
    }

    public Link getCancelLink() {
        return getLinkByName(RELATIONS_URI + "cancel");
    }

    public Link getUpdateLink() {
        return getLinkByName(RELATIONS_URI + "update");
    }
    
    public Link getSubmitLink() {
        return getLinkByName(RELATIONS_URI + "submit");
    }
    
    public Link getFollowupLink() {
        return getLinkByName(RELATIONS_URI + "followup");
    }

    public Link getGradedItemLink() {
        return getLinkByName(RELATIONS_URI + "gradedItem");
    }
        
    public Link getSelfLink() {
        return getLinkByName("self");
    }
    
    public AppealStatus getStatus() {
        return status;
    }

}
