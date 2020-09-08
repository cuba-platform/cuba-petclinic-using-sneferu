package com.haulmont.sample.petclinic.web.guide;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.proxy.DataServiceProxy;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.owner.Owner;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import com.haulmont.sample.petclinic.web.owner.owner.OwnerBrowse;
import com.haulmont.sample.petclinic.web.owner.owner.OwnerEdit;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.environment.StartScreen;
import de.diedavids.sneferu.environment.SubsequentScreen;
import de.diedavids.sneferu.screen.StandardEditorTestAPI;
import de.diedavids.sneferu.screen.StandardLookupTestAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import static de.diedavids.sneferu.ComponentDescriptors.*;
import static de.diedavids.sneferu.Interactions.*;

class OwnerBrowseSneferuTest {

    @RegisterExtension
    SneferuTestUiEnvironment environment =
            new SneferuTestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
                    .withScreenPackages(
                            "com.haulmont.cuba.web.app.main",
                            "com.haulmont.sample.petclinic.web"
                    )
                    .withUserLogin("admin")
                    .withMainScreen(MainScreen.class);

    private DataService dataService;
    private PetclinicData data;
    private Owner ash;
    private Owner misty;
    private List<Owner> ashAndMisty;

    @BeforeEach
    void setUp() {
        mockDataService();

        data = new PetclinicData(environment.getContainer());

        ash = data.owner("Ash", "Ketchum");
        misty = data.owner("Misty");
        ashAndMisty = asList(ash, misty);

        when(ownerListIsLoaded())
                .thenReturn(ashAndMisty);
    }

    private void mockDataService() {
        dataService = Mockito.spy(
                new DataServiceProxy(environment.getContainer())
        );
        TestServiceProxy.mock(DataService.class, dataService);
    }

    @Test
    void given_ownerIsSelected_when_editIsPerformed_then_ownerEditorIsOpened(
            @StartScreen StandardLookupTestAPI<Owner, OwnerBrowse> ownerBrowse,
            @SubsequentScreen StandardEditorTestAPI<Owner, OwnerEdit> ownerEdit
    ) {

        ownerBrowse
                .interact(selectInList(table("ownersTable"), ash))
                .andThen(click(button("editBtn")));

        assertThat(ownerEdit.screen().getEditedEntity())
                .isEqualTo(ash);
    }

    private List<Owner> ownerListIsLoaded() {
        LoadContext<Owner> loadOwnerList = Mockito.argThat(loadContext ->
                loadContext.getEntityMetaClass().equals("petclinic_Owner")
        );
        return dataService.loadList(loadOwnerList);
    }

}
