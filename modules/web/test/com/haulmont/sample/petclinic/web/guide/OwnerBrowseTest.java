package com.haulmont.sample.petclinic.web.guide;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.TestEntityFactory;
import com.haulmont.cuba.web.testsupport.TestEntityState;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import com.haulmont.cuba.web.testsupport.proxy.DataServiceProxy;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.sample.petclinic.entity.owner.Owner;
import com.haulmont.sample.petclinic.web.PetclinicWebTestContainer;
import com.haulmont.sample.petclinic.web.owner.owner.OwnerBrowse;
import com.haulmont.sample.petclinic.web.owner.owner.OwnerEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class OwnerBrowseTest {

    @RegisterExtension
    TestUiEnvironment environment =
            new TestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
                    .withScreenPackages(
                            "com.haulmont.cuba.web.app.main",
                            "com.haulmont.sample.petclinic.web"
                    )
                    .withUserLogin("admin");

    private Screens screens;
    private DataService dataService;
    private PetclinicData data;
    private Owner ash;
    private Owner misty;
    private List<Owner> ashAndMisty;

    @BeforeEach
    void setUp() {
        mockDataService();

        screens = environment.getScreens();
        screens.create(MainScreen.class, OpenMode.ROOT).show();

        data = new PetclinicData(environment.getContainer());


        ash = data.owner("Ash", "Ketchum");
        misty = data.owner("Misty");
        ashAndMisty = asList(ash, misty);
    }

    private void mockDataService() {
        dataService = Mockito.spy(
                new DataServiceProxy(environment.getContainer())
        );
        TestServiceProxy.mock(DataService.class, dataService);
    }

    @Test
    void when_ownerListIsDisplayed_then_ownersAreShownInTheTable() {

        when(ownerListIsLoaded())
                .thenReturn(ashAndMisty);

        OwnerBrowse ownerBrowse = openScreen(OwnerBrowse.class);

        Table<Owner> ownersTable = ownersTable(ownerBrowse);

        assertThat(ownersTable.getItems().getItems())
                .hasSize(2);
    }

    @Test
    void given_ownerIsSelected_when_editIsPerformed_then_ownerEditorIsOpened() {

        when(ownerListIsLoaded())
                .thenReturn(ashAndMisty);

        OwnerBrowse ownerBrowse = openScreen(OwnerBrowse.class);

        Table<Owner> ownersTable = ownersTable(ownerBrowse);

        ownersTable.setSelected(ash);

        ownersTable
                .getAction("edit")
                .actionPerform(editButton(ownerBrowse));

        OwnerEdit ownerEdit = findOpenScreen(OwnerEdit.class);

        assertThat(ownerEdit.getEditedEntity())
                .isEqualTo(ash);
    }

    private List<Owner> ownerListIsLoaded() {
        LoadContext<Owner> loadOwnerList = Mockito.argThat(loadContext ->
                loadContext.getEntityMetaClass().equals("petclinic_Owner")
        );
        return dataService.loadList(loadOwnerList);
    }

    private Table<Owner> ownersTable(OwnerBrowse ownerBrowse) {
        return (Table<Owner>) ownerBrowse.getWindow().getComponent("ownersTable");
    }

    private Component editButton(OwnerBrowse ownerBrowse) {
        return ownerBrowse.getWindow().getComponent("editBtn");
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
