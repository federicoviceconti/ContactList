package com.example.personale.contactlist.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
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
    FloatingActionButton fab;
    public static final int STAGGERED_LAYOUT = 10;
    public static final int LINEAR_LAYOUT = 20;
    private int stateLayout = LINEAR_LAYOUT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactAdapter = new ContactAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        fab = (FloatingActionButton)findViewById(R.id.add_contact);
        fab.setOnClickListener(this);
        initializeComponent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.name_layout_preferences), Context.MODE_PRIVATE).edit();
        editor.putInt(getString(R.string.state_layout_preferences), stateLayout);
        editor.apply();
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
                contactAdapter.deleteContact(contactAdapter.getPosition());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_layout:
                setLayout(item);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLayout(MenuItem item) {
        switch (stateLayout){
            case STAGGERED_LAYOUT:
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                stateLayout = LINEAR_LAYOUT;
                break;
            case LINEAR_LAYOUT:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                stateLayout = STAGGERED_LAYOUT;
                break;
        }

        item.setIcon(stateLayout == LINEAR_LAYOUT ? R.drawable.ic_linear : R.drawable.ic_staggered);
    }
}
