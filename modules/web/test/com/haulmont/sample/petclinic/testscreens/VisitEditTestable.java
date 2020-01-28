package com.haulmont.sample.petclinic.testscreens;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.web.visit.visit.VisitEdit;

import javax.inject.Inject;
import java.util.Date;

@UiController("petclinic_Visit.edit")
@UiDescriptor("/com/haulmont/sample/petclinic/web/visit/visit/visit-edit.xml")
public class VisitEditTestable extends VisitEdit {

    @Inject
    public TextArea<String> descriptionField;
    @Inject
    public HBoxLayout editActions;
    @Inject
    public Form fieldGroup;
    @Inject
    public LookupPickerField<Pet> petField;
    @Inject
    public DateField<Date> visitDateField;
}
