package hevs.it.SkiRunV2.missions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.MissionEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseMissionManager;

public class DoorControllerMissions extends AppCompatActivity {
    private static final String TAG = "DoorController";

    MissionEntity mission;
    TextView dateTextView, descriptionTextView;
    Button cameraButton;
    Intent i;
    String competitionName, disciplineName, missionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_controller_missions);

        dateTextView = (TextView)findViewById(R.id.dateTextViewDoorController);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextViewDoorController);

        cameraButton = (Button)findViewById(R.id.beginvideorecorderButton);

        i = getIntent();

        competitionName= i.getStringExtra("competition");
        disciplineName = i.getStringExtra("discipline");
        missionName = i.getStringExtra("missionName");

        loadMission();
        pressCameraButton();
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

    public void pressCameraButton(){

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DoorControllerCamera.class);
                try {
                    i.putExtra("competition", competitionName);
                    i.putExtra("discipline", disciplineName);
                    i.putExtra("missionName", missionName);
                    i.putExtra("location", mission.getLocation());
                    startActivity(i);
                }catch (NullPointerException e ){
                    Toast.makeText(DoorControllerMissions.this, R.string.somethingbad, Toast.LENGTH_SHORT);
                }
            }
        });
    }
}