package com.haulmont.sample.petclinic.web;

import de.diedavids.sneferu.components.descriptor.TextFieldComponentDescriptor;

public class PetclinicComponentDescriptors {




  /**
   * creates a {@link TextFieldComponentDescriptor} instance
   * @param id the id of the component as defined in the screen XMl descriptor
   * @return a TextFieldComponentDescriptor instance
   */
  public static TextFieldComponentDescriptor textField(String id) {
    return new TextFieldComponentDescriptor(id);
  }


}
