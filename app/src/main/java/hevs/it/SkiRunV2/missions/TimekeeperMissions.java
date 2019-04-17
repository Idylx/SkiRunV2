package hevs.it.SkiRunV2.missions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.MissionEntity;
import hevs.it.SkiRunV2.entity.ResultEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseMissionManager;
import hevs.it.SkiRunV2.firebase.FirebaseResultManager;

// Time Keeper mission
public class TimekeeperMissions extends AppCompatActivity {

    private static final String TAG = "TimeKeeperMissions";

    // variables
    MissionEntity mission;
    TextView dateTextView, descriptionTextView, resultTextView, locationTextView;
    EditText bipNumberEditText, resultEditText;
    Button okButton, dnfButton;
    Intent i;
    String competitionName, disciplineName, missionName, resultType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timekeeper_missions);

        // get text view with id
        dateTextView = (TextView) findViewById(R.id.dateTextViewTimeKeeper);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextViewTimeKeeper);
        resultTextView = (TextView)findViewById(R.id.resultsTextview);
        locationTextView = (TextView) findViewById(R.id.locationTextViewTimeKeeper);

        bipNumberEditText = (EditText)findViewById(R.id.bibNumberEditText);
        resultEditText = (EditText)findViewById(R.id.resultEditText);


        i = getIntent();
        competitionName = i.getStringExtra("competition");
        disciplineName = i.getStringExtra("discipline");
        missionName = i.getStringExtra("missionName");

        // load the mission
        loadMission();

        // managment of button ok
        onOkPressd();

        // managment of DNF button
        onDNFPressed();
    }

    // load the mission
    public void loadMission() {

        try {
            // call firebase method get mission
            FirebaseMissionManager.getMission(competitionName, disciplineName, missionName,
                    new FirebaseCallBack() {
                        @Override
                        public void onCallBack(Object o) {

                            // Create a new mission
                            mission = (MissionEntity) o;

                            // Set data with current mission
                            dateTextView.setText(DateFormat.format("dd/MM/yy hh:mm", new Date(mission.getStartTime() * 1000)).toString() +
                                    " - " + DateFormat.format("hh:mm", new Date(mission.getEndTime() * 1000)).toString());
                            descriptionTextView.setText(mission.getDescription());
                            locationTextView.setText(mission.getLocation());
                            getSupportActionBar().setTitle(mission.getMissionName());
                            changeResultText();
                        }
                    });
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    // change the label depending on the type of job
    public void changeResultText() {
        switch(mission.getTypeJob()){
            // TimeKeeper - distance
            case "TimeKeeper - distance":
                // set text to distance
                resultTextView.setText(R.string.distance);
                resultEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                // for the distance we need meter
                resultType = "Meter";
                break;
            // TimeKeeper - time
            case "TimeKeeper - time":
                // set text to time
                resultTextView.setText(R.string.time);
                resultEditText.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                // with time keeper we need min and sec
                resultType = "Min, sec";
                break;
            // TimeKeeper - vitesse
            case "TimeKeeper - vitesse":
                // set text to speed
                resultTextView.setText(R.string.speed);
                resultEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                // for timekeeper speed we need Km/h
                resultType = "Km/h";
                break;
            // Default
            default:
                resultTextView.setText(R.string.results);
                resultEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                resultType = "null";
                break;

        }
    }

    // when the user press on ok
    public void onOkPressd(){
        okButton = (Button)findViewById(R.id.validateResultButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if it is empty
                if (TextUtils.isEmpty(bipNumberEditText.getText())) {
                    Toast.makeText(getApplicationContext(), R.string.enterNumber, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(resultEditText.getText())) {
                    Toast.makeText(getApplicationContext(), R.string.enterResult, Toast.LENGTH_SHORT).show();
                    return;
                }
                // create a new result and get all informations
                ResultEntity resultEntity  = new ResultEntity();
                resultEntity.setBibNumber(Integer.parseInt(bipNumberEditText.getText().toString()));
                resultEntity.setResult(resultEditText.getText().toString());
                resultEntity.setUnit(resultType);

                try {
                    // update the result in firebase manager with the current data
                    FirebaseResultManager.updateResult(competitionName, disciplineName, missionName, resultEntity);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), R.string.somethingbad, Toast.LENGTH_SHORT).show();
                    Log.wtf(TAG,e.getMessage());
                    return;
                }
                // set to "" the bib number and the result
                bipNumberEditText.setText("");
                resultEditText.setText("");
                Toast.makeText(getApplicationContext(), R.string.okbutton, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // when the user press on DNF button
    public void onDNFPressed(){
        dnfButton = (Button)findViewById(R.id.dnfbutton);
        dnfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if it is empty
                if (TextUtils.isEmpty(bipNumberEditText.getText())) {
                    Toast.makeText(getApplicationContext(), R.string.enterNumber, Toast.LENGTH_SHORT).show();
                    return;
                }
                // create a new result and get the data
                ResultEntity resultEntity  = new ResultEntity();
                resultEntity.setBibNumber(Integer.parseInt(bipNumberEditText.getText().toString()));
                resultEntity.setResult("DNF");
                resultEntity.setUnit(resultType);
                try {
                    // update the result in firebase manager with current data
                    FirebaseResultManager.updateResult(competitionName, disciplineName, missionName, resultEntity);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), R.string.somethingbad, Toast.LENGTH_SHORT).show();
                    Log.wtf(TAG,e.getMessage());
                }
                // set the bib number and the result to ""
                bipNumberEditText.setText("");
                resultEditText.setText("");
                Toast.makeText(getApplicationContext(), R.string.okbutton, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
