package com.haulmont.sample.petclinic.web.guide;

import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.TestEntityFactory;
import com.haulmont.cuba.web.testsupport.TestEntityState;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import com.haulmont.sample.petclinic.web.pet.pet.PetEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PetEditTest {

    @RegisterExtension
    TestUiEnvironment environment =
            new TestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
                    .withScreenPackages(
                            "com.haulmont.cuba.web.app.main",
                            "com.haulmont.sample.petclinic.web"
                    )
                    .withUserLogin("admin");


    private Screens screens;
    private TestEntityFactory<Pet> petsFactory;

    @BeforeEach
    void setUp() {
        petsFactory = environment.getContainer().getEntityFactory(Pet.class, TestEntityState.NEW);

        screens = environment.getScreens();
        screens.create(MainScreen.class, OpenMode.ROOT).show();
    }

    @Test
    void identificationNumberIsCorrectlyBoundInTheInputField() {

        Pet pet = petsFactory.create(Collections.singletonMap("identificationNumber", "019"));
        PetEdit petEdit = showPetEditorFor(pet);

        TextInputField identificationNumber = identificationNumberField(petEdit);

        assertThat(identificationNumber.getValue())
            .isEqualTo("019");
    }

    private PetEdit showPetEditorFor(Pet pet) {
        PetEdit petEdit = screens.create(PetEdit.class);
        petEdit.setEntityToEdit(pet);
        petEdit.show();
        return petEdit;
    }

    private TextInputField identificationNumberField(PetEdit petEdit) {
        return (TextInputField) petEdit.getWindow().getComponent("identificationNumberField");
    }

}
