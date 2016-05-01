package com.asu.appealing.activities;

import com.asu.appealing.model.GradedItem;
import com.asu.appealing.repositories.GradedItemRepository;
import com.asu.appealing.representations.AsuUri;
import com.asu.appealing.representations.GradedItemRepresentation;

public class ReadGradedItemActivity {
    public GradedItemRepresentation retrieveByUri(AsuUri gradedItemUri) {
        String identifier  = gradedItemUri.getId();
        
        GradedItem gradedItem = GradedItemRepository.current().get(identifier);
        
        if(gradedItem == null) {
            throw new NoSuchGradedItemException();
        }
        
        return GradedItemRepresentation.createResponseGradedItemRepresentation(gradedItem, gradedItemUri);
    }
}
