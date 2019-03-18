package hevs.it.SkiRunV2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import hevs.it.SkiRunV2.entity.CompetitionEntity;
import hevs.it.SkiRunV2.entity.MissionEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseManager;

public class DashboardFragment extends Fragment {


    Spinner spCompetitions;
    Spinner spDisciplines;
    ListView lvMissions;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
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
        refreshCompetions();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        spCompetitions = (Spinner) getView().findViewById(R.id.sp_competion);
        spDisciplines = (Spinner) getView().findViewById(R.id.sp_discipline);
        lvMissions = (ListView) getView().findViewById(R.id.lv_missions);


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


    private void refreshCompetions() {
        FirebaseManager.getListCompetions(new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                ArrayList<String> competions = (ArrayList<String>) o;
                ArrayAdapter<String>  adapterCompetions = new ArrayAdapter<String>(DashboardFragment.this.getContext(), android.R.layout.simple_spinner_item, competions);
                spCompetitions.setAdapter(adapterCompetions);
            }
        });
    }

    private void refreshDisciplines() {
        FirebaseManager.getCompetition(spCompetitions.getSelectedItem().toString(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                CompetitionEntity competitionSelected = (CompetitionEntity) o;
                ArrayAdapter<String>  adapterDiscipline = new ArrayAdapter<String>(DashboardFragment.this.getContext(), android.R.layout.simple_spinner_item, competitionSelected.getListDiscipline());
                spDisciplines.setAdapter(adapterDiscipline);
            }
        });
    }

    private void refreshMissions() {
        FirebaseManager.getMissions(spCompetitions.getSelectedItem().toString(), spDisciplines.getSelectedItem().toString(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                ArrayAdapter<MissionEntity> adapterMission = new ArrayAdapter<MissionEntity>(DashboardFragment.this.getContext(), android.R.layout.simple_list_item_1, (List<MissionEntity>)o);
                lvMissions.setAdapter(adapterMission);
            }
        });
    }


}
