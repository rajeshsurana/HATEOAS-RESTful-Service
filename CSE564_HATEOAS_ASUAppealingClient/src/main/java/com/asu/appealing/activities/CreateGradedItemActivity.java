package com.asu.appealing.activities;

import com.asu.appealing.model.GradedItem;
import com.asu.appealing.repositories.GradedItemRepository;
import com.asu.appealing.representations.AsuUri;
import com.asu.appealing.representations.GradedItemRepresentation;
import com.asu.appealing.representations.Link;
import com.asu.appealing.representations.Representation;

public class CreateGradedItemActivity {
    public GradedItemRepresentation create(GradedItem gradedItem, AsuUri requestUri) {
                
        String identifier = GradedItemRepository.current().store(gradedItem);
        
        AsuUri gradedItemUri = new AsuUri(requestUri.getBaseUri() + "/gradedItem/" + identifier);
        return new GradedItemRepresentation(gradedItem, 
                new Link(Representation.RELATIONS_URI + "update", gradedItemUri),
                new Link(Representation.SELF_REL_VALUE, gradedItemUri));
    }
}
