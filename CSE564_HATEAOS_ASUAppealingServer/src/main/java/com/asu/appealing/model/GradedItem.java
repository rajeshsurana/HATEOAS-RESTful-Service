package com.asu.appealing.model;

import com.asu.appealing.representations.Representation;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class GradedItem {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradedItem.class);
    
    @XmlElement(namespace = Representation.ASU_NAMESPACE)
    private final int studentId;
    @XmlElement(namespace = Representation.ASU_NAMESPACE)
    private final int assignmentId;
    @XmlElement(namespace = Representation.ASU_NAMESPACE)
    private int score;
    @XmlElement(namespace = Representation.ASU_NAMESPACE)
    private String comment;


    public GradedItem(int studentId, int assignmentId, int score, String comment) {
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.score = score;
        this.comment = comment;
        LOG.debug("Executing GradedItem constructor: student id = {}, assignment id = {}, score = {} and comment = {}", studentId, assignmentId, score, comment);
        LOG.debug("graded item = {}", this);
    }

    public int getStudentId() {
        LOG.debug("Executing GradedItem.getStudentId");
        LOG.debug("student id = {}", studentId);
        return studentId;
    }
    
    public int getAssignmentId() {
        LOG.debug("Executing GradedItem.getAssignmentId");
        LOG.debug("assignment id = {}", assignmentId);
        return assignmentId;
    }
    
    public void setScore(int score) {
        LOG.debug("Executing GradedItem.setScore");
        this.score = score;
    }
    
    public int getScore() {
        LOG.debug("Executing GradedItem.getScore");
        LOG.debug("score = {}", score);
        return score;
    }
    
    public void setComment(String comment) {
        LOG.debug("Executing GradedItem.setComment");
        this.comment = comment;
    }
    
    public String getComment() {
        LOG.debug("Executing GradedItem.getComment");
        LOG.debug("comment = {}", comment);
        return comment;
    }
    
    @Override
    public String toString() {
        LOG.debug("Executing GradedItem.toString");
        StringBuilder sb = new StringBuilder();
        sb.append("Student Id: " + studentId + "\n");
        sb.append("Assignment Id: " + assignmentId + "\n");
        sb.append("Score: " + score + "\n");
        sb.append("Comment: " + comment + "\n");
        return sb.toString();
    }
}