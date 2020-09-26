package com.haulmont.sample.petclinic.web.pet

import com.haulmont.cuba.gui.Screens
import com.haulmont.cuba.gui.Screens.OpenedScreens
import com.haulmont.cuba.gui.screen.Screen
import com.haulmont.sample.petclinic.entity.pet.Pet
import com.haulmont.sample.petclinic.web.PetclinicWebIntegrationSpec
import com.haulmont.sample.petclinic.web.pet.pet.PetBrowse
import de.diedavids.sneferu.screen.StandardLookupTestAPI

import static de.diedavids.sneferu.ComponentDescriptors.button
import static de.diedavids.sneferu.Interactions.click

class PetBrowseSpec extends PetclinicWebIntegrationSpec {

  StandardLookupTestAPI<Pet, PetBrowse> petBrowse


  def "the Discount for a Pet can be displayed via the Pet Browse Screen"() {

    given:
    petBrowse = uiTestAPI.openStandardLookup(Pet, PetBrowse)

    when:
    petBrowse.interact(click(button("greetBtn")))

    def screens = environment.screens
    def openedScreens = screens.openedScreens

    def all = openedScreens.all
    def browseScreen = environment.screens.openedScreens.all[0]
    def window = browseScreen.window
    then:
    [all].size() == 2
  }

}