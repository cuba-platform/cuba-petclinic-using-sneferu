package com.haulmont.sample.petclinic.web.pet.pet;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogOutcome;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog.InputDialogResult;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.pet.PetType;
import com.haulmont.sample.petclinic.service.DiseaseWarningMailingService;
import com.haulmont.sample.petclinic.service.VisitService;
import javax.inject.Inject;

@UiController("petclinic_Pet.browse")
@UiDescriptor("pet-browse.xml")
@LookupComponent("petsTable")
@LoadDataBeforeShow
public class PetBrowse extends StandardLookup<Pet> {

  @Inject
  protected Metadata metadata;
  @Inject
  protected GroupTable<Pet> petsTable;
  @Inject
  protected VisitService visitService;
  @Inject
  protected Dialogs dialogs;
  @Inject
  protected DiseaseWarningMailingService diseaseWarningMailingService;
  @Inject
  protected Notifications notifications;
  @Inject
  protected MessageBundle messageBundle;
  @Inject
  private Screens screens;

  @Subscribe("petsTable.createDiseaseWarningMailing")
  public void createDiseaseWarningMailing(ActionPerformedEvent actionPerformedEvent) {
    screens.create(CreateDiseaseWarningMailing.class, OpenMode.DIALOG).show();
  }


  @Subscribe("petsTable.calculateDiscount")
  public void calculateDiscount(ActionPerformedEvent actionPerformedEvent) {

    Pet pet = petsTable.getSingleSelected();

    int discount = calculateDiscount(pet);

    showDiscountCalculatedNotification(pet, discount);
  }


  private void showDiscountCalculatedNotification(Pet pet, int discount) {

    String petName = metadata.getTools().getInstanceName(pet);

    String discountMessage = "Discount for " + petName + ": " + discount + "%";

    dialogs
        .createMessageDialog()
        .withMessage(discountMessage)
        .show();
  }

  private int calculateDiscount(Pet pet) {
    int discount = 0;

    int visitAmount = visitService.countVisitsForPet(pet);
    if (visitAmount > 10) {
      discount = 10;
    } else if (visitAmount > 5) {
      discount = 5;
    }
    return discount;
  }

  @Subscribe("petsTable.greet")
  protected void onPetsTableGreet(ActionPerformedEvent event) {

    dialogs
        .createMessageDialog()
        .withMessage("Hello World")
        .show();
  }

  @Install(to = "createDiseaseWarningMailing", subject = "dialogResultHandler")
  protected void createDiseaseWarningMailingDialogResultHandler(
      InputDialogResult inputDialogResult
  ) {
    if (inputDialogResult.closedWith(DialogOutcome.OK)) {
      createDiseaseWarningMailing(
          inputDialogResult.getValue("city"),
          inputDialogResult.getValue("disease"),
          inputDialogResult.getValue("petType")
      );
    }
  }

  private void createDiseaseWarningMailing(
      String city,
      String disease,
      PetType petType
  ) {

    diseaseWarningMailingService.warnAboutDisease(
        petType,
        disease,
        city
    );

    notifications.create(NotificationType.TRAY)
        .withCaption(
            messageBundle.formatMessage("warningMailingSendForAllOwnersIn", city)
        )
        .show();
  }

  @Subscribe("petsTable.createDiseaseWarningMailingInputDialog")
  protected void onPetsTableCreateDiseaseWarningMailingInputDialog(ActionPerformedEvent event) {

    dialogs.createInputDialog(this)
        .withCaption(messageBundle.getMessage("createDiseaseWarningMailing"))
        .withParameters(
            InputParameter.stringParameter("city")
            .withRequired(true)
            .withCaption(messageBundle.getMessage("city")),

            InputParameter.stringParameter("disease")
            .withRequired(true)
            .withCaption(messageBundle.getMessage("disease")),

            InputParameter.entityParameter("petType", PetType.class)
            .withRequired(true)
            .withCaption(messageBundle.getMessage("petType"))

        )
        .withCloseListener(inputDialogCloseEvent -> {
          if (inputDialogCloseEvent.closedWith(DialogOutcome.OK)) {
            createDiseaseWarningMailing(
                inputDialogCloseEvent.getValue("city"),
                inputDialogCloseEvent.getValue("disease"),
                inputDialogCloseEvent.getValue("petType")
            );
          }
        })
        .show();
  }
}