package com.haulmont.sample.petclinic.web.guide;

import static de.diedavids.sneferu.ComponentDescriptors.button;
import static de.diedavids.sneferu.ComponentDescriptors.lookupField;
import static de.diedavids.sneferu.ComponentDescriptors.textInputField;
import static de.diedavids.sneferu.Interactions.click;
import static de.diedavids.sneferu.Interactions.enter;
import static de.diedavids.sneferu.Interactions.select;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.pet.PetType;
import com.haulmont.sample.petclinic.service.DiseaseWarningMailingService;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import com.haulmont.sample.petclinic.web.pet.pet.CreateDiseaseWarningMailing;
import com.haulmont.sample.petclinic.web.pet.pet.PetBrowse;
import de.diedavids.sneferu.Interactions;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.environment.StartScreen;
import de.diedavids.sneferu.screen.InputDialogTestAPI;
import de.diedavids.sneferu.screen.StandardLookupTestAPI;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

class DiseaseWarningMailingInputDialogVariantsTest {

    @RegisterExtension
    SneferuTestUiEnvironment environment =
        new SneferuTestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
            .withScreenPackages(
                "com.haulmont.cuba.web.app.main",
                "com.haulmont.sample.petclinic.web",
                "com.haulmont.cuba.gui.app.core.inputdialog"
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


    @Test
    @DisplayName("[InputDialogFacet] When button is clicked and data is entered in Input Dialog, then the Service is invoked")
    void given_inputDialogFacet_when_buttonIsClicked_and_dataIsEnteredInInputDialog_then_theServiceIsInvoked(
        @StartScreen StandardLookupTestAPI<Pet, PetBrowse> browse
    ) {

        // given:
        browse
            .interact(click(button("createDiseaseWarningMailingInputDialogFacetBtn")));


        final InputDialogTestAPI inputDialogFacet = environment.getUiTestAPI()
            .openedInputDialog();


        // when:
        final OperationResult operationResult = inputDialogFacet
            .interact(enter(textInputField("city"), "Alabastia"))
            .andThen(enter(textInputField("disease"), "Fever"))
            .andThen(select(lookupField("petType"), electricType))
            .andThenGet(Interactions.closeInputDialogWith(InputDialog.INPUT_DIALOG_OK_ACTION));


        // then:
        assertThat(operationResult)
            .isEqualTo(OperationResult.success());

        // and:
        assertThat(environment.getUiTestAPI().isActive(inputDialogFacet))
            .isFalse();


        // and: this interaction should happen as the dialog was successfully closed
        verify(diseaseWarningMailingService, times(1))
            .warnAboutDisease(
                electricType,
                "Fever",
                "Alabastia"
            );
    }



    @Test
    @DisplayName("[InputDialog] When button is clicked and data is entered in Input Dialog, then the Service is invoked")
    void given_inputDialog_when_buttonIsClicked_and_dataIsEnteredInInputDialog_then_theServiceIsInvoked(
        @StartScreen StandardLookupTestAPI<Pet, PetBrowse> browse
    ) {

        // given:
        browse
            .interact(click(button("createDiseaseWarningMailingInputDialogBtn")));


        final InputDialogTestAPI inputDialogFacet = environment.getUiTestAPI()
            .openedInputDialog();


        // when:
        final OperationResult operationResult = inputDialogFacet
            .interact(enter(textInputField("city"), "Alabastia"))
            .andThen(enter(textInputField("disease"), "Fever"))
            .andThen(select(lookupField("petType"), electricType))
            .andThenGet(Interactions.closeInputDialogWith(InputDialog.INPUT_DIALOG_OK_ACTION));


        // then:
        assertThat(operationResult)
            .isEqualTo(OperationResult.success());

        // and:
        assertThat(environment.getUiTestAPI().isActive(inputDialogFacet))
            .isFalse();


        // and: this interaction should happen as the dialog was successfully closed
        verify(diseaseWarningMailingService, times(1))
            .warnAboutDisease(
                electricType,
                "Fever",
                "Alabastia"
            );
    }

}
