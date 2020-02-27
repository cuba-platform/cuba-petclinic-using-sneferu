package com.haulmont.sample.petclinic.web.owner

import com.haulmont.sample.petclinic.entity.owner.Owner
import com.haulmont.sample.petclinic.web.PetclinicWebIntegrationSpec


import static de.diedavids.sneferu.ComponentDescriptors.*;
import static de.diedavids.sneferu.Interactions.*;

class EditOwnerSpec extends PetclinicWebIntegrationSpec {

  private Owner existingOwner

  def setup() {
    existingOwner = metadata.create(Owner)
  }

  def "the address attribute of the Owner entity is correctly bound to the Address Field"() {

    given:
    existingOwner.address = "Miastreet 134"

    and:
    def ownerEdit = OwnerEditScreenObject.forEntity(uiTestAPI, existingOwner)

    expect:
    ownerEdit.addressIsSetTo("Miastreet 134")
  }

  def "the city attribute is correctly bound to the city field"() {

    given:
    existingOwner.city = "Alabastia"

    and:
    def ownerEdit = OwnerEditScreenObject.forEntity(uiTestAPI, existingOwner)

    expect:
    def city = ownerEdit.delegate()
                        .verify(componentValue(textField("cityField")))

    city == "Alabastia"
  }


}