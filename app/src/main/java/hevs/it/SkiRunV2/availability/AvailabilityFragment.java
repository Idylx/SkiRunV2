package hevs.it.SkiRunV2.availability;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private static final String TAG = "AvailabilityFragment";

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

    CompetitionEntity competitionSelected;
    ArrayList<String> competions;
    ArrayAdapter<String> adapterCompetions;
    ArrayAdapter<String> adapterDiscipline;

    public AvailabilityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        competitionSelected = new CompetitionEntity();
        competions = new ArrayList<>();

        // initialize the list of mission
        missions = new ArrayList<>();

        //get every competition on the creation of the fragment
        refreshCompetitions();
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
        adapterCompetions = new ArrayAdapter<String>(this.getContext(), R.layout.custom_textview, competions);
        adapterDiscipline = new ArrayAdapter<String>(getContext(), R.layout.custom_textview, competitionSelected.getListDiscipline());
        adapter = new AvailabilityMissionAdapter(missions);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // set spinner from view
        spCompetitions = (Spinner) getView().findViewById(R.id.spinner_competition);
        spDisciplines = (Spinner) getView().findViewById(R.id.spinner_discipline);

        spCompetitions.setAdapter(adapterCompetions);
        spDisciplines.setAdapter(adapterDiscipline);
        rv.setAdapter(adapter);

        // when the user click on the spinner competition
        spCompetitions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //refresh Discipline with the selected competition
                refreshDisciplines();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing selected
            }
        });

        // when the user press on the spinner discipline
        spDisciplines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // refresh the missions
                refreshMissions();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // refresh competitions
    private void refreshCompetitions() {
        //get current competition
        FirebaseManager.getListCompetions(new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                competions = (ArrayList<String>) o;
                // clear the adapter
                adapterCompetions.clear();
                // add all the competitions
                adapterCompetions.addAll(competions);
                // notify changes
                adapterCompetions.notifyDataSetChanged();
            }
        });
    }

    // refresh the disciplines
    private void refreshDisciplines() {
        try {
            // get current discipline
            FirebaseManager.getCompetition(spCompetitions.getSelectedItem().toString(), new FirebaseCallBack() {
                @Override
                public void onCallBack(Object o) {
                    competitionSelected = (CompetitionEntity) o;
                    // clear the adapter
                    adapterDiscipline.clear();
                    // add all the competitions selected
                    adapterDiscipline.addAll(competitionSelected.getListDiscipline());
                    // notify changes
                    adapterDiscipline.notifyDataSetChanged();
                    // refresh the missions
                    refreshMissions();
                }
            });
        }catch (NullPointerException e){
            Log.println(1, "AvailabilityFragment", e.getMessage());
        }
    }

    // refresh missions
    private void refreshMissions() {
        try {
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
        catch (NullPointerException e){
            Log.i(TAG, e.getMessage());
        }
        catch (IndexOutOfBoundsException eI){
            Log.i(TAG, eI.getMessage());
            spDisciplines.setSelection(0);
        }
    }
}
