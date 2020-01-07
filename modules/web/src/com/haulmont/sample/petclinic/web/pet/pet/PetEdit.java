package com.haulmont.sample.petclinic.web.pet.pet;

import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.pet.Pet;

@UiController("petclinic_Pet.edit")
@UiDescriptor("pet-edit.xml")
@EditedEntityContainer("petDc")
@LoadDataBeforeShow
public class PetEdit extends StandardEditor<Pet> {


  /**
   * the visibility of the method is extended to public in order to
   * received the ValidationErrors instance in test cases
   * @return the ValidationErrors instance
   */
  @Override
  public ValidationErrors validateScreen() {
    return super.validateScreen();
  }
}