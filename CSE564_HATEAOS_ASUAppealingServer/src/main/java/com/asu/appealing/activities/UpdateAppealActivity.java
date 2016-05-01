package com.asu.appealing.activities;

import com.asu.appealing.model.Appeal;
import com.asu.appealing.model.AppealStatus;
import com.asu.appealing.repositories.AppealRepository;
import com.asu.appealing.representations.AppealRepresentation;
import com.asu.appealing.representations.AsuUri;

public class UpdateAppealActivity {
    public AppealRepresentation update(Appeal appeal, AsuUri appealUri) {
        String appealIdentifier = appealUri.getId();

        AppealRepository repository = AppealRepository.current();
        if (AppealRepository.current().appealNotPlaced(appealIdentifier)) { // Defensive check to see if we have the appeal
            throw new NoSuchAppealException();
        }

        if (!appealCanBeChanged(appealIdentifier, appeal.getStatus())) {
            throw new UpdateException();
        }

        Appeal storedAppeal = repository.get(appealIdentifier);
        AppealStatus newStatus = appeal.getStatus();
        if(newStatus != AppealStatus.FOLLOWUP){
            //System.out.println("RAJESH -- inside if");
            storedAppeal.setStatus(newStatus);
            String description = appeal.getDescription();
            if(description != null && !"".equals(description.trim())){
                storedAppeal.setDescription(description);
            }
        }
        //repository.store(storedAppeal);
        return AppealRepresentation.createResponseAppealRepresentation(storedAppeal, appealUri); 
    }
    
    private boolean appealCanBeChanged(String identifier, AppealStatus newStatus) {
        AppealStatus status = AppealRepository.current().get(identifier).getStatus();
        if(status == AppealStatus.DRAFT && newStatus == AppealStatus.DRAFT 
           || status == AppealStatus.DRAFT && newStatus == AppealStatus.SUBMITTED  
           || status == AppealStatus.SUBMITTED && newStatus == AppealStatus.APPROVED
           || status == AppealStatus.SUBMITTED && newStatus == AppealStatus.REJECTED
           || status == AppealStatus.SUBMITTED && newStatus == AppealStatus.FOLLOWUP)
            return true;
        else{
            return false;
        }
    }
}
