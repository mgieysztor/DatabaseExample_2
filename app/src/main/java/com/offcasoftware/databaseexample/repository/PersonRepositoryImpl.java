package com.offcasoftware.databaseexample.repository;

import com.offcasoftware.databaseexample.AndroidApplication;
import com.offcasoftware.databaseexample.databse.Database;
import com.offcasoftware.databaseexample.model.Address;
import com.offcasoftware.databaseexample.model.Person;
import com.offcasoftware.databaseexample.model.PersonQuery;
import com.offcasoftware.databaseexample.net.PersonService;
import com.offcasoftware.databaseexample.net.PersonServiceImpl;

import java.util.List;

/**
 * @author maciej.pachciarek on 08.03.2017.
 */

public class PersonRepositoryImpl implements PersonRepository {

    private static final PersonRepositoryImpl ourInstance = new PersonRepositoryImpl();

    public static PersonRepositoryImpl getInstance() {
        return ourInstance;
    }

    private final PersonService mPersonService;
    private final Database mDatabase;

    private PersonRepositoryImpl() {
        mPersonService = new PersonServiceImpl();
        mDatabase = AndroidApplication.getDatabase();
    }

    @Override
    public List<Person> getPersons() {
        List<Person> personsPersistant = mDatabase.getPersons();
        if (personsPersistant.isEmpty()) {
            List<Person> person = mPersonService.downloadPersons();
            mDatabase.savePersons(person);
            return person;
        }

        return mDatabase.getPersons();
    }

    @Override
    public void removePerson(int personId) {
        mDatabase.removePerson(personId);
    }

    @Override
    public void updateClick(int personId, int click) {
        mDatabase.updateClick(personId, click);
    }

    @Override
    public List<Person> getFilteredPerson(PersonQuery personQuery) {

        return mDatabase.getFilteredPerson(personQuery);
    }

    @Override
    public List<Person> getCityPerson(String city) {
        return mDatabase.getCityPerson(city);

    }


}
