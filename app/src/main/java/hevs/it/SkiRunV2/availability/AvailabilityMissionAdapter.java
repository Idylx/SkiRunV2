package hevs.it.SkiRunV2.availability;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.MissionEntity;

public class AvailabilityMissionAdapter extends RecyclerView.Adapter<AvailabilityMissionViewHolder> {

    private static final String TAG = "AvailabilityMissionAdapter";

    // get list of mission
    private List<MissionEntity> missions;

    // current settings to get the path
    private String currentCompetition;
    private String currentDiscipline;

    //constructor
    AvailabilityMissionAdapter(List<MissionEntity> missions) {
        this.missions = missions;
    }


    @Override
    public AvailabilityMissionViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        // Inflate the layout for this recycler view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.availability_missions_recyclerview, parent, false);

        return new AvailabilityMissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AvailabilityMissionViewHolder holder, int i) {

        // bind data to the holder
        holder.missionName.setText(missions.get(i).getMissionName());
        holder.missionTime.setText(DateFormat.format("hh:mm", new Date(missions.get(i).getStartTime()*1000)).toString()+" "+DateFormat.format("hh:mm", new Date(missions.get(i).getEndTime()*1000)).toString());
        holder.currentMissionName = missions.get(i).getMissionName();
        holder.currentCompetition = currentCompetition;
        holder.currentDiscipline = currentDiscipline;

        holder.selectionMission.setChecked(checkSubscrubed(i));
    }

    @Override
    public int getItemCount() {
        return missions.size();
    }


    // setter
    void setCurrentCompetition(String currentCompetition) {
        this.currentCompetition = currentCompetition;
    }

    void setCurrentDiscipline(String currentDiscipline) {
        this.currentDiscipline = currentDiscipline;
    }

    //update logic
    void update(List<MissionEntity> newMission){
        missions.clear();
        missions.addAll(newMission);
        notifyDataSetChanged();
    }

    boolean checkSubscrubed(int i) {
        for (String s : missions.get(i).getSubscribed()) {
            if (s.equals(FirebaseAuth.getInstance().getUid()))
                return true;
            else
               return false;
        }
        return false;
    }
}
