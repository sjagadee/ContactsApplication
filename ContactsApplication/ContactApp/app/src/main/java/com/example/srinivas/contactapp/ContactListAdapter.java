package com.example.srinivas.contactapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.srinivas.jsonobjects.ContactsListObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by srinivas on 11/4/15.
 */
public class ContactListAdapter extends ArrayAdapter<ContactsListObject> {

    ArrayList<ContactsListObject> arrayListContact;
    int resource;
    Context context;
    LayoutInflater layoutInflater;


    public ContactListAdapter(Context context, int resource, ArrayList<ContactsListObject> objects) {
        super(context, resource, objects);

        this.arrayListContact = objects;
        this.resource = resource;
        this.context = context;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        // view is getting created
        if(convertView == null) {
            convertView = layoutInflater.inflate(resource, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.ivImage);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhoneNumber);

            convertView.setTag(holder);

        } else { // view is being recycled, could have used ViewGroup called RecyclerView
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(R.mipmap.ic_launcher);// could have used from the objects
        new DownloadImageTask(holder.imageView).execute(arrayListContact.get(position).getSmallImageURL());
        holder.tvName.setText(arrayListContact.get(position).getName());
        holder.tvPhone.setText(arrayListContact.get(position).getPhone());

        return convertView;
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView tvName;
        private TextView tvPhone;
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
