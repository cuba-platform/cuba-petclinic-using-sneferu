package com.haulmont.sample.petclinic.web.owner

import com.haulmont.sample.petclinic.web.PetclinicWebIntegrationSpec

class CreateOwnerSpec extends PetclinicWebIntegrationSpec {

  def "an Owner can be created when all attributes are provided in the Editor"() {

    given:
    def ownerEdit = OwnerEditScreenObject.newEntity(uiTestAPI)

    when:
    ownerEdit
        .enterOwnerDetails(
            "Ash",
            "Ketchum",
            "Miastreet 134",
            "Alabastia")
        .save()

    then:
    ownerEdit.wasSuccessfullyStored()
  }

  def "an Owner cannot be created when first name is missing"() {

    given:
    def ownerEdit = OwnerEditScreenObject.newEntity(uiTestAPI)

    when:
    ownerEdit
        .enterOwnerDetails(
            null,
            "Ketchum",
            "Miastreet 134",
            "Alabastia")
        .save()

    then:
    !ownerEdit.wasSuccessfullyStored()
  }
}