package com.haulmont.sample.petclinic.testscreens;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.sample.petclinic.web.visit.visit.VisitBrowse;

import javax.inject.Inject;

@UiController
@UiDescriptor("/com/haulmont/sample/petclinic/web/visit/visit/visit-browse.xml")
public class VisitBrowseTestable extends VisitBrowse {

    @Inject
    public Button createBtn;
}
