package com.asu.appealing.activities;

import com.asu.appealing.model.Appeal;
import com.asu.appealing.model.AppealStatus;
import com.asu.appealing.repositories.AppealRepository;
import com.asu.appealing.representations.AppealRepresentation;
import com.asu.appealing.representations.AsuUri;

public class RemoveAppealActivity {
    public AppealRepresentation delete(AsuUri appealUri) {
        // Discover the URI of the appeal that has been cancelled
        
        String identifier = appealUri.getId();

        AppealRepository appealRepository = AppealRepository.current();

        if (appealRepository.appealNotPlaced(identifier)) {
            throw new NoSuchAppealException();
        }

        Appeal appeal = appealRepository.get(identifier);

        // Can't delete a processed or submitted appeal
        if (appeal.getStatus() == AppealStatus.APPROVED 
            || appeal.getStatus() == AppealStatus.REJECTED
            || appeal.getStatus() == AppealStatus.SUBMITTED) {
            throw new AppealDeletionException();
        }

        if(appeal.getStatus() == AppealStatus.DRAFT ) { // A draft appeal is being cancelled/deleted 
            appealRepository.remove(identifier);
        }

        return new AppealRepresentation(appeal);
    }

}
