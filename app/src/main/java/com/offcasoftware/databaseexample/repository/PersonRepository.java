package com.offcasoftware.databaseexample.repository;

import com.offcasoftware.databaseexample.model.Address;
import com.offcasoftware.databaseexample.model.Person;
import com.offcasoftware.databaseexample.model.PersonQuery;

import java.util.List;

/**
 * @author maciej.pachciarek on 08.03.2017.
 */

public interface PersonRepository {

    List<Person> getPersons();

    void removePerson (int personId);

    void updateClick (int personId, int click);

    List<Person> getFilteredPerson(PersonQuery personquery);

    List<Person> getCityPerson (String city);
}
