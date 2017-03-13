package com.example.personale.contactlist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.personale.contactlist.R;
import com.example.personale.contactlist.controller.adapter.ContactAdapter;
import com.example.personale.contactlist.controller.utility.Field;

/**
 * Created by personale on 09/03/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    ContactAdapter contactAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactAdapter = new ContactAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        findViewById(R.id.add_contact).setOnClickListener(this);

        initializeComponent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Field.ADD:
                    contactAdapter.addContact(data);
                    recyclerView.scrollToPosition(0);
                    break;
                case Field.EDIT:
                    contactAdapter.editContact(data, data.getIntExtra(Field.POSITION, -1));
                    break;
            }
        } else {
            Snackbar.make(findViewById(R.id.coordinator), "Operazione annullata!", Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_contact:
                startActivityForResult(new Intent(this, AddNewContact.class), Field.ADD);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trash:
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void initializeComponent() {
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(contactAdapter);
        }

        contactAdapter.loadFromDb();
    }
}
