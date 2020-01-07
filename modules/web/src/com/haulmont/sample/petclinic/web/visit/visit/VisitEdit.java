package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.visit.Visit;

@UiController("petclinic_Visit.edit")
@UiDescriptor("visit-edit.xml")
@EditedEntityContainer("visitDc")
@LoadDataBeforeShow
public class VisitEdit extends StandardEditor<Visit> {

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