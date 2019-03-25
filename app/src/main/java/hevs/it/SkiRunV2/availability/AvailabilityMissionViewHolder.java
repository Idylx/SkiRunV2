package hevs.it.SkiRunV2.availability;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.firebase.FirebaseMissionManager;

public class AvailabilityMissionViewHolder
        extends RecyclerView.ViewHolder implements View.OnClickListener{

    //items of one row
    TextView missionName, missionTime;
    CheckBox selectionMission;

    public String currentMissionName;

    //current settings of missions
    String currentCompetition;
    String currentDiscipline;

    //constructor
    AvailabilityMissionViewHolder(View itemView) {
        super(itemView);
        missionName =(TextView) itemView.findViewById(R.id.AvailibilityMissionName);
        missionTime =(TextView) itemView.findViewById(R.id.AvailibilityMissionTime);
        selectionMission =(CheckBox) itemView.findViewById(R.id.AvailibilityMissionCheckBox);

        itemView.setOnClickListener(this);

        //set listener to the checkbox
        checkLogic();
    }

    @Override
    public void onClick(View v) {

        checkLogic();
    }



    private void checkLogic(){
        //checkbox click event handling
        selectionMission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // if changed on the check box set the subscription of the user
                if (isChecked) {
                    FirebaseMissionManager.updateSubscriberMission(currentCompetition,  currentDiscipline, currentMissionName);
                } else {
                    FirebaseMissionManager.removeSubscriberMission(currentCompetition,  currentDiscipline, currentMissionName);
                }
            }
        });

    }
}
