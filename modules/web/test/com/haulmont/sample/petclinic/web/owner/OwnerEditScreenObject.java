package com.haulmont.sample.petclinic.web.owner;

import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.sample.petclinic.entity.owner.Owner;
import com.haulmont.sample.petclinic.web.owner.owner.OwnerEdit;
import de.diedavids.sneferu.ScreenObject;
import de.diedavids.sneferu.UiTestAPI;
import de.diedavids.sneferu.screen.StandardEditorTestAPI;

import static de.diedavids.sneferu.ComponentDescriptors.*;
import static de.diedavids.sneferu.Interactions.*;

public class OwnerEditScreenObject implements ScreenObject<StandardEditorTestAPI<OwnerEdit>> {

  private StandardEditorTestAPI<OwnerEdit> delegate;

  static OwnerEditScreenObject newEntity(
      UiTestAPI uiTestAPI
  ) {

    StandardEditorTestAPI<OwnerEdit> standardLookupTestAPI = uiTestAPI.openStandardEditor(
        Owner.class,
        OwnerEdit.class
    );

    return new OwnerEditScreenObject(
        standardLookupTestAPI
    );
  }


  static OwnerEditScreenObject forEntity(
      UiTestAPI uiTestAPI,
      Owner entity
  ) {

    StandardEditorTestAPI<OwnerEdit> standardLookupTestAPI = uiTestAPI.openStandardEditor(
        Owner.class,
        OwnerEdit.class,
        entity
    );

    return new OwnerEditScreenObject(
        standardLookupTestAPI
    );
  }


  private OwnerEditScreenObject(
      StandardEditorTestAPI<OwnerEdit> delegate) {
    this.delegate = delegate;
  }

  @Override
  public StandardEditorTestAPI<OwnerEdit> delegate() {
    return delegate;
  }

  public OperationResult saveOwnerWithDetails(
      String firstName,
      String lastName,
      String address,
      String city
  ) {
    return (OperationResult) delegate
        .interact(enter(textField("firstNameField"), firstName))
        .andThen(enter(textField("lastNameField"), lastName))
        .andThen(enter(textField("addressField"), address))
        .andThen(enter(textField("cityField"), city))
        .andThenGet(closeEditor());
  }

  public boolean addressIsSetTo(String expectedAddress) {
    return delegate.component(textField("addressField"))
        .rawComponent()
        .getValue().equals(expectedAddress);
  }
}
