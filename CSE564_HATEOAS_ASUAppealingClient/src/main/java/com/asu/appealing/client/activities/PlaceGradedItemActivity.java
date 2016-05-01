package com.asu.appealing.client.activities;

import com.asu.appealing.client.ClientGradedItem;
import java.net.URI;

import com.asu.appealing.model.GradedItem;
import com.asu.appealing.representations.GradedItemRepresentation;

public class PlaceGradedItemActivity extends Activity {

    private GradedItem gradedItem;

    public void placeGradedItem(GradedItem gradedItem, URI gradedItemUri) {
        
        try {
            GradedItemRepresentation createdGradedItemRepresentation = binding.createGradedItem(gradedItem, gradedItemUri);
            this.actions = new RepresentationHypermediaProcessor().extractNextActionsFromGradedItemRepresentation(createdGradedItemRepresentation);
            this.gradedItem = createdGradedItemRepresentation.getGradedItem();
        } catch (MalformedGradedItemException e) {
            this.actions = retryCurrentActivity();
        } catch (ServiceFailureException e) {
            this.actions = retryCurrentActivity();
        } 
    }
    
    public ClientGradedItem getGradedItem() {
        return new ClientGradedItem(gradedItem);
    }
}
