package com.example.srinivas.contactapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by srinivas on 11/4/15.
 */
public class DetailedActivity extends AppCompatActivity {

    private TextView tvName;
    private ImageView ivImageLarge;
    private TextView tvPhone;
    private TextView tvCompany;
    private TextView tvDob;
    private TextView tvEmail;
    private TextView tvAddr1;
    private TextView tvAddr2;
    private Button bMyLocation;
    private Button bMyWebSite;

    private String name;
    private String phone;
    private String company;
    private String birthdate;
    private String detailsURL;
    private String emailId;
    private String addr1;
    private String addr2;
    private String largeImage;
    private String webSite;
    private Double latitude;
    private Double longitude;
    private String favorite;
    private Date dob;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = (TextView) findViewById(R.id.tvNameDetail);
        ivImageLarge = (ImageView) findViewById(R.id.ivImageLarge);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvCompany = (TextView) findViewById(R.id.tvCompany);
        tvDob = (TextView) findViewById(R.id.tvBirthDay);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvAddr1 = (TextView) findViewById(R.id.tvAddr1);
        tvAddr2 = (TextView) findViewById(R.id.tvAddr2);
        bMyLocation = (Button) findViewById(R.id.bMyLocation);
        bMyWebSite = (Button) findViewById(R.id.bMyWebSite);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            detailsURL = (String) bundle.get("detailsURL");

            name = (String) bundle.get("name");
            phone = (String) bundle.get("phone");
            company = (String) bundle.get("company");

            tvName.setText(name);
            tvPhone.setText(phone);
            tvCompany.setText(company);


            birthdate = (String) bundle.get("birthdate");
            dob = new Date(Integer.parseInt(birthdate.substring(0, 5)));
            tvDob.setText(String.format("%1$tb %1$td, %1$tY", dob));

        }

        DownloadTask task = new DownloadTask();
        task.execute(detailsURL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_fav) {

            Toast.makeText(getApplicationContext(), "This would contain fav", Toast.LENGTH_LONG).show();

        } else if (id == R.id.action_edit) {

            Intent intent = new Intent(getApplicationContext(), EditActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("phone", phone);
            intent.putExtra("company", company);
            intent.putExtra("email", emailId);
            intent.putExtra("addr1", addr1);
            intent.putExtra("addr2", addr2);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
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

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    emailId = jsonObject.getString("email");
                    addr1 = jsonObject.getJSONObject("address").getString("street");
                    addr2 = jsonObject.getJSONObject("address").getString("city") + ", "
                            + jsonObject.getJSONObject("address").getString("state")  + " "
                            + jsonObject.getJSONObject("address").getString("zip") + ", "
                            + jsonObject.getJSONObject("address").getString("country");
                    largeImage = jsonObject.getString("largeImageURL");
                    webSite = jsonObject.getString("website");
                    latitude = jsonObject.getJSONObject("address").getDouble("latitude");
                    longitude = jsonObject.getJSONObject("address").getDouble("longitude");
                    favorite = jsonObject.getString("favorite");

                    tvEmail.setText(emailId);
                    tvAddr1.setText(addr1);
                    tvAddr2.setText(addr2);

                    ivImageLarge.setImageResource(R.mipmap.ic_launcher);
                    new DownloadImageTask(ivImageLarge).execute(largeImage);

                    bMyLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);

                            startActivity(intent);
                        }
                    });

                    bMyWebSite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                            intent.putExtra("url", webSite);

                            startActivity(intent);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
