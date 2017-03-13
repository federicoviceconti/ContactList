package com.example.personale.contactlist.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.personale.contactlist.R;
import com.example.personale.contactlist.controller.utility.Field;
import com.example.personale.contactlist.database.DbField;
import com.example.personale.contactlist.model.Contact;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by personale on 09/03/2017.
 */

public class AddNewContact extends AppCompatActivity implements View.OnClickListener {

    EditText nameEt, phoneEt, addressEt, mailEt;
    Button btnConfirm, btnCancel;
    ImageView imageAccount;

    private int state = Field.ADD;
    private int PICK_PHOTO = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nameEt = (EditText) findViewById(R.id.name_et);
        phoneEt = (EditText) findViewById(R.id.phone_et);
        addressEt = (EditText) findViewById(R.id.address_et);
        mailEt = (EditText) findViewById(R.id.mail_et);

        imageAccount = (ImageView)findViewById(R.id.account_image);
        imageAccount.setOnClickListener(this);

        btnConfirm = (Button) findViewById(R.id.confirm_action);
        btnCancel = (Button) findViewById(R.id.cancel_action);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        initializeComponent(getIntent() != null ? getIntent().getFlags() : -1);
    }

    private void initializeComponent(int flag) {

        if (flag != -1) {
            Contact c = getIntent().getParcelableExtra(Field.OBJECT);

            nameEt.setText(c.getName());
            addressEt.setText(c.getAddress());
            phoneEt.setText(c.getPhone());
            mailEt.setText(c.getMail());

            /*nameEt.setText(getIntent().getStringExtra(DbField.NAME));
            addressEt.setText(getIntent().getStringExtra(DbField.ADDRESS));
            phoneEt.setText(getIntent().getStringExtra(DbField.PHONE));
            mailEt.setText(getIntent().getStringExtra(DbField.MAIL));*/

            state = getIntent().getFlags();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_action:
                setResult(Activity.RESULT_OK, addContact());
                finish();
                break;
            case R.id.cancel_action:
                setResult(cancelAction());
                finish();
                break;
            case R.id.account_image:
                pickImage();
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_PHOTO){
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                imageAccount.setImageBitmap(yourSelectedImage);
                imageAccount.setMaxWidth(128);
                imageAccount.setMaxHeight(128);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelAction();
    }

    private Intent addContact() {
        Intent i = new Intent();
        i.putExtra(DbField.NAME, nameEt.getText().toString());
        i.putExtra(DbField.PHONE, phoneEt.getText().toString());
        i.putExtra(DbField.ADDRESS, addressEt.getText().toString());
        i.addFlags(state);
        return i;
    }

    private int cancelAction() {
        finish();
        return Activity.RESULT_CANCELED;
    }
}
