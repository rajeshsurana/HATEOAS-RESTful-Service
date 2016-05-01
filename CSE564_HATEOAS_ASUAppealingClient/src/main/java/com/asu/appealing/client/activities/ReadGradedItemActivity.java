package com.asu.appealing.client.activities;

import com.asu.appealing.client.ClientGradedItem;
import com.asu.appealing.representations.GradedItemRepresentation;
import java.net.URI;


public class ReadGradedItemActivity extends Activity {

    private final URI gradedItemUri;
    private GradedItemRepresentation currentGradedItemRepresentation;

    public ReadGradedItemActivity(URI gradedItemUri) {
        this.gradedItemUri = gradedItemUri;
    }

    public void readGradedItem() {
        try {
            currentGradedItemRepresentation = binding.retrieveGradedItem(gradedItemUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromGradedItemRepresentation(currentGradedItemRepresentation);
        } catch (NotFoundException e) {
            actions = new Actions();
            actions.add(new PlaceGradedItemActivity());
        } catch (ServiceFailureException e) {
            actions = new Actions();
            actions.add(this);
        }
    }

    public ClientGradedItem getGradedItem() {
        return new ClientGradedItem(currentGradedItemRepresentation.getGradedItem());
    }
}
