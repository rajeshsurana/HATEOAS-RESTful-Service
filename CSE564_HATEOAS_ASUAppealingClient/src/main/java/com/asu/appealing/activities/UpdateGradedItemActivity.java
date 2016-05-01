package com.asu.appealing.activities;

import com.asu.appealing.model.GradedItem;
import com.asu.appealing.repositories.GradedItemRepository;
import com.asu.appealing.representations.AsuUri;
import com.asu.appealing.representations.GradedItemRepresentation;

public class UpdateGradedItemActivity {
    public GradedItemRepresentation update(GradedItem gradedItem, AsuUri gradedItemUri) {
        String gradedItemIdentifier = gradedItemUri.getId();

        GradedItemRepository repository = GradedItemRepository.current();
        if (GradedItemRepository.current().gradedItemNotPlaced(gradedItemIdentifier)) { // Defensive check to see if we have the appeal
            throw new NoSuchGradedItemException();
        }

        GradedItem storedGradedItem = repository.get(gradedItemIdentifier);
        storedGradedItem.setScore(gradedItem.getScore());
        String comment = gradedItem.getComment();
        if(comment != null && !"".equals(comment.trim())){
            storedGradedItem.setComment(gradedItem.getComment());
        }

        return GradedItemRepresentation.createResponseGradedItemRepresentation(storedGradedItem, gradedItemUri); 
    }
    
}
