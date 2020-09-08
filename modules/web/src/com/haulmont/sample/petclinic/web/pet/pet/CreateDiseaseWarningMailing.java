package com.haulmont.sample.petclinic.web.pet.pet;

import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.pet.PetType;
import com.haulmont.sample.petclinic.service.DiseaseWarningMailingService;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;

import javax.inject.Inject;

import static java.util.Arrays.*;

@UiController("petclinic_CreateDiseaseWarningMailing")
@UiDescriptor("create-disease-warning-mailing.xml")
public class CreateDiseaseWarningMailing extends Screen {

    @Inject
    protected ScreenValidation screenValidation;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent beforeShowEvent) {
        getScreenData().loadAll();
    }

    @Inject
    protected DiseaseWarningMailingService diseaseWarningMailingService;

    @Inject
    protected TextField<String> city;

    @Inject
    protected TextField<String> disease;

    @Inject
    protected LookupField<PetType> petType;

    @Inject
    protected Notifications notifications;

    @Subscribe("createDiseaseWarningMailing")
    protected void createDiseaseWarningMailing(Action.ActionPerformedEvent event) {

        ValidationErrors validationErrors = validationResult();

        if (!validationErrors.isEmpty()) {
            screenValidation.showValidationErrors(this, validationErrors);
            return;
        }


        int endangeredPets = diseaseWarningMailingService.warnAboutDisease(
                petType.getValue(),
                disease.getValue(),
                city.getValue()
        );


        close(new StandardCloseAction(EditorScreen.WINDOW_COMMIT_AND_CLOSE))
                .then(() ->
                        notifications.create()
                                .withCaption(endangeredPets + " Owner(s) of endangered Pets have been notified")
                                .withType(Notifications.NotificationType.TRAY)
                                .show()
                );
    }

    public ValidationErrors validationResult() {
        return screenValidation.validateUiComponents(
                asList(city, disease, petType)
        );
    }

    @Subscribe("cancel")
    protected void onCancel(Action.ActionPerformedEvent event) {
        close(StandardOutcome.DISCARD);
    }
}