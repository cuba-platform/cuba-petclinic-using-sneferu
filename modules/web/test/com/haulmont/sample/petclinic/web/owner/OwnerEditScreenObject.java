package com.haulmont.sample.petclinic.web.owner;

import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.sample.petclinic.entity.owner.Owner;
import com.haulmont.sample.petclinic.web.owner.owner.OwnerEdit;
import com.haulmont.sneferu.ScreenObject;
import com.haulmont.sneferu.UiTestAPI;
import com.haulmont.sneferu.screen.StandardEditorTestAPI;

import static com.haulmont.sneferu.ComponentDescriptors.*;
import static com.haulmont.sneferu.Interactions.*;

public class OwnerEditScreenObject implements ScreenObject<StandardEditorTestAPI<Owner, OwnerEdit>> {

  private StandardEditorTestAPI<Owner, OwnerEdit> delegate;

  static OwnerEditScreenObject newEntity(
      UiTestAPI uiTestAPI
  ) {

    StandardEditorTestAPI<Owner, OwnerEdit> standardLookupTestAPI = uiTestAPI.openStandardEditor(
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

    StandardEditorTestAPI<Owner, OwnerEdit> standardLookupTestAPI = uiTestAPI.openStandardEditor(
        Owner.class,
        OwnerEdit.class,
        entity
    );

    return new OwnerEditScreenObject(
        standardLookupTestAPI
    );
  }


  private OwnerEditScreenObject(
      StandardEditorTestAPI<Owner, OwnerEdit> delegate) {
    this.delegate = delegate;
  }

  @Override
  public StandardEditorTestAPI<Owner, OwnerEdit> delegate() {
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
