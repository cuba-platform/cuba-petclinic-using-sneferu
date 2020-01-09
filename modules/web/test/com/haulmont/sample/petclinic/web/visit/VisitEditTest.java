package com.haulmont.sample.petclinic.web.visit;

import static com.haulmont.sneferu.ComponentDescriptors.*;
import static com.haulmont.sneferu.Interactions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import com.haulmont.sample.petclinic.web.visit.visit.VisitBrowse;
import com.haulmont.sample.petclinic.web.visit.visit.VisitEdit;
import com.haulmont.sneferu.CubaWebUiTestAPI;
import com.haulmont.sneferu.UiTestAPI;
import com.haulmont.sneferu.screen.StandardEditorTestAPI;
import com.haulmont.sneferu.screen.StandardLookupTestAPI;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class VisitEditTest {

  @RegisterExtension
  public TestUiEnvironment environment =
      new TestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
          .withScreenPackages(
              "com.haulmont.cuba.web.app.main",
              "com.haulmont.sample.petclinic.web"
          )
          .withUserLogin("admin");



  private UiTestAPI uiTestAPI;

  private StandardLookupTestAPI<VisitBrowse> visitBrowse;
  private StandardEditorTestAPI<VisitEdit> visitEdit;

  private Pet pikachu;

  @BeforeEach
  public void setupEnvironment() {
    uiTestAPI = new CubaWebUiTestAPI(
        environment,
        AppBeans.get(ScreenBuilders.class),
        MainScreen.class
    );

    visitBrowse = uiTestAPI.openStandardLookup(Visit.class, VisitBrowse.class);

    visitBrowse.interact(click(button("createBtn")));

    visitEdit = uiTestAPI.getOpenedEditorScreen(VisitEdit.class);


    Metadata metadata = AppBeans.get(Metadata.class);
    pikachu = metadata.create(Pet.class);
    pikachu.setName("Pikachu");

  }

  @Test
  public void aVisitCanBeCreated_whenAllFieldsAreFilled() {

    // when:
    OperationResult outcome = (OperationResult) visitEdit
        .interact(enter(dateField("visitDateField"), new Date()))
        .interact(enter(textField("descriptionField"), "Regular Visit"))
        .interact(select(lookupField("petField"), pikachu))
        .andThenGet(closeEditor());

    // then:
    assertThat(outcome).isEqualTo(OperationResult.success());

    // and:
    assertThat(uiTestAPI.isActive(visitEdit))
      .isFalse();
  }

  @Test
  public void aVisitCannotBeCreated_whenVisitDateIsMissing() {

    // when:
    visitEdit
        .interact(enter(dateField("visitDateField"), null));

    // and:
    OperationResult outcome = (OperationResult) visitEdit
        .interact(enter(textField("descriptionField"), "Regular Visit"))
        .interact(select(lookupField("petField"), pikachu))
        .andThenGet(closeEditor());

    // then:
    assertThat(outcome).isEqualTo(OperationResult.fail());

    // and:
    assertThat(visitEdit.screen().validateScreen().getAll())
      .hasSize(1);
  }

  @AfterEach
  public void closeAllScreens() {
      uiTestAPI.closeAllScreens();
      TestServiceProxy.clear();
  }
}
