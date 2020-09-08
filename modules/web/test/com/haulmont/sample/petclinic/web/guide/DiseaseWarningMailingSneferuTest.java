package com.haulmont.sample.petclinic.web.guide;

import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.pet.PetType;
import com.haulmont.sample.petclinic.service.DiseaseWarningMailingService;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import com.haulmont.sample.petclinic.web.pet.pet.CreateDiseaseWarningMailing;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.environment.StartScreen;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import static de.diedavids.sneferu.ComponentDescriptors.*;
import static de.diedavids.sneferu.Interactions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DiseaseWarningMailingSneferuTest {

    @RegisterExtension
    SneferuTestUiEnvironment environment =
            new SneferuTestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
                    .withScreenPackages(
                            "com.haulmont.cuba.web.app.main",
                            "com.haulmont.sample.petclinic.web"
                    )
                    .withUserLogin("admin")
                    .withMainScreen(MainScreen.class);

    private DiseaseWarningMailingService diseaseWarningMailingService;
    private PetType electricType;

    @BeforeEach
    void setUp() {
        setupTestData();
        mockDiseaseWarningMailingService();
    }

    private void setupTestData() {
        electricType = new PetclinicData(environment.getContainer()).electricType();
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

    @Nested
    @DisplayName("When Valid Dialog Input Data, then...")
    class When_SubmitValidDialogInput {

        @Test
        @DisplayName("Form is valid")
        void then_formIsValid(
                @StartScreen StandardScreenTestAPI<CreateDiseaseWarningMailing> dialog
        ) {

            dialog
                    .interact(enter(textInputField("city"), "Alabastia"))
                    .andThen(enter(textInputField("disease"), "Fever"))
                    .andThen(select(lookupField("petType"), electricType))
                    .andThen(click(button("createDiseaseWarningMailingBtn")));

            assertThat(dialog.screen().validationResult().getAll())
                    .isEmpty();
        }

        @Test
        @DisplayName("Mailing is send")
        void then_mailingIsSend(
                @StartScreen StandardScreenTestAPI<CreateDiseaseWarningMailing> dialog
        ) {

            dialog
                    .interact(enter(textInputField("city"), "Alabastia"))
                    .andThen(enter(textInputField("disease"), "Fever"))
                    .andThen(select(lookupField("petType"), electricType))
                    .andThen(click(button("createDiseaseWarningMailingBtn")));

            verify(diseaseWarningMailingService, times(1))
                    .warnAboutDisease(
                            electricType,
                            "Fever",
                            "Alabastia"
                    );
        }
    }
}
