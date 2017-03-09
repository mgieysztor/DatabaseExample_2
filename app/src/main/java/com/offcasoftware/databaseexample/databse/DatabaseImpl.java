package com.offcasoftware.databaseexample.databse;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.offcasoftware.databaseexample.model.Address;
import com.offcasoftware.databaseexample.model.Person;
import com.offcasoftware.databaseexample.model.PersonQuery;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maciej.pachciarek on 2017-03-08.
 */

public class DatabaseImpl extends OrmLiteSqliteOpenHelper implements Database {

    private final static String DATABASE_NAME = "persons";
    private final static int DATABASE_VERSION = 3;

    private RuntimeExceptionDao<Person, Integer> mPersonsDao;
    private RuntimeExceptionDao<Address, Integer> mAddressDao;

    public DatabaseImpl(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mPersonsDao = getRuntimeExceptionDao(Person.class);
        mAddressDao = getRuntimeExceptionDao(Address.class);
    }

    @Override
    public void onCreate(final SQLiteDatabase database, final ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Person.class);
            TableUtils.createTableIfNotExists(connectionSource, Address.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Person.class, true);
            TableUtils.dropTable(connectionSource, Address.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        onCreate(database, connectionSource);
    }

    @Override
    public void savePersons(final List<Person> personList) {
        try {
            getWritableDatabase().beginTransaction();

            for (Person person : personList) {
                mPersonsDao.create(person);
                Address address = person.getAddress();
                mAddressDao.createIfNotExists(address);
            }

            getWritableDatabase().setTransactionSuccessful();

        } finally {
            getWritableDatabase().endTransaction();
        }
    }

    @Override
    public List<Person> getPersons() {
        return mPersonsDao.queryForAll();
    }

    @Override
    public void deletePerson(Person person) {
        mPersonsDao.delete(person);
    }

    @Override
    public void addClick(Person person) {
        try {
            mPersonsDao.updateBuilder().updateColumnValue("clickedCount", Integer.valueOf(person.getClicked() + 1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mPersonsDao.update(person);
    }

    @Override
    public void updateClick(int personId, int click) {

        try {
            UpdateBuilder<Person, Integer> updateBuilder = mPersonsDao.updateBuilder();
            Where where = updateBuilder.where();
            where.eq("id", personId);
            updateBuilder.updateColumnValue("clickedCount", click + 1);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePerson(int personId) {
        mPersonsDao.deleteById(personId);
    }


    @Override
    public List<Person> getFilteredPerson(PersonQuery personquery) {
        try {
            QueryBuilder<Person, Integer> queryBuilder = mPersonsDao.queryBuilder();
            Where where = queryBuilder.where();
            if (personquery.getName() != null && personquery.getName().length() > 0) {

                where.like("name", "%" + personquery.getName() + "%");

            } else {
                queryBuilder.setWhere(null);
            }
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new ArrayList<>();
    }

    @Override
    public List<Person> getCityPerson(String city) {
        QueryBuilder<Person, Integer> queryBuilder = mPersonsDao.queryBuilder();
        QueryBuilder<Address, Integer> queryAddress = mAddressDao.queryBuilder();


        try {
            queryAddress.where().eq("city", "%" + city + "%");
            queryBuilder.join(queryAddress);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

}
