package com.haulmont.sample.petclinic.web

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.gui.ScreenBuilders
import com.haulmont.cuba.web.app.main.MainScreen
import com.haulmont.cuba.web.testsupport.TestUiEnvironment
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import com.haulmont.sneferu.CubaWebUiTestAPI
import com.haulmont.sneferu.UiTestAPI
import com.haulmont.sneferu.environment.SneferuTestUiEnvironment
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class PetclinicWebIntegrationSpec extends Specification {


  @Shared @ClassRule
  SneferuTestUiEnvironment environment =
      new SneferuTestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
          .withScreenPackages(
              "com.haulmont.cuba.web.app.main",
              "com.haulmont.sample.petclinic.web"
          )
          .withUserLogin("admin")
          .withMainScreen(MainScreen)

  UiTestAPI uiTestAPI

  Metadata metadata

  def setup() {
    uiTestAPI = environment.getUiTestAPI()

    metadata = AppBeans.get(Metadata)
  }


  void cleanup() {
    uiTestAPI.closeAllScreens()
    TestServiceProxy.clear()
  }


}
