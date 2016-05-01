package com.asu.appealing.activities;

import com.asu.appealing.model.Appeal;
import com.asu.appealing.model.AppealStatus;
import com.asu.appealing.repositories.AppealRepository;
import com.asu.appealing.representations.AppealRepresentation;

public class CompleteAppealActivity {

    public AppealRepresentation completeAppeal(String id, AppealStatus status) {
        AppealRepository repository = AppealRepository.current();
        if (repository.has(id)) {
            Appeal appeal = repository.get(id);

            if (appeal.getStatus() == AppealStatus.SUBMITTED) {
                appeal.setStatus(status);
            } else if (appeal.getStatus() == AppealStatus.APPROVED
                    || appeal.getStatus() == AppealStatus.REJECTED) {
                throw new AppealAlreadyProcessedException();
            } else if (appeal.getStatus() == AppealStatus.DRAFT) {
                throw new AppealNotSubmittedException();
            }

            return new AppealRepresentation(appeal);
        } else {
            throw new NoSuchAppealException();
        }
    }
}
