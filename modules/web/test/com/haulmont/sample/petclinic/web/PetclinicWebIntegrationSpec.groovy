package com.haulmont.sample.petclinic.web

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.gui.ScreenBuilders
import com.haulmont.cuba.web.app.main.MainScreen
import com.haulmont.cuba.web.testsupport.TestUiEnvironment
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import de.diedavids.sneferu.CubaWebUiTestAPI
import de.diedavids.sneferu.UiTestAPI
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class PetclinicWebIntegrationSpec extends Specification {


  @Shared @ClassRule
  TestUiEnvironment environment =
      new TestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
          .withScreenPackages(
              "com.haulmont.cuba.web.app.main",
              "com.haulmont.sample.petclinic.web"
          )
          .withUserLogin("admin")

  UiTestAPI uiTestAPI

  Metadata metadata

  def setup() {
    uiTestAPI = new CubaWebUiTestAPI(
        environment,
        AppBeans.get(ScreenBuilders.class),
        MainScreen
    )

    metadata = AppBeans.get( Metadata)
  }


  void cleanup() {
    uiTestAPI.closeAllScreens()
    TestServiceProxy.clear()
  }


}
