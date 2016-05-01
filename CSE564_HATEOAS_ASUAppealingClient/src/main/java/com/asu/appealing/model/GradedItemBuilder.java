package com.asu.appealing.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GradedItemBuilder {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradedItemBuilder.class);
    
    public static GradedItemBuilder gradedItem() {
        return new GradedItemBuilder();
    }

    private static int count = 0;
    private final int studentId = 12345430;
    private final int assignmentId = 1;
    private int score = 50;
    private String comment = "Fixed end point not mentioned";
    
  
    public GradedItem build() {
        LOG.debug("Executing GradedItemBuilder.build");
        return new GradedItem(studentId+(count++), assignmentId, score, comment);
    }

    public GradedItemBuilder withScore(int score) {
        LOG.debug("Executing GradedItemBuilder.withScore");
        this.score = score;
        return this;
    }
    
    public GradedItemBuilder withComment(String comment) {
        LOG.debug("Executing GradedItemBuilder.withComment");
        this.comment = comment;
        return this;
    }

}
