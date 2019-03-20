package hevs.it.SkiRunV2.availability;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.MissionEntity;
import hevs.it.SkiRunV2.firebase.FirebaseSession;

public class AvailabilityMissionAdapter extends RecyclerView.Adapter<AvailabilityMissionViewHolder> {

    private List<MissionEntity> missions;
    private Context context;

    private String currentCompetition;
    private String currentDiscipline;

    public AvailabilityMissionAdapter(List<MissionEntity> missions, Context context) {
        this.missions = missions;
        this.context = context;
    }

    @Override
    public AvailabilityMissionViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.availability_missions_recyclerview, parent, false);

        return new AvailabilityMissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AvailabilityMissionViewHolder holder, int i) {

        holder.missionName.setText(missions.get(i).getMissionName());
        holder.missionTime.setText(DateFormat.format("hh:mm", new Date(missions.get(i).getStartTime()*1000)).toString()+" "+DateFormat.format("hh:mm", new Date(missions.get(i).getEndTime()*1000)).toString());
        holder.mission = missions.get(i);
        holder.currentCompetition = currentCompetition;
        holder.currentDiscipline = currentDiscipline;


        for (String s : missions.get(i).getSubscribed()) {
            if (s.equals(FirebaseSession.UID_USER))
                holder.selectionMission.setChecked(true);
            else
                holder.selectionMission.setChecked(false);
        }

    }
    @Override
    public int getItemCount() {
        return missions.size();
    }



    public void setCurrentCompetition(String currentCompetition) {
        this.currentCompetition = currentCompetition;
    }

    public void setCurrentDiscipline(String currentDiscipline) {
        this.currentDiscipline = currentDiscipline;
    }

    public void update(List<MissionEntity> newMission){
        missions.clear();
        missions.addAll(newMission);
        notifyDataSetChanged();
    }
}
