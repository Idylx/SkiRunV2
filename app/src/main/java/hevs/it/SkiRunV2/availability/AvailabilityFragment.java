package hevs.it.SkiRunV2.availability;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.CompetitionEntity;
import hevs.it.SkiRunV2.entity.MissionEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseManager;


public class AvailabilityFragment extends Fragment {


    //Spinner
    Spinner spCompetitions;
    Spinner spDisciplines;

    //all mission
    List<MissionEntity> missions;

    //custom adapter for the list in the recycler
    AvailabilityMissionAdapter adapter;

    //Recycler view variables
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;

    ArrayAdapter<String> adapterCompetions;
    ArrayAdapter<String> adapterDiscipline;


    public AvailabilityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get every competition on the creation of the fragment
        refreshCompetitions();

        // initialize the list of mission
        missions = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_availability, container, false);

        // bind the recyclerView and set the layout for the list
        rv = (RecyclerView) view.findViewById(R.id.RecyclerViewAvailability);
        layoutManager = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(layoutManager);

        //set the adapter to the recycler view
        adapter = new AvailabilityMissionAdapter(missions);
        rv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // set spinner from view
        spCompetitions = (Spinner) getView().findViewById(R.id.spinner_competition);
        spDisciplines = (Spinner) getView().findViewById(R.id.spinner_discipline);


        spCompetitions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //refresh Discipline with the selected competition
                refreshDisciplines();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDisciplines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshMissions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void refreshCompetitions() {
        //get current competition
        FirebaseManager.getListCompetions(new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                ArrayList<String> competitions = (ArrayList<String>) o;
                adapterCompetions = new ArrayAdapter<String>(getContext(),R.layout.custom_textview, competitions);
                spCompetitions.setAdapter(adapterCompetions);
            }
        });
    }


    private void refreshDisciplines() {
        // get current discipline
        FirebaseManager.getCompetition(spCompetitions.getSelectedItem().toString(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                CompetitionEntity competitionSelected = (CompetitionEntity) o;
                adapterDiscipline = new ArrayAdapter<String>(getContext(), R.layout.custom_textview, competitionSelected.getListDiscipline());
                spDisciplines.setAdapter(adapterDiscipline);
            }
        });
    }


    private void refreshMissions() {
        //get current missions
        FirebaseManager.getMissions(spCompetitions.getSelectedItem().toString(), spDisciplines.getSelectedItem().toString(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                List<MissionEntity> missions = (List<MissionEntity>) o;
                //set selection on the adapter
                adapter.setCurrentCompetition(spCompetitions.getSelectedItem().toString());
                adapter.setCurrentDiscipline(spDisciplines.getSelectedItem().toString());
                // update list
                adapter.update(missions);
            }
        });
    }


}
