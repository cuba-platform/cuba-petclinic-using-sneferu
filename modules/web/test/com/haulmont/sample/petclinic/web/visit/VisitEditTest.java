package com.haulmont.sample.petclinic.web.visit;

import static com.haulmont.sneferu.ComponentDescriptors.button;
import static com.haulmont.sneferu.ComponentDescriptors.dateField;
import static com.haulmont.sneferu.ComponentDescriptors.lookupField;
import static com.haulmont.sneferu.ComponentDescriptors.textField;
import static com.haulmont.sneferu.Interactions.click;
import static com.haulmont.sneferu.Interactions.closeEditor;
import static com.haulmont.sneferu.Interactions.enter;
import static com.haulmont.sneferu.Interactions.select;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import com.haulmont.sample.petclinic.web.visit.visit.VisitBrowse;
import com.haulmont.sample.petclinic.web.visit.visit.VisitEdit;
import com.haulmont.sneferu.environment.SneferuTestUiEnvironment;
import com.haulmont.sneferu.environment.StartScreen;
import com.haulmont.sneferu.screen.StandardEditorTestAPI;
import com.haulmont.sneferu.screen.StandardLookupTestAPI;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class VisitEditTest {

  @RegisterExtension
  public SneferuTestUiEnvironment environment =
      new SneferuTestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
          .withScreenPackages(
              "com.haulmont.cuba.web.app.main",
              "com.haulmont.sample.petclinic.web"
          )
          .withUserLogin("admin")
          .withMainScreen(MainScreen.class);


  private Pet pikachu;

  @BeforeEach
  public void setupEnvironment() {

    StandardLookupTestAPI<Visit, VisitBrowse> visitBrowse = environment.getUiTestAPI()
        .openStandardLookup(Visit.class, VisitBrowse.class);

    visitBrowse.interact(click(button("createBtn")));

    Metadata metadata = AppBeans.get(Metadata.class);
    pikachu = metadata.create(Pet.class);
    pikachu.setName("Pikachu");

  }

  @Test
  public void aVisitCanBeCreated_whenAllFieldsAreFilled(
      @StartScreen StandardEditorTestAPI<Visit, VisitEdit> visitEdit
  ) {

    // when:
    OperationResult outcome = (OperationResult) visitEdit
        .interact(enter(dateField("visitDateField"), new Date()))
        .interact(enter(textField("descriptionField"), "Regular Visit"))
        .interact(select(lookupField("petField"), pikachu))
        .andThenGet(closeEditor());

    // then:
    assertThat(outcome).isEqualTo(OperationResult.success());

  }

  @Test
  public void aVisitCannotBeCreated_whenVisitDateIsMissing(
      @StartScreen StandardEditorTestAPI<Visit, VisitEdit> visitEdit
  ) {

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
    environment.getUiTestAPI().closeAllScreens();
    TestServiceProxy.clear();
  }
}
