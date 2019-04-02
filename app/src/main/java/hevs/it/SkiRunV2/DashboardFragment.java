package hevs.it.SkiRunV2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hevs.it.SkiRunV2.entity.CompetitionEntity;
import hevs.it.SkiRunV2.entity.MissionEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseManager;
import hevs.it.SkiRunV2.missions.LambdaMissions;
import hevs.it.SkiRunV2.missions.TimekeeperMissions;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    // spinner
    Spinner spCompetitions;
    Spinner spDisciplines;

    // list view
    ListView lvMissions;

    // competition
    CompetitionEntity competitionSelected;

    // array of competitions
    ArrayList<String> competions;
    ArrayList<MissionEntity> myMissions;

    // array of adapters
    ArrayAdapter<String>  adapterCompetions;
    ArrayAdapter<String>  adapterDiscipline;
    ArrayAdapter<MissionEntity> adapterMission;

    public DashboardFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        competitionSelected = new CompetitionEntity();
        competions = new ArrayList<>();
        myMissions = new ArrayList<>();

        // refresh the competitions
        refreshCompetions();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // instanciate the adapters
        adapterCompetions = new ArrayAdapter<String>(getContext(), R.layout.custom_textview, competions);
        adapterDiscipline = new ArrayAdapter<String>(getContext(), R.layout.custom_textview, competitionSelected.getListDiscipline());
        adapterMission = new ArrayAdapter<MissionEntity>(getContext(), R.layout.custom_textview, myMissions );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        spCompetitions = (Spinner) getView().findViewById(R.id.sp_competion);
        spDisciplines = (Spinner) getView().findViewById(R.id.sp_discipline);
        lvMissions = (ListView) getView().findViewById(R.id.lv_missions);

        // set the adapters
        spCompetitions.setAdapter(adapterCompetions);
        spDisciplines.setAdapter(adapterDiscipline);
        lvMissions.setAdapter(adapterMission);

        // listener on  spinner competition
        spCompetitions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshDisciplines();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // listener on the spinner disciplines
        spDisciplines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshMissions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // listener on the mission
        lvMissions.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final MissionEntity clickedMission = (MissionEntity) lvMissions.getItemAtPosition(position);
                //Open DetailsBookActivity - Pass some values to get the details of the book
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardFragment.this.getContext(), R.style.AlertDialog);

                //Set title for AlertDialog
                builder.setTitle(clickedMission.getMissionName());

                //Set body message of Dialog
                builder.setMessage(clickedMission.getDescription()+" \n"+
                        "Date:  " +DateFormat.format("dd/MM/yy", new Date(clickedMission.getStartTime()*1000)).toString()+" \n"+
                        "Start: " +DateFormat.format("hh:mm", new Date(clickedMission.getStartTime()*1000)).toString()+" \n"+
                        "End:   " +DateFormat.format("hh:mm", new Date(clickedMission.getEndTime()*1000)).toString()+" \n");

                //Dismiss when touching outside
                builder.setCancelable(true);
                builder.setPositiveButton("Start Mission",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (clickedMission.getTypeJob().contains("TimeKeeper")){
                                    Intent intentTimekeeper = new Intent(getContext(), TimekeeperMissions.class);

                                    intentTimekeeper.putExtra("missionName", clickedMission.getMissionName());
                                    intentTimekeeper.putExtra("competition", spCompetitions.getSelectedItem().toString());
                                    intentTimekeeper.putExtra("discipline", spDisciplines.getSelectedItem().toString());
                                    startActivity(intentTimekeeper);
                                }
                                else{
                                    Intent intentLambda = new Intent(getContext(), LambdaMissions.class);

                                    intentLambda.putExtra("missionName", clickedMission.getMissionName());
                                    intentLambda.putExtra("competition", spCompetitions.getSelectedItem().toString());
                                    intentLambda.putExtra("discipline", spDisciplines.getSelectedItem().toString());
                                    startActivity(intentLambda);
                                }

                            }
                        });

                //
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void refreshCompetions() {
        try {
            FirebaseManager.getListCompetions(new FirebaseCallBack() {
                @Override
                public void onCallBack(Object o) {
                    competions = (ArrayList<String>) o;
                    adapterCompetions.clear();
                    adapterCompetions.addAll(competions);
                    adapterCompetions.notifyDataSetChanged();
                }
            });
        }
        catch (NullPointerException e){
            Log.println(1, "DashboardFragment", e.getMessage());
        }
    }

    // refresh descipline
    private void refreshDisciplines() {
        try {
                FirebaseManager.getCompetition(spCompetitions.getSelectedItem().toString(), new FirebaseCallBack() {
                @Override
                public void onCallBack(Object o) {
                    competitionSelected = (CompetitionEntity) o;
                    adapterDiscipline.clear();
                    adapterDiscipline.addAll(competitionSelected.getListDiscipline());
                    adapterDiscipline.notifyDataSetChanged();
                    refreshMissions();
                }
            });
        }catch (NullPointerException e){
            Log.println(1, "DashboardFragment", e.getMessage());
        }
    }

    // refresh missions
    private void refreshMissions() {

        try{
            FirebaseManager.getMissions(spCompetitions.getSelectedItem().toString(),
                    spDisciplines.getSelectedItem().toString(), new FirebaseCallBack() {
                @Override
                public void onCallBack(Object o) {

                    List<MissionEntity> myMissions = new ArrayList<>();
                    for(MissionEntity mission :  (List<MissionEntity>)o){
                        for(String uid : mission.getSelecteds()){
                            if(uid.equals(FirebaseAuth.getInstance().getUid())) {
                                myMissions.add(mission);
                                break;
                            }
                        }
                    }
                    adapterMission.clear();
                    adapterMission.addAll(myMissions);
                    adapterMission.notifyDataSetChanged();
                }
            });
        }
        catch (NullPointerException e){
            Log.e(TAG, e.getMessage());
        }
        catch (IndexOutOfBoundsException e ){
            Log.e(TAG, e.getMessage());
        }
    }


}
