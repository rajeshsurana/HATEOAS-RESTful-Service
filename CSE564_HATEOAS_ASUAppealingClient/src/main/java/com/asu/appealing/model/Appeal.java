package com.asu.appealing.model;

import com.asu.appealing.representations.Representation;
import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Appeal {
    
    private static final Logger LOG = LoggerFactory.getLogger(Appeal.class);
    
    @XmlElement(namespace = Representation.ASU_NAMESPACE)
    private final int studentId;
    @XmlElement(namespace = Representation.ASU_NAMESPACE)
    private final int assignmentId;
    @XmlElement(namespace = Representation.ASU_NAMESPACE)
    private String description;
    @XmlElement(namespace = Representation.ASU_NAMESPACE)
    private AppealStatus status = AppealStatus.DRAFT;

    public Appeal(int studentId, int assignmentId, String description) {
        this(studentId, assignmentId, description, AppealStatus.DRAFT);
        LOG.debug("Executing Appeal constructor: student id = {}, assignment id = {} and description = {}", studentId, assignmentId, description);
    }
    

    public Appeal(int studentId, int assignmentId, String description, AppealStatus status) {
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.description = description;
        this.status = status;
        LOG.debug("Executing Appeal constructor: student id = {}, assignment id = {}, description = {} and status = {}", studentId, assignmentId, description, status);
        LOG.debug("appeal = {}", this);
    }

    public int getStudentId() {
        LOG.debug("Executing Appeal.getStudentId");
        LOG.debug("student id = {}", studentId);
        return studentId;
    }
    
    public int getAssignmentId() {
        LOG.debug("Executing Appeal.getAssignmentId");
        LOG.debug("assignment id = {}", assignmentId);
        return assignmentId;
    }
    
    public void setDescription(String description) {
        LOG.debug("Executing Appeal.setDescription");
        this.description = description;
    }
    
    public String getDescription() {
        LOG.debug("Executing Appeal.getDescription");
        LOG.debug("description = {}", description);
        return description;
    }

    public void setStatus(AppealStatus status) {
        LOG.debug("Executing Appeal.setStatus");
        this.status = status;
    }

    public AppealStatus getStatus() {
        LOG.debug("Executing Appeal.getStatus");
        LOG.debug("status = {}", status);
        return status;
    }
    
    @Override
    public String toString() {
        LOG.debug("Executing Appeal.toString");
        StringBuilder sb = new StringBuilder();
        sb.append("Student Id: " + studentId + "\n");
        sb.append("Assignment Id: " + assignmentId + "\n");
        sb.append("Description Id: " + description + "\n");
        sb.append("Status: " + status + "\n");
        
        return sb.toString();
    }
}