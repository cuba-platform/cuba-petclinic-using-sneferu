package com.haulmont.sample.petclinic.web.guide;

import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.pet.PetType;
import com.haulmont.sample.petclinic.service.DiseaseWarningMailingService;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import com.haulmont.sample.petclinic.web.pet.pet.CreateDiseaseWarningMailing;
import com.haulmont.sample.petclinic.web.pet.pet.PetBrowse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DiseaseWarningMailingTest {

    @RegisterExtension
    TestUiEnvironment environment =
            new TestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
                    .withScreenPackages(
                            "com.haulmont.cuba.web.app.main",
                            "com.haulmont.sample.petclinic.web"
                    )
                    .withUserLogin("admin");

    private Screens screens;
    private DiseaseWarningMailingService diseaseWarningMailingService;
    private CreateDiseaseWarningMailing dialog;
    private PetType electricType;

    @BeforeEach
    void setUp() {

        setupTestData();

        mockDiseaseWarningMailingService();

        openWarningMailingDialogFromPetBrowse();

    }

    private void setupTestData() {
        PetclinicData data = new PetclinicData(environment.getContainer());

        electricType = data.electricType();
    }

    private void mockDiseaseWarningMailingService() {

        diseaseWarningMailingService = Mockito.mock(
                DiseaseWarningMailingService.class
        );

        TestServiceProxy.mock(
                DiseaseWarningMailingService.class,
                diseaseWarningMailingService
        );
    }

    private void openWarningMailingDialogFromPetBrowse() {
        screens = environment.getScreens();
        screens.create(MainScreen.class, OpenMode.ROOT).show();

        PetBrowse petBrowse = openScreen(PetBrowse.class);

        petTable(petBrowse)
                .getAction("createDiseaseWarningMailing")
                .actionPerform(createDiseaseWarningMailingBtn(petBrowse));

        dialog = findOpenScreen(CreateDiseaseWarningMailing.class);

    }

    @Nested
    @DisplayName("When Valid Dialog Input Data, then...")
    class When_SubmitValidDialogInput {

        @BeforeEach
        void fillFields() {
            city(dialog).setValue("Alabastia");
            disease(dialog).setValue("Fever");
            petType(dialog).setValue(electricType);
        }

        @Test
        @DisplayName("Form is valid")
        void then_formIsValid() {

            submitDialog(dialog);

            assertThat(dialog.validationResult().getAll())
                    .isEmpty();
        }

        @Test
        @DisplayName("Mailing is send")
        void then_mailingIsSend() {

            submitDialog(dialog);

            verify(diseaseWarningMailingService, times(1))
                    .warnAboutDisease(
                            electricType,
                            "Fever",
                            "Alabastia"
                    );
        }

    }

    @Nested
    @DisplayName("When Invalid Dialog Input Data, then...")
    class When_SubmitInvalidDialogInput {

        @BeforeEach
        void fillCorrectFields() {
            city(dialog).setValue("Alabastia");
            disease(dialog).setValue("Fever");
        }

        @Test
        @DisplayName("Mailing is not send")
        void then_diseaseWarningMailingIsNotSend() {

            petType(dialog).setValue(null);

            submitDialog(dialog);

            verify(diseaseWarningMailingService, never())
                    .warnAboutDisease(any(), anyString(), anyString());
        }

        @Test
        @DisplayName("Form contains Errors")
        void then_formContainsValidationErrors() {

            petType(dialog).setValue(null);

            submitDialog(dialog);

            ValidationErrors validationErrors = dialog.validationResult();

            assertThat(validationErrors.getAll())
                    .isNotEmpty();

            assertThat(petTypeValidationError(validationErrors))
                    .isPresent();
        }

        private Optional<ValidationErrors.Item> petTypeValidationError(
                ValidationErrors validationErrors
        ) {
            return validationErrors.getAll().stream()
                    .filter(item -> item.component == petType(dialog))
                    .findFirst();
        }

    }

    private void submitDialog(CreateDiseaseWarningMailing dialog) {
        dialog
                .getWindow()
                .getAction("createDiseaseWarningMailing")
                .actionPerform(createMailingBtn(dialog));
    }


    private Component createMailingBtn(CreateDiseaseWarningMailing dialog) {
        return component(dialog, "createDiseaseWarningMailingBtn");
    }

    private TextInputField<String> city(CreateDiseaseWarningMailing dialog) {
        return (TextInputField<String>) component(dialog, "city");
    }

    private LookupField<PetType> petType(CreateDiseaseWarningMailing dialog) {
        return (LookupField<PetType>) component(dialog, "petType");
    }

    private TextInputField<String> disease(CreateDiseaseWarningMailing dialog) {
        return (TextInputField<String>) component(dialog, "disease");
    }

    private Table<Pet> petTable(PetBrowse petBrowse) {
        return (Table<Pet>) component(petBrowse, "petsTable");
    }

    private Component createDiseaseWarningMailingBtn(PetBrowse petBrowse) {
        return component(petBrowse, "createDiseaseWarningMailingBtn");
    }

    private Component component(Screen screen, String componentId) {
        return screen
                .getWindow()
                .getComponent(componentId);
    }

    private <T extends Screen> T findOpenScreen(Class<T> screenClass) {
        return (T) screens.getOpenedScreens().getAll().stream()
                .filter(openedScreen ->
                        openedScreen.getClass().isAssignableFrom(screenClass)
                )
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private <T extends Screen> T openScreen(Class<T> screenClass) {
        T screen = screens.create(screenClass);
        screen.show();
        return screen;
    }

}
