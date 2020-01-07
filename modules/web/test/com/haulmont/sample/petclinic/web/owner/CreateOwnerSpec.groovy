package com.haulmont.sample.petclinic.web.owner

import com.haulmont.cuba.gui.util.OperationResult
import com.haulmont.sample.petclinic.web.PetclinicWebIntegrationSpec

import java.rmi.server.Operation

class CreateOwnerSpec extends PetclinicWebIntegrationSpec {

  def "an Owner can be created when all attributes are provided in the Editor"() {

    given:
    def ownerEdit = OwnerEditScreenObject.newEntity(uiTestAPI)

    when:
    OperationResult operationResult = ownerEdit
        .saveOwnerWithDetails(
            "Ash",
            "Ketchum",
            "Miastreet 134",
            "Alabastia")

    then:
    operationResult == OperationResult.success()
  }

  def "an Owner cannot be created when first name is missing"() {

    given:
    def ownerEdit = OwnerEditScreenObject.newEntity(uiTestAPI)

    when:
    OperationResult operationResult = ownerEdit
        .saveOwnerWithDetails(
            null,
            "Ketchum",
            "Miastreet 134",
            "Alabastia")

    then:
    operationResult == OperationResult.fail()
  }
}