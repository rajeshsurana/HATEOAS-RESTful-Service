package com.asu.appealing.client.activities;

import com.asu.appealing.representations.AppealRepresentation;
import com.asu.appealing.representations.GradedItemRepresentation;

class RepresentationHypermediaProcessor {

    Actions extractNextActionsFromAppealRepresentation(AppealRepresentation representation) {
        Actions actions = new Actions();

        if (representation != null) {

            if (representation.getUpdateLink() != null) {
                actions.add(new UpdateAppealActivity(representation.getUpdateLink().getUri()));
            }

            if (representation.getSelfLink() != null) {
                actions.add(new ReadAppealActivity(representation.getSelfLink().getUri()));
            }

            if (representation.getCancelLink() != null) {
                actions.add(new CancelAppealActivity(representation.getCancelLink().getUri()));
            }
        }

        return actions;
    }
    
    Actions extractNextActionsFromGradedItemRepresentation(GradedItemRepresentation representation) {
        Actions actions = new Actions();

        if (representation != null) {

            if (representation.getUpdateLink() != null) {
                actions.add(new UpdateGradedItemActivity(representation.getUpdateLink().getUri()));
            }

            if (representation.getSelfLink() != null) {
                actions.add(new ReadGradedItemActivity(representation.getSelfLink().getUri()));
            }

        }

        return actions;
    }

}
