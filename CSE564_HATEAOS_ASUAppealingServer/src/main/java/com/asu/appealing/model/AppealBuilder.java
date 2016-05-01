package com.asu.appealing.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppealBuilder {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealBuilder.class);
    
    public static AppealBuilder appeal() {
        return new AppealBuilder();
    }

    private static int count = 0;
    private final int studentId = 12345430;
    private final int assignmentId = 1;
    private String description = "I have already written 'Fixed end point' in point 4 (highlighed on paper)";
    private AppealStatus status = AppealStatus.DRAFT;
    
    public Appeal build() {
        LOG.debug("Executing AppealBuilder.build");
        return new Appeal(studentId+(count++), assignmentId, description, status);
    }

    public AppealBuilder withDescription(String description) {
        LOG.debug("Executing AppealBuilder.withDescription");
        this.description = description;
        return this;
    }

 
    public AppealBuilder withStatus(AppealStatus status) {
        LOG.debug("Executing AppealBuilder.withStatus");
        this.status = status;
        return this;
    }

}
