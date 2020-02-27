package com.haulmont.sample.petclinic.web.visit;

import static de.diedavids.sneferu.ComponentDescriptors.button;
import static de.diedavids.sneferu.ComponentDescriptors.dateField;
import static de.diedavids.sneferu.ComponentDescriptors.lookupField;
import static de.diedavids.sneferu.ComponentDescriptors.textField;
import static de.diedavids.sneferu.Interactions.click;
import static de.diedavids.sneferu.Interactions.closeEditor;
import static de.diedavids.sneferu.Interactions.enter;
import static de.diedavids.sneferu.Interactions.select;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.environment.NewEntity;
import de.diedavids.sneferu.environment.StartScreen;
import de.diedavids.sneferu.environment.SubsequentScreen;
import com.haulmont.sample.petclinic.web.visit.visit.VisitBrowse;
import com.haulmont.sample.petclinic.web.visit.visit.VisitEdit;
import de.diedavids.sneferu.screen.StandardEditorTestAPI;
import de.diedavids.sneferu.screen.StandardLookupTestAPI;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class VisitBrowseToEditTest {

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
      @StartScreen StandardLookupTestAPI<Visit,VisitBrowse> visitBrowse,
      @SubsequentScreen StandardEditorTestAPI<Visit, VisitEdit> visitEdit,
      @NewEntity Pet pikachu
  ) {

    // given:
    pikachu.setName("Pikachu");

    // and:
    visitBrowse.interact(click(button("createBtn")));

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
