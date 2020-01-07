package com.haulmont.sample.petclinic.web.pet

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.gui.ScreenBuilders
import com.haulmont.cuba.gui.util.OperationResult
import com.haulmont.cuba.web.app.main.MainScreen
import com.haulmont.cuba.web.testsupport.TestUiEnvironment
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import com.haulmont.sample.petclinic.entity.pet.Pet
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer
import com.haulmont.sample.petclinic.web.pet.pet.PetBrowse
import com.haulmont.sample.petclinic.web.pet.pet.PetEdit
import de.diedavids.sneferu.CubaWebUiTestAPI
import de.diedavids.sneferu.UiTestAPI
import de.diedavids.sneferu.interactions.SetValueInteraction
import de.diedavids.sneferu.screen.StandardEditorTestAPI
import de.diedavids.sneferu.screen.StandardLookupTestAPI
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import static de.diedavids.sneferu.ComponentDescriptors.button
import static de.diedavids.sneferu.ComponentDescriptors.textField
import static de.diedavids.sneferu.Interactions.click
import static de.diedavids.sneferu.Interactions.closeEditor
import static de.diedavids.sneferu.Interactions.enter

class CreatePetSpec extends Specification {

  @Shared @ClassRule
  TestUiEnvironment environment =
      new TestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
          .withScreenPackages(
              "com.haulmont.cuba.web.app.main",
              "com.haulmont.sample.petclinic.web.pet.pet"
          )
          .withUserLogin("admin")

  UiTestAPI uiTestAPI

  StandardLookupTestAPI<PetBrowse> petBrowse
  StandardEditorTestAPI<PetEdit> petEdit

  def setup() {
    uiTestAPI = new CubaWebUiTestAPI(
        environment,
        AppBeans.get(ScreenBuilders.class),
        MainScreen
    )

    petBrowse = uiTestAPI.openStandardLookup(Pet, PetBrowse)

    petBrowse.interact(click(button("createBtn")))

    petEdit = uiTestAPI.getOpenedEditorScreen(PetEdit)

  }

  void cleanup() {
    uiTestAPI.closeAllScreens()
    TestServiceProxy.clear()
  }

  def "a Pet can be created with a valid Name and Identification Number"() {

    when:
    OperationResult outcome = petEdit
                                       .interact(setNameTo("Hoopa Unbound"))
                                       .andThen(setIdentificationNumberTo("720"))
                                       .andThenGet(closeEditor())

    then:
    outcome == OperationResult.success()

    and:
    uiTestAPI.isActive(petBrowse)
  }

  protected SetValueInteraction setIdentificationNumberTo(String identificationNumber) {
    enter(textField("identificationNumberField"), identificationNumber)
  }

  protected SetValueInteraction setNameTo(String name) {
    enter(textField("nameField"), name)
  }

  def "a Pet cannot be created without an Identification Number"() {

    when:
    OperationResult outcome = petEdit
        .interact(setNameTo("Hoopa Unbound"))
        .andThen(setIdentificationNumberTo(null))
        .andThenGet(closeEditor())

    then:
    outcome == OperationResult.fail()

    and:
    uiTestAPI.isActive(petEdit)

    and:
    def validationErrors = petEdit.screen().validateScreen().getAll()

    and:
    validationErrors.size() == 1
    validationErrors[0].description == "Identification Number required"
  }

}