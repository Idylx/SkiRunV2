package hevs.it.SkiRunV2.availability;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.firebase.FirebaseMissionManager;

public class AvailabilityMissionViewHolder
        extends RecyclerView.ViewHolder implements View.OnClickListener{

    private static final String TAG = "AvailabilityMissionViewHolder";

    //items of one row
    TextView missionName, missionTime;
    CheckBox selectionMission;

    public String currentMissionName;

    List<String> listSubscribers = new ArrayList<>();
    //current settings of missions
    String currentCompetition;
    String currentDiscipline;

    //constructor
    AvailabilityMissionViewHolder(View itemView) {
        super(itemView);
        missionName =(TextView) itemView.findViewById(R.id.AvailibilityMissionName);
        missionTime =(TextView) itemView.findViewById(R.id.AvailibilityMissionTime);
        selectionMission =(CheckBox) itemView.findViewById(R.id.AvailibilityMissionCheckBox);

        //desactivate the click, UX reason, so clicking on the check will call the on click of the row
        selectionMission.setClickable(false);
        //set listener to the checkbox
        itemView.setOnClickListener(this);
    }

    // check if the user is in the list if subscibed
    public void setCheckSubscribed(){
        for (String s : listSubscribers){
            if (s.equals(FirebaseAuth.getInstance().getUid())){
                selectionMission.setChecked(true);
            }
        }
    }


    @Override
    public void onClick(View v) {
        // if the checkbox is not currently set update it on firebase
        if(!selectionMission.isChecked()){
            selectionMission.setChecked(true);
            FirebaseMissionManager.updateSubscriberMission(currentCompetition,currentDiscipline, currentMissionName);
        }else{
            selectionMission.setChecked(false);
            FirebaseMissionManager.removeSubscriberMission(currentCompetition,currentDiscipline, currentMissionName);
        }
    }
}
