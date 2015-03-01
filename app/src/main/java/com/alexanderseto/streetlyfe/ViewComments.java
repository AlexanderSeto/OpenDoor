package com.alexanderseto.streetlyfe;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class ViewComments extends ActionBarActivity {

    //Bundle extras = getIntent().getExtras();

//        String postID = getIntent().getExtras().getString("postID");



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment);

        Intent intent = getIntent();

        String postID = intent.getStringExtra("postID");

//        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, "tkUqqlhmg1yPNJztnBPDRXPZwmeGOV9PDTfpWYIf", "3yWmzU1Whubl5vI9YxFvTFvEIMyYdy7GXdq0GrPE");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Waypoint");
        query.getInBackground(postID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    TextView titleText = (TextView) findViewById(R.id.titleTextView);
                    titleText.setText(parseObject.getString("title"));
                    TextView informationText = (TextView) findViewById(R.id.informationTextView);
                    informationText.setText(parseObject.getString("information"));
                    TextView upText = (TextView) findViewById(R.id.upTextView);
                    upText.setText(String.valueOf(parseObject.getInt("upVotes") - parseObject.getInt("downVotes")));
                } else {
                    // Shit's gone down

                }

            }
        });

        final ParseQuery<ParseObject> commentQuery = ParseQuery.getQuery("Comment");
        commentQuery.whereEqualTo("waypointID", postID);
        commentQuery.orderByDescending("upVotes");
        commentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                {

                    String[] comments = new String[parseObjects.size()];
                    int[] votes = new int[parseObjects.size()];

                    int i = 0;
                    for (ParseObject parseObject : parseObjects) {
                        votes[i] = parseObject.getInt("upVotes") - parseObject.getInt("downVotes");
                        comments[i] = parseObject.getString("text");
                        i++;
                    }

                    List items = java.util.Arrays.asList(comments);



                    ListView lv = (ListView) findViewById(R.id.commentListView);
                    ArrayAdapter myArrayAdapter = new ArrayAdapter<String>(ViewComments.this, R.layout.comment_layout,R.id.commentTextView,comments);
                    lv.setAdapter(myArrayAdapter);
                    lv.setTextFilterEnabled(true);
                    lv.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            return false;
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_comment, menu);
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
    public void clickUp(View v) {
        Intent intent = getIntent();

        String postID = intent.getStringExtra("postID");

        TextView upText = (TextView) findViewById(R.id.upTextView);
        upText.setText(Integer.toString(Integer.parseInt(upText.getText().toString()) + 1));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Waypoint");
        query.getInBackground(postID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {


                parseObject.put("upVotes", parseObject.getInt("upVotes") + 1);
                parseObject.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                        } else {

                        }
                    }


                });
            }
        });
    }
//    public void clickDown(View v) {
//        TextView upText = (TextView) findViewById(R.id.upTextView);
//        upText.setText(Integer.toString(Integer.parseInt(upText.getText().toString())-1));
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Waypoint");
//        query.getInBackground(postID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject parseObject, ParseException e) {
//                parseObject.put("downVotes", parseObject.getInt("downVotes")+1);
//                parseObject.saveInBackground(new ca;
//            }
//        });
//    }


}
