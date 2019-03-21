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



    Spinner spCompetitions;
    Spinner spDisciplines;
    List<MissionEntity> missions;

    AvailabilityMissionAdapter adapter;
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;

    private static final String TAG = "AvailabilityFragment";


    public AvailabilityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshCompetitions();
        missions = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_availability, container, false);

        rv = (RecyclerView) view.findViewById(R.id.RecyclerViewAvailability);
        layoutManager = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(layoutManager);
        adapter = new AvailabilityMissionAdapter(missions, getContext());
        rv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        spCompetitions = (Spinner) getView().findViewById(R.id.spinner_competition);
        spDisciplines = (Spinner) getView().findViewById(R.id.spinner_discipline);

        spCompetitions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        FirebaseManager.getListCompetions(new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                ArrayList<String> competitions = (ArrayList<String>) o;
                ArrayAdapter<String> adapterCompetions = new ArrayAdapter<String>(AvailabilityFragment.this.getContext(),R.layout.custom_textview, competitions);
                spCompetitions.setAdapter(adapterCompetions);
            }
        });
    }

    private void refreshDisciplines() {
        FirebaseManager.getCompetition(spCompetitions.getSelectedItem().toString(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                CompetitionEntity competitionSelected = (CompetitionEntity) o;
                ArrayAdapter<String> adapterDiscipline = new ArrayAdapter<String>(AvailabilityFragment.this.getContext(), R.layout.custom_textview, competitionSelected.getListDiscipline());
                spDisciplines.setAdapter(adapterDiscipline);
            }
        });
    }


    private void refreshMissions() {
        FirebaseManager.getMissions(spCompetitions.getSelectedItem().toString(), spDisciplines.getSelectedItem().toString(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                List<MissionEntity> missions = (List<MissionEntity>) o;
                adapter.setCurrentCompetition(spCompetitions.getSelectedItem().toString());
                adapter.setCurrentDiscipline(spDisciplines.getSelectedItem().toString());
                adapter.update(missions);
            }
        });
    }


}
