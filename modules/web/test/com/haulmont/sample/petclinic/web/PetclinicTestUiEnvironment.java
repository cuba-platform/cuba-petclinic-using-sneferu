package com.haulmont.sample.petclinic.web;

import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.client.testsupport.TestUserSessionSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.sys.UiControllersConfiguration;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import com.haulmont.cuba.web.testsupport.ui.TestWindowConfig;

import java.util.Arrays;

import static java.util.Collections.singletonList;




public class PetclinicTestUiEnvironment extends TestUiEnvironment {
    public PetclinicTestUiEnvironment() {
        super(PetclinicWebTestContainer.Common.INSTANCE);
    }

    // todo temporary workaround to use full access role by default
    @Override
    protected void setupSession() {

        sessionSource = container.getBean(UserSessionSource.NAME);

        UserSession serverSession = sessionSource.createTestSession();
        ClientUserSession session = new ClientUserSession(serverSession);
        session.setAuthenticated(isSessionAuthenticated());

        sessionSource.setSession(session);
    }

    // we need to add extended screens as a separate configuration otherwise we'll get duplication exception
    @Override
    protected void exportScreens(String... packages) {
        TestWindowConfig windowConfig = container.getBean(TestWindowConfig.class);

        UiControllersConfiguration configuration = new UiControllersConfiguration();
        getInjector().autowireBean(configuration);
        configuration.setBasePackages(Arrays.asList(packages));

        UiControllersConfiguration testConf = new UiControllersConfiguration();
        getInjector().autowireBean(testConf);
        testConf.setBasePackages(Arrays.asList("com.haulmont.sample.petclinic.testscreens"));

        windowConfig.setConfigurations(Arrays.asList(configuration, testConf));
        windowConfig.reset();
    }
}
