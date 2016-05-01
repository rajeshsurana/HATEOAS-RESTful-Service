package com.asu.appealing.client.activities;

import com.asu.appealing.client.ClientAppeal;
import com.asu.appealing.model.Appeal;
import com.asu.appealing.representations.AppealRepresentation;
import java.net.URI;


public class UpdateAppealActivity extends Activity {

    private final URI updateUri;
    private AppealRepresentation updatedAppealRepresentation;

    public UpdateAppealActivity(URI updateUri) {
        this.updateUri = updateUri;
    }

    public void updateAppeal(Appeal appeal) {
        try {
            updatedAppealRepresentation = binding.updateAppeal(appeal, updateUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromAppealRepresentation(updatedAppealRepresentation);
        } catch (MalformedAppealException e) {
            actions = retryCurrentActivity();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } catch (CannotUpdateAppealException e) {
            actions = noFurtherActivities();
        }
    }
    
    public ClientAppeal getAppeal() {
        return new ClientAppeal(updatedAppealRepresentation.getAppeal());
    }
}
