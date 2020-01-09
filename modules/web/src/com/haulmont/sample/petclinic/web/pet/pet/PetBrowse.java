package com.haulmont.sample.petclinic.web.pet.pet;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.cuba.gui.screen.*;
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
}