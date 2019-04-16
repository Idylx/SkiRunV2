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

public class TimekeeperMissions extends AppCompatActivity {



    private static final String TAG = "TimeKeeperMissions";

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

        loadMission();

        onOkPressd();
        onDNFPressed();
    }


    public void loadMission() {

        try {
            FirebaseMissionManager.getMission(competitionName, disciplineName, missionName,
                    new FirebaseCallBack() {
                        @Override
                        public void onCallBack(Object o) {

                            mission = (MissionEntity) o;


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

    public void changeResultText() {
        switch(mission.getTypeJob()){

            case "TimeKeeper - distance":
                resultTextView.setText(R.string.distance);
                resultEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                resultType = "Meter";
                break;
            case "TimeKeeper - time":
                resultTextView.setText(R.string.time);
                resultEditText.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                resultType = "Min, sec";
                break;
            case "TimeKeeper - vitesse":
                resultTextView.setText(R.string.speed);
                resultEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                resultType = "Km/h";
                break;
            default:
                resultTextView.setText(R.string.results);
                resultEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                resultType = "null";
                break;

        }
    }

    public void onOkPressd(){
        okButton = (Button)findViewById(R.id.validateResultButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (TextUtils.isEmpty(bipNumberEditText.getText())) {
                    Toast.makeText(getApplicationContext(), R.string.enterNumber, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(resultEditText.getText())) {
                    Toast.makeText(getApplicationContext(), R.string.enterResult, Toast.LENGTH_SHORT).show();
                    return;
                }
                ResultEntity resultEntity  = new ResultEntity();
                resultEntity.setBibNumber(Integer.parseInt(bipNumberEditText.getText().toString()));
                resultEntity.setResult(resultEditText.getText().toString());
                resultEntity.setUnit(resultType);

                try {
                    FirebaseResultManager.updateResult(competitionName, disciplineName, missionName, resultEntity);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), R.string.somethingbad, Toast.LENGTH_SHORT).show();
                    Log.wtf(TAG,e.getMessage());
                    return;
                }
                bipNumberEditText.setText("");
                resultEditText.setText("");
                Toast.makeText(getApplicationContext(), R.string.okbutton, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDNFPressed(){
        dnfButton = (Button)findViewById(R.id.dnfbutton);
        dnfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(bipNumberEditText.getText())) {
                    Toast.makeText(getApplicationContext(), R.string.enterNumber, Toast.LENGTH_SHORT).show();
                    return;
                }
                ResultEntity resultEntity  = new ResultEntity();
                resultEntity.setBibNumber(Integer.parseInt(bipNumberEditText.getText().toString()));
                resultEntity.setResult("DNF");
                resultEntity.setUnit(resultType);
                try {
                    FirebaseResultManager.updateResult(competitionName, disciplineName, missionName, resultEntity);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), R.string.somethingbad, Toast.LENGTH_SHORT).show();
                    Log.wtf(TAG,e.getMessage());
                }
                bipNumberEditText.setText("");
                resultEditText.setText("");
                Toast.makeText(getApplicationContext(), R.string.okbutton, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
