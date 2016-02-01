package com.example.srinivas.contactapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by srinivas on 11/5/15.
 */
public class EditActivity extends Activity {

    private String name;
    private String phone;
    private String company;
    private String emailId;
    private String addr1;
    private String addr2;

    private EditText etName;
    private EditText etPhone;
    private EditText etCompany;
    private EditText etEmail;
    private EditText etAddr1;
    private EditText etAddr2;
    private Button bEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        etName = (EditText) findViewById(R.id.etNameDetail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etCompany = (EditText) findViewById(R.id.etCompany);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAddr1 = (EditText) findViewById(R.id.etAddr1);
        etAddr2 = (EditText) findViewById(R.id.etAddr2);
        bEdit = (Button) findViewById(R.id.bEdit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            name = (String) bundle.get("name");
            phone = (String) bundle.get("phone");
            company = (String) bundle.get("company");
            emailId = (String) bundle.get("email");
            addr1 = (String) bundle.get("addr1");
            addr2 = (String) bundle.get("addr2");

            etName.setText(name);
            etPhone.setText(phone);
            etCompany.setText(company);
            etEmail.setText(emailId);
            etAddr1.setText(addr1);
            etAddr2.setText(addr2);


        }

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Does not work to set the data, as it gets too complicated to set static data, one on top of other!
                //Can be done, with more efforts.
                Toast.makeText(getApplicationContext(),"Does Not Work", Toast.LENGTH_LONG).show();
            }
        });

    }
}
