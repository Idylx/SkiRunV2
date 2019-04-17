package hevs.it.SkiRunV2.missions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.MissionEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseMissionManager;

// all the 'others' missions
public class LambdaMissions extends AppCompatActivity {

    private static final String TAG = "LambdaMissions";

    // variables
    MissionEntity mission;
    TextView dateTextView, descriptionTextView, locationTextView;
    Intent i;
    String competitionName, disciplineName, missionName;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lambda_missions);

        // get the text view with id
        dateTextView = (TextView)findViewById(R.id.dateTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextViewLambada);
        doneButton = (Button) findViewById(R.id.doneButton);

        i = getIntent();

        // get current competition, discipline and mission
        competitionName=i.getStringExtra("competition");
        disciplineName = i.getStringExtra("discipline");
        missionName = i.getStringExtra("missionName");

        // load mission
        loadMission();

        // when click on done button
        onDonePressed();
    }

    // load mission
    public void loadMission() {

        try {
            // call firebase manager and get the mission
            FirebaseMissionManager.getMission(competitionName, disciplineName , missionName,
                    new FirebaseCallBack() {
                        @Override
                        public void onCallBack(Object o) {
                            // create a mission
                            mission = (MissionEntity)o;

                            // set the text view with current data
                            dateTextView.setText(DateFormat.format("dd/MM/yy hh:mm", new Date(mission.getStartTime()*1000)).toString() +
                                    " - " + DateFormat.format("hh:mm", new Date(mission.getEndTime()*1000)).toString());
                            descriptionTextView.setText(mission.getDescription());
                            locationTextView.setText(mission.getLocation());
                            getSupportActionBar().setTitle(mission.getMissionName());
                        }
                    });
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    // when the done button is pressed
    public void onDonePressed(){

        // finish the current view
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
