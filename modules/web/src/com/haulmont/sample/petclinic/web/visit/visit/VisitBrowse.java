package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import javax.inject.Inject;

@UiController("petclinic_Visit.browse")
@UiDescriptor("visit-browse.xml")
@LookupComponent("visitsTable")
@LoadDataBeforeShow
public class VisitBrowse extends StandardLookup<Visit> {

  @Inject
  private Screens screens;

  @Subscribe("visitsTable.createForPet")
  public void createForPet(Action.ActionPerformedEvent event) {
    screens.create(CreateVisitForPet.class, OpenMode.DIALOG).show();
  }
}