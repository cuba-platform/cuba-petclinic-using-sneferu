package com.haulmont.sample.petclinic.web.visit;

import static com.haulmont.sneferu.ComponentDescriptors.dateField;
import static com.haulmont.sneferu.ComponentDescriptors.lookupField;
import static com.haulmont.sneferu.ComponentDescriptors.textField;
import static com.haulmont.sneferu.Interactions.closeEditor;
import static com.haulmont.sneferu.Interactions.enter;
import static com.haulmont.sneferu.Interactions.select;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import com.haulmont.sneferu.environment.SneferuTestUiEnvironment;
import com.haulmont.sneferu.environment.NewEntity;
import com.haulmont.sneferu.environment.StartScreen;
import com.haulmont.sample.petclinic.web.visit.visit.VisitEdit;
import com.haulmont.sneferu.screen.StandardEditorTestAPI;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class VisitEditTestWithAnnotations {

  @RegisterExtension
  public SneferuTestUiEnvironment environment =
      new SneferuTestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
          .withScreenPackages(
              "com.haulmont.cuba.web.app.main",
              "com.haulmont.sample.petclinic.web"
          )
          .withUserLogin("admin")
          .withMainScreen(MainScreen.class);

  @Test
  public void aVisitCanBeCreated_whenAllFieldsAreFilled(
      @StartScreen StandardEditorTestAPI<Visit, VisitEdit> visitEdit,
      @NewEntity Pet pikachu
  ) {

    pikachu.setName("Pikachu");

    // when:
    OperationResult outcome = (OperationResult) visitEdit
        .interact(enter(dateField("visitDateField"), new Date()))
        .interact(enter(textField("descriptionField"), "Regular Visit"))
        .interact(select(lookupField("petField"), pikachu))
        .andThenGet(closeEditor());

    // then:
    assertThat(outcome).isEqualTo(OperationResult.success());

    // and:
    assertThat(environment.getUiTestAPI().isActive(visitEdit))
      .isFalse();

  }

  @AfterEach
  public void closeAllScreens() {
      environment.getUiTestAPI().closeAllScreens();
      TestServiceProxy.clear();
  }
}
