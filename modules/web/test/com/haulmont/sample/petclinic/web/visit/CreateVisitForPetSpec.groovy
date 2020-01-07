package com.haulmont.sample.petclinic.web.visit

import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import com.haulmont.sample.petclinic.entity.pet.Pet
import com.haulmont.sample.petclinic.entity.visit.Visit
import com.haulmont.sample.petclinic.service.VisitService
import com.haulmont.sample.petclinic.web.PetclinicWebIntegrationSpec
import com.haulmont.sample.petclinic.web.visit.visit.CreateVisitForPet
import com.haulmont.sample.petclinic.web.visit.visit.VisitBrowse
import com.haulmont.sample.petclinic.web.visit.visit.VisitEdit

import static de.diedavids.sneferu.ComponentDescriptors.button
import static de.diedavids.sneferu.ComponentDescriptors.dateField
import static de.diedavids.sneferu.ComponentDescriptors.textField
import static de.diedavids.sneferu.Interactions.click
import static de.diedavids.sneferu.Interactions.enter

class CreateVisitForPetSpec extends PetclinicWebIntegrationSpec {
  private visitService

  def setup() {
    visitService = Mock(VisitService)
    TestServiceProxy.mock(VisitService, visitService)
  }

  def "a new visit can be created by entering a Pet Identification Number"() {

    given:
    def visit = new Visit(
        pet: new Pet()
    )
    visitService.createVisitForToday("123") >> visit

    and:
    def visitBrowse = uiTestAPI
        .openStandardLookup(Visit, VisitBrowse)

    and:
    visitBrowse
        .interact(click(button("createForPetBtn")))

    def createVisitForPetScreen = uiTestAPI
        .getOpenedScreen(CreateVisitForPet)

    when:
    createVisitForPetScreen
        .interact(enter(textField("identificationNumber"), "123"))
        .interact(click(button("createVisitBtn")))


    then:
    def visitEdit = uiTestAPI
        .getOpenedEditorScreen(VisitEdit)

    and:
    uiTestAPI.isActive(visitEdit)

    and:
    visitEdit.screen().getEditedEntity() == visit
  }
}
