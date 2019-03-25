package hevs.it.SkiRunV2.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;
import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.UserEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseManager;

public class ClubActivity extends AppCompatActivity {

    // Variables
    private RecyclerView mRecyclerView;
    private ClubAdapter mClubAdapter;
    private FirebaseUser mFirebaseCurrentUser;
    private List<String> mClubList = new ArrayList<>();
    private UserEntity mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_clubs);

        // get current user
        mFirebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        // get all data of the current user
        FirebaseManager.getUser(mFirebaseCurrentUser.getUid(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                mCurrentUser = (UserEntity) o;
            }
        });

        // refresh the list of clubs from firebase
        refreshClubs();

        // create a linearLayoutManager for the list
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(recyclerLayoutManager);

        // create a DividerItemDecoration : divider between items of a LinearLayoutManage
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    // refresh the list of the clubs from firebase
    private void refreshClubs() {
        FirebaseManager.getClubsNames(new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                // get a list of clubs
                mClubList = (ArrayList<String>) o;
                // instanciate the club adapter with the list
                mClubAdapter = new ClubAdapter(mClubList, mCurrentUser);
                // set the adapter in the recycler view
                mRecyclerView.setAdapter(mClubAdapter);
            }
        });
    }
}
