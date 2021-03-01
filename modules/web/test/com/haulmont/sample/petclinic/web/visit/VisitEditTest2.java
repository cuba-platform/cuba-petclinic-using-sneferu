package com.haulmont.sample.petclinic.web.visit;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.testscreens.VisitBrowseTestable;
import com.haulmont.sample.petclinic.testscreens.VisitEditTestable;
import com.haulmont.sample.petclinic.web.PetclinicTestUiEnvironment;
import com.vaadin.ui.Button;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class VisitEditTest2 {

    @RegisterExtension
    public TestUiEnvironment environment =
            new PetclinicTestUiEnvironment()
                    .withScreenPackages(
                            "com.haulmont.cuba.web.app.main",
                            "com.haulmont.sample.petclinic.web"
                    )
                    .withUserLogin("admin");

    Screens screens;


    private Pet pikachu;
    private VisitEditTestable visitEdit;

    @BeforeEach
    public void setupEnvironment() {

        screens = environment.getScreens();

        VisitBrowseTestable visitBrowse = screens.create(VisitBrowseTestable.class);
        visitBrowse.show();

        visitBrowse.createBtn.unwrap(Button.class).click();


        visitEdit = (VisitEditTestable) screens.getOpenedScreens().getAll().stream()
                .filter(s ->
                        s.getClass().isAssignableFrom(VisitEditTestable.class)
                )
                .findFirst()
                .orElseThrow(RuntimeException::new);


        Metadata metadata = AppBeans.get(Metadata.class);
        pikachu = metadata.create(Pet.class);
        pikachu.setName("Pikachu");

    }

    @Test
    public void aVisitCanBeCreated_whenAllFieldsAreFilled() {

        // when:
        visitEdit.visitDateField.setValue(new Date());
        visitEdit.descriptionField.setValue("regular visit");
        visitEdit.petField.setValue(pikachu);

        OperationResult outcome = visitEdit.closeWithCommit();


        // then:
        assertThat(outcome).isEqualTo(OperationResult.success());

        // and:
        assertThat(screens.getOpenedScreens().getActiveScreens().stream().anyMatch(s -> s.getClass().isAssignableFrom(visitEdit.getClass())))
                .isFalse();
    }


    @AfterEach
    public void closeAllScreens() {
        environment.getScreens().removeAll();
        TestServiceProxy.clear();
    }
}
