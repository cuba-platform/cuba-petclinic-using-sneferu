package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.service.VisitService;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.StandardCloseAction;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;

@UiController
@UiDescriptor("create-visit-for-pet.xml")
public class CreateVisitForPet extends Screen {

    @Inject
    protected VisitService visitService;

    @Inject
    protected TextField<String> identificationNumber;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Notifications notifications;

    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe(id = "createVisit")
    public void createVisit(Action.ActionPerformedEvent event) {
        Visit visit = createVisitForToday();

        if (visit != null) {
            close(new StandardCloseAction(EditorScreen.WINDOW_COMMIT_AND_CLOSE))
                    .then(() -> showVisitCreatedNotification(visit))
                    .then(() -> openVisitDetails(visit));
        }
        else {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption("No Pet found for Identification Number: " + identificationNumber.getValue())
                    .show();
        }
    }

    private Visit createVisitForToday() {
        String petIdentificationNumber = identificationNumber.getValue();
        return visitService.createVisitForToday(petIdentificationNumber);
    }

    private void openVisitDetails(Visit visit) {
        screenBuilders
                .editor(Visit.class, this)
                .editEntity(visit)
                .withLaunchMode(OpenMode.NEW_TAB)
                .build()
                .show();
    }

    private void showVisitCreatedNotification(Visit visit) {

        String petName = metadata.getTools().getInstanceName(visit.getPet());

        String notificationMessage = "Visit created for " + petName;

        notifications.create()
                .withCaption(notificationMessage)
                .withType(Notifications.NotificationType.TRAY)
                .show();
    }
}