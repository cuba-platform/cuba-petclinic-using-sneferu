package com.haulmont.sample.petclinic.web.visit

import com.haulmont.cuba.gui.Screens.LaunchMode
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import com.haulmont.sample.petclinic.entity.pet.Pet
import com.haulmont.sample.petclinic.entity.visit.Visit
import com.haulmont.sample.petclinic.service.VisitService
import com.haulmont.sample.petclinic.web.PetclinicWebIntegrationSpec
import com.haulmont.sample.petclinic.web.visit.visit.CreateVisitForPet
import com.haulmont.sample.petclinic.web.visit.visit.VisitBrowse
import com.haulmont.sample.petclinic.web.visit.visit.VisitEdit

import static com.haulmont.sneferu.ComponentDescriptors.button
import static com.haulmont.sneferu.ComponentDescriptors.textField
import static com.haulmont.sneferu.Interactions.click
import static com.haulmont.sneferu.Interactions.enter
import static com.haulmont.sneferu.Interactions.screenOpenMode

class CreateVisitForPetSpec extends PetclinicWebIntegrationSpec {
  private visitService

  def setup() {

    given:
    visitService = Mock(VisitService)
    TestServiceProxy.mock(VisitService, visitService)

    and:
    def visitBrowse = uiTestAPI
        .openStandardLookup(Visit, VisitBrowse)

    and:
    visitBrowse
        .interact(click(button("createForPetBtn")))

  }

  def "the Create Visit for Pet screen is opened as a DIALOG"() {

    given:
    def createVisitForPetScreen = uiTestAPI
        .getOpenedScreen(CreateVisitForPet)

    expect:
    createVisitForPetScreen.verify(screenOpenMode()) == OpenMode.DIALOG
  }

  def "a new visit can be created by entering a Pet Identification Number"() {

    given:
    def visit = new Visit(
        pet: new Pet()
    )
    visitService.createVisitForToday("123") >> visit

    and:
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

  def "no visit is created in case the Identification Number is invalid"() {

    given: 'the ID 123 returns no Visit'
    visitService.createVisitForToday("123") >> null

    and:
    def createVisitForPetScreen = uiTestAPI
        .getOpenedScreen(CreateVisitForPet)

    when:
    createVisitForPetScreen
        .interact(enter(textField("identificationNumber"), "123"))
        .interact(click(button("createVisitBtn")))


    then: 'the create screen is still shown and displaying an error message'
    uiTestAPI.isActive(createVisitForPetScreen)
  }
}
