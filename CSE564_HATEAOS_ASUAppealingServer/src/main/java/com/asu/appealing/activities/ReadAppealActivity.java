package com.asu.appealing.activities;

import com.asu.appealing.model.Appeal;
import com.asu.appealing.repositories.AppealRepository;
import com.asu.appealing.representations.AppealRepresentation;
import com.asu.appealing.representations.AsuUri;

public class ReadAppealActivity {
    public AppealRepresentation retrieveByUri(AsuUri appealUri) {
        String identifier  = appealUri.getId();
        
        Appeal appeal = AppealRepository.current().get(identifier);
        
        if(appeal == null) {
            throw new NoSuchAppealException();
        }
        
        return AppealRepresentation.createResponseAppealRepresentation(appeal, appealUri);
    }
}
