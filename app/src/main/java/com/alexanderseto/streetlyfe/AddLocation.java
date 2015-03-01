package com.alexanderseto.streetlyfe;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class AddLocation extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void postLocation(View v) {
        final EditText editText = (EditText) findViewById(R.id.editText);
        String info = editText.getText().toString();
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String category = spinner.getSelectedItem().toString();
        final EditText titleText = (EditText) findViewById(R.id.editText2);
        String title = titleText.getText().toString();
        LatLng latLng = BathroomFragment.userLocation;
        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(latLng.latitude, latLng.longitude);

        ParseObject waypoint = new ParseObject("Waypoint");
        waypoint.put("information", info);
        waypoint.put("category", category);
        waypoint.put("title", title);
        waypoint.put("location", parseGeoPoint);
        waypoint.put("upVotes", 0);
        waypoint.put("downVotes", 0);

        waypoint.saveInBackground();

        finishActivity(1);
    }
}
