package com.haulmont.sample.petclinic.web.guide;

import com.haulmont.cuba.web.testsupport.TestContainer;
import com.haulmont.cuba.web.testsupport.TestEntityFactory;
import com.haulmont.cuba.web.testsupport.TestEntityState;
import com.haulmont.sample.petclinic.entity.owner.Owner;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.pet.PetType;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import org.junit.platform.engine.TestTag;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.*;


/**
 * PetclinicData represents an API abstraction for creating different entities for test purposes
 */
public class PetclinicData {

    private final TestContainer container;

    public PetclinicData(TestContainer container) {

        this.container = container;
    }

    public List<Owner> owners(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(index -> owner("Ash" + index, "Ketchum"))
                .collect(Collectors.toList());
    }

    public Owner owner(String firstName, String lastName) {
        return ownerFactory().create(
                "firstName", firstName,
                "lastName", lastName
        );
    }

    private TestEntityFactory<Owner> ownerFactory() {
        return container.getEntityFactory(Owner.class, TestEntityState.NEW);
    }

    public Owner owner(String firstName) {
        return ownerFactory().create(
                "firstName", firstName
        );
    }


    public PetType electricType() {
        PetType type = new PetType();
        type.setName("Electric");
        return type;
    }
    public PetType waterType() {
        PetType type = new PetType();
        type.setName("Water");
        return type;
    }

    public PetType fireType() {
        PetType type = new PetType();
        type.setName("Fire");
        return type;
    }


}