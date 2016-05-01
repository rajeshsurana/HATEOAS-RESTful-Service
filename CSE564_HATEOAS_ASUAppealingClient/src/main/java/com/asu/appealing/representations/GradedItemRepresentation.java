package com.asu.appealing.representations;

import com.asu.appealing.activities.InvalidGradedItemException;
import com.asu.appealing.model.GradedItem;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "gradedItem", namespace = Representation.ASU_NAMESPACE)
public class GradedItemRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradedItemRepresentation.class);

    @XmlElement(name = "studentId", namespace = Representation.ASU_NAMESPACE)
    private int studentId;
    @XmlElement(name = "assignmentId", namespace = Representation.ASU_NAMESPACE)
    private int assignmentId;
    @XmlElement(name = "score", namespace = Representation.ASU_NAMESPACE)
    private int score;
    @XmlElement(name = "comment", namespace = Representation.ASU_NAMESPACE)
    private String comment;

    /**
     * For JAXB :-(
     */
    GradedItemRepresentation() {
        LOG.info("Executing GradedItemRepresentation constructor");
    }

    public static GradedItemRepresentation fromXmlString(String xmlRepresentation) {
        LOG.info("Creating an Graded Item object from the XML = {}", xmlRepresentation);
                
        GradedItemRepresentation gradedItemRepresentation = null;     
        try {
            JAXBContext context = JAXBContext.newInstance(GradedItemRepresentation.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            gradedItemRepresentation = (GradedItemRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
        } catch (Exception e) {
            throw new InvalidGradedItemException(e);
        }
        
        LOG.debug("Generated the object {}", gradedItemRepresentation);
        return gradedItemRepresentation;
    }
    
    public static GradedItemRepresentation createResponseGradedItemRepresentation(GradedItem gradedItem, AsuUri gradedItemUri) {
        LOG.info("Creating a Response Graded Item for gradedItem = {} and gradedItem URI", gradedItem.toString(), gradedItemUri.toString());
        
        GradedItemRepresentation gradedItemRepresentation = null; 
        
            gradedItemRepresentation = new GradedItemRepresentation(gradedItem, 
                    new Link(RELATIONS_URI + "update", gradedItemUri),
                    new Link(Representation.SELF_REL_VALUE, gradedItemUri));
        
        
        LOG.debug("The graded item representation created for the Create Response GradedItem Representation is {}", gradedItemRepresentation);
        
        return gradedItemRepresentation;
    }

    public GradedItemRepresentation(GradedItem gradedItem, Link... links) {
        LOG.info("Creating a Graded Item Representation for gradedItem = {} and links = {}", gradedItem.toString(), links.toString());
        
        try {
            this.studentId = gradedItem.getStudentId();
            this.assignmentId = gradedItem.getAssignmentId();
            this.score = gradedItem.getScore();
            this.comment = gradedItem.getComment();
            this.links = java.util.Arrays.asList(links);
        } catch (Exception ex) {
            throw new InvalidGradedItemException(ex);
        }
        
        LOG.debug("Created the GradedItemRepresentation {}", this);
    }

    @Override
    public String toString() {
        try {
            JAXBContext context = JAXBContext.newInstance(GradedItemRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GradedItem getGradedItem() {
        if (studentId == 0 || assignmentId == 0) {
            throw new InvalidGradedItemException();
        }

        return new GradedItem(studentId, assignmentId, score, comment);
    }

    public Link getUpdateLink() {
        return getLinkByName(RELATIONS_URI + "update");
    }
    
    public Link getSelfLink() {
        return getLinkByName("self");
    }
    
    public int getScore() {
        return score;
    }
    
    public String getComment() {
        return comment;
    }

}
