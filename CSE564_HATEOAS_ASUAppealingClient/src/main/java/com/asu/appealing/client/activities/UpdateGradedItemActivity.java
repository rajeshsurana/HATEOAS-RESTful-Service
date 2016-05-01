package com.asu.appealing.client.activities;

import com.asu.appealing.client.ClientGradedItem;
import com.asu.appealing.model.GradedItem;
import com.asu.appealing.representations.GradedItemRepresentation;
import java.net.URI;


public class UpdateGradedItemActivity extends Activity {

    private final URI updateUri;
    private GradedItemRepresentation updatedGradedItemRepresentation;

    public UpdateGradedItemActivity(URI updateUri) {
        this.updateUri = updateUri;
    }

    public void updateGradedItem(GradedItem gradedItem) {
        try {
            updatedGradedItemRepresentation = binding.updateGradedItem(gradedItem, updateUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromGradedItemRepresentation(updatedGradedItemRepresentation);
        } catch (MalformedGradedItemException e) {
            actions = retryCurrentActivity();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } 
    }
    
    public ClientGradedItem getGradedItem() {
        return new ClientGradedItem(updatedGradedItemRepresentation.getGradedItem());
    }
}
