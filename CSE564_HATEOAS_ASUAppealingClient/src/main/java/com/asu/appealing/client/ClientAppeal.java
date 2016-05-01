package com.asu.appealing.client;

import com.asu.appealing.model.Appeal;
import com.asu.appealing.model.AppealStatus;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.asu.appealing.representations.Representation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "appeal", namespace = Representation.ASU_NAMESPACE)
public class ClientAppeal {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientAppeal.class);
    
    @XmlElement(name = "studentId", namespace = Representation.ASU_NAMESPACE)
    private int studentId;
    @XmlElement(name = "assignmentId", namespace = Representation.ASU_NAMESPACE)
    private int assignmentId;
    @XmlElement(name = "description", namespace = Representation.ASU_NAMESPACE)
    private String description;
    @XmlElement(name = "status", namespace = Representation.ASU_NAMESPACE)
    private AppealStatus status;
    
    private ClientAppeal(){}
    
    public ClientAppeal(Appeal appeal) {
        LOG.debug("Executing ClientAppeal constructor");
        this.studentId = appeal.getStudentId();
        this.assignmentId = appeal.getAssignmentId();
        this.description = appeal.getDescription();
        this.status = appeal.getStatus();
    }
    
    public Appeal getAppeal() {
        LOG.debug("Executing ClientAppeal.getAppeal");
        return new Appeal(studentId, assignmentId, description, status);
    }
    
    public int getStudentId() {
        LOG.debug("Executing ClientAppeal.getStudentId");
        return studentId;
    }
    
    public int getAssignmentId() {
        LOG.debug("Executing ClientAppeal.getAssignmentId");
        return assignmentId;
    }
    public String getDescription() {
        LOG.debug("Executing ClientAppeal.getDescription");
        return description;
    }

    @Override
    public String toString() {
        LOG.debug("Executing ClientAppeal.toString");
        try {
            JAXBContext context = JAXBContext.newInstance(ClientAppeal.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public AppealStatus getStatus() {
        LOG.debug("Executing ClientAppeal.getStatus");
        return status;
    }

}