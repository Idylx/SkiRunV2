package hevs.it.SkiRunV2.missions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.MissionEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseMissionManager;

public class LambdaMissions extends AppCompatActivity {

    private static final String TAG = "LambdaMissions";

    MissionEntity mission;
    TextView dateTextView, descriptionTextView;
    Intent i;
    String competitionName, disciplineName, missionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lambda_missions);

        dateTextView = (TextView)findViewById(R.id.dateTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        i = getIntent();
        competitionName=i.getStringExtra("competition");
        disciplineName = i.getStringExtra("discipline");
        missionName = i.getStringExtra("location");

        loadMission();
    }


    public void loadMission() {

        try {
            FirebaseMissionManager.getMission(competitionName, disciplineName , missionName,
                    new FirebaseCallBack() {
                        @Override
                        public void onCallBack(Object o) {

                            mission = (MissionEntity)o;


                            dateTextView.setText(DateFormat.format("dd/MM/yy hh:mm", new Date(mission.getStartTime()*1000)).toString() +
                                    " - " + DateFormat.format("hh:mm", new Date(mission.getEndTime()*1000)).toString());
                            descriptionTextView.setText(mission.getDescription());
                            getSupportActionBar().setTitle(mission.getMissionName());

                        }
                    });


        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
