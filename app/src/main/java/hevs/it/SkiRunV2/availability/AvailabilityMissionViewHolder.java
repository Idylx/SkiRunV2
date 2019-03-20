package hevs.it.SkiRunV2.availability;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import hevs.it.SkiRunV2.R;

public class AvailabilityMissionViewHolder
    extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView missionName, missionTime;
    public CheckBox selectionMission;


    public AvailabilityMissionViewHolder(View itemView) {
        super(itemView);
        missionName =(TextView) itemView.findViewById(R.id.AvailibilityMissionName);
        missionTime =(TextView) itemView.findViewById(R.id.AvailibilityMissionTime);
        selectionMission =(CheckBox) itemView.findViewById(R.id.AvailibilityMissionCheckBox);

        itemView.setOnClickListener(this);

        //checkbox click event handling
        selectionMission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
