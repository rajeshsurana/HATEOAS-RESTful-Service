package com.asu.appealing.client;

import com.asu.appealing.model.GradedItem;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.asu.appealing.representations.Representation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "gradedItem", namespace = Representation.ASU_NAMESPACE)
public class ClientGradedItem {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientGradedItem.class);
    
    @XmlElement(name = "studentId", namespace = Representation.ASU_NAMESPACE)
    private int studentId;
    @XmlElement(name = "assignmentId", namespace = Representation.ASU_NAMESPACE)
    private int assignmentId;
    @XmlElement(name = "score", namespace = Representation.ASU_NAMESPACE)
    private int score;
    @XmlElement(name = "comment", namespace = Representation.ASU_NAMESPACE)
    private String comment;
    
    private ClientGradedItem(){}
    
    public ClientGradedItem(GradedItem gradedItem) {
        LOG.debug("Executing ClientGradedItem constructor");
        this.studentId = gradedItem.getStudentId();
        this.assignmentId = gradedItem.getAssignmentId();
        this.score = gradedItem.getScore();
        this.comment = gradedItem.getComment();
    }
    
    public GradedItem getGradedItem() {
        LOG.debug("Executing ClientGradedItem.getGradedItem");
        return new GradedItem(studentId, assignmentId, score, comment);
    }
    
    public int getStudentId() {
        LOG.debug("Executing ClientGradedItem.getStudentId");
        return studentId;
    }
    
    public int getAssignmentId() {
        LOG.debug("Executing ClientGradedItem.getAssignmentId");
        return assignmentId;
    }
    
    public int getScore() {
        LOG.debug("Executing ClientGradedItem.getAssignmentId");
        return score;
    }
    
    public String getComment() {
        LOG.debug("Executing ClientGradedItem.getDescription");
        return comment;
    }

    @Override
    public String toString() {
        LOG.debug("Executing ClientAppeal.toString");
        try {
            JAXBContext context = JAXBContext.newInstance(ClientGradedItem.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}