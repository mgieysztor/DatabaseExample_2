package com.offcasoftware.databaseexample.view;

import com.offcasoftware.databaseexample.AndroidApplication;
import com.offcasoftware.databaseexample.R;
import com.offcasoftware.databaseexample.databse.Database;
import com.offcasoftware.databaseexample.model.Address;
import com.offcasoftware.databaseexample.model.Order;
import com.offcasoftware.databaseexample.model.Person;
import com.offcasoftware.databaseexample.model.PersonQuery;
import com.offcasoftware.databaseexample.repository.PersonRepository;
import com.offcasoftware.databaseexample.repository.PersonRepositoryImpl;
import com.offcasoftware.databaseexample.view.adapter.PersonAdapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements PersonAdapter.PersonInterface {

    @BindView(R.id.order_none)
    RadioButton mOrderNone;

    @BindView(R.id.order_asc)
    RadioButton mOrderAsc;

    @BindView(R.id.order_desc)
    RadioButton mOrderDesc;

    @BindView(R.id.query_name)
    EditText mPersonName;

    @BindView(R.id.query_country)
    EditText mPersonCountry;

    @BindView(R.id.query_click)
    EditText mPersonClick;

    @BindView(R.id.query_city)
    EditText mPersonCity;

    @BindView(R.id.persons_recycler_view)
    RecyclerView mRecyclerView;

    private PersonAdapter mPersonAdapter;
    private PersonRepository mPersonRepository;

    Database mDatabase = AndroidApplication.getDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        displayAllData();
    }

    @Override
    public void onPersonClick(final Person person) {
        Toast.makeText(this, person.getAddress().getCity(), Toast.LENGTH_SHORT).show();

//        Log.i("Person clicked id: ", String.valueOf(person.getId()));

        mPersonRepository.updateClick(person.getId(), person.getClicked());
        displayAllData();
    }

    @Override
    public void onPersonDeleteClick(final Person person) {
//        Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
//        Log.i("Person del clicked id: ", String.valueOf(person.getId()));

//        mDatabase.deletePerson(person);
        mPersonRepository.removePerson(person.getId());
        displayAllData();
    }

    @OnClick(R.id.button_search)
    public void onSearchClick(View view) {
        getFilteredPerson();

    }

    private void init() {
        mPersonAdapter = new PersonAdapter(this);
        mPersonAdapter.setPersonInterface(this);
        mPersonRepository = PersonRepositoryImpl.getInstance();
        mRecyclerView.setAdapter(mPersonAdapter);
    }

    private void displayAllData() {
        List<Person> persons = mPersonRepository.getPersons();
        mPersonAdapter.swapData(persons);
    }

    private void getFilteredPerson() {
        String city = mPersonCity.getText().toString().trim();

        if (city !=null || city.length()>0){
            List<Person> persons =  mPersonRepository.getCityPerson(city);
            mPersonAdapter.swapData(persons);
        }


        Order order = Order.NONE;

        if (mOrderNone.isChecked()) {
            order = Order.NONE;
        } else if (mOrderAsc.isChecked()) {
            order = Order.ASC;
        } else if (mOrderDesc.isChecked()) {
            order = Order.DESC;
        }

        String name = mPersonName.getText().toString().trim();
//        String country = mPersonCountry.getText().toString();
//        int click = Integer.parseInt(mPersonClick.getText().toString());
//        String city = mPersonCity.getText().toString();

//        mPersonRepository.getFilteredPerson(name, country, click, city);


        PersonQuery personQuery = new PersonQuery(order, name, null, 0);

        List<Person> persons = mPersonRepository.getFilteredPerson(personQuery);
        mPersonAdapter.swapData(persons);

    }
}
