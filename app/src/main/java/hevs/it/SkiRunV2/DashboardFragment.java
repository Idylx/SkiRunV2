package hevs.it.SkiRunV2;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    // array of adapters
    ArrayAdapter<String>  adapterCompetions;
    ArrayAdapter<String>  adapterDiscipline;
    ArrayAdapter<MissionEntity> adapterMission;

    public DashboardFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        competitionSelected = new CompetitionEntity();
        competions = new ArrayList<>();

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

        // listener on  spinner competition
        spCompetitions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // refresh disciplines
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

                MissionEntity clickedMission = (MissionEntity) lvMissions.getItemAtPosition(position);
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

                //TODO next step when starting the mission
                builder.setPositiveButton("Start Mission",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //do when hidden
            refreshMissions();
        } else {
            //do when show
            refreshMissions();
        }
    }

    // refresh competition
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

                    List<MissionEntity> myMissions = new ArrayList<MissionEntity>();
                    for(MissionEntity mission :  (List<MissionEntity>)o){
                        for(String uid : mission.getSelecteds()){
                            if(uid.equals(FirebaseAuth.getInstance().getUid())) {
                                myMissions.add(mission);
                                break;
                            }
                        }
                    }
                    adapterMission = new ArrayAdapter<MissionEntity>(getContext(),  R.layout.custom_textview, myMissions);
                    adapterMission.notifyDataSetChanged();
                    lvMissions.setAdapter(adapterMission);
                }
            });
        }
        catch (NullPointerException e){
            Log.println(1, "DashboardFragment", e.getMessage());
        }
        catch (IndexOutOfBoundsException e ){
            Log.println(1, "DashboardFragment", e.getMessage());
        }
    }


}
