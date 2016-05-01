package com.asu.appealing.activities;

import com.asu.appealing.model.Appeal;
import com.asu.appealing.model.AppealStatus;
import com.asu.appealing.repositories.AppealRepository;
import com.asu.appealing.representations.AppealRepresentation;
import com.asu.appealing.representations.AsuUri;
import com.asu.appealing.representations.Link;
import com.asu.appealing.representations.Representation;

public class CreateAppealActivity {
    public AppealRepresentation create(Appeal appeal, AsuUri requestUri) {
        appeal.setStatus(AppealStatus.DRAFT);
                
        String identifier = AppealRepository.current().store(appeal);
        
        AsuUri appealUri = new AsuUri(requestUri.getBaseUri() + "/appeal/" + identifier);
        return new AppealRepresentation(appeal, 
                new Link(Representation.RELATIONS_URI + "cancel", appealUri),
                new Link(Representation.RELATIONS_URI + "submit", appealUri), 
                new Link(Representation.RELATIONS_URI + "update", appealUri),
                new Link(Representation.SELF_REL_VALUE, appealUri));
    }
}
