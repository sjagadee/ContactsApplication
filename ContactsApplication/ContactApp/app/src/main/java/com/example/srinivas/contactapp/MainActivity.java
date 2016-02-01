package com.example.srinivas.contactapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.srinivas.jsonobjects.ContactsListObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {

    ListView list;
    //ContactListAdapter adapter;
    ArrayList<ContactsListObject> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.lvContacts);
        contactsList = new ArrayList<>();

        DownloadTask task = new DownloadTask();
        task.execute("https://solstice.applauncher.com/external/contacts.json");
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;

            try {
                url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);

                    int data = reader.read();
                    while (data != -1) {
                        char current = (char) data;

                        result += current;
                        data = reader.read();
                    }

                    return result;
                } finally {
                    urlConnection.disconnect();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);


                for (int i = 0; i < jsonArray.length(); i++) {
                    ContactsListObject contactsListObject = new ContactsListObject();
                    JSONObject object = jsonArray.getJSONObject(i);

                    // storing objects in object class
                    contactsListObject.setName(object.getString("name"));
                    contactsListObject.setPhone(object.getJSONObject("phone").getString("home"));
                    contactsListObject.setSmallImageURL(object.getString("smallImageURL"));
                    contactsListObject.setBirthdate(object.getString("birthdate"));
                    contactsListObject.setDetailsURL(object.getString("detailsURL"));
                    contactsListObject.setCompany(object.getString("company"));


                    contactsList.add(contactsListObject);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ContactListAdapter adapter = new ContactListAdapter(getApplicationContext(), R.layout.row, contactsList);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), DetailedActivity.class);
                    intent.putExtra("name", contactsList.get(position).getName());
                    intent.putExtra("phone", contactsList.get(position).getPhone());
                    intent.putExtra("company", contactsList.get(position).getCompany());
                    intent.putExtra("detailsURL", contactsList.get(position).getDetailsURL());
                    intent.putExtra("birthdate", contactsList.get(position).getBirthdate());
                    startActivity(intent);
                }
            });
        }
    }
}