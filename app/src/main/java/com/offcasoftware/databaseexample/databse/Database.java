package com.offcasoftware.databaseexample.databse;

import com.offcasoftware.databaseexample.model.Address;
import com.offcasoftware.databaseexample.model.Person;
import com.offcasoftware.databaseexample.model.PersonQuery;

import java.util.List;

/**
 * @author maciej.pachciarek on 2017-03-08.
 */

public interface Database {

    void savePersons(List<Person> personList);

    List<Person> getPersons();

    void deletePerson(Person person);

    void addClick(Person person);

    void updateClick (int personId, int click);

    void removePerson(int personId);

    List<Person> getFilteredPerson(PersonQuery personquery);


    List<Person> getCityPerson(String city);
}
