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

public class RoleActivity extends AppCompatActivity {

    private static final String TAG = "RoleActivity";

    // variables
    private RecyclerView mRecyclerView;
    private RoleAdapter mTypeJobAdapter;
    private FirebaseUser mFirebaseCurrentUser;
    private List<String> mTypeJobList = new ArrayList<>();
    private UserEntity mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_roles);

        // get current user
        mFirebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        // get all data of the current user
        FirebaseManager.getUser(mFirebaseCurrentUser.getUid(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                mCurrentUser = (UserEntity) o;
            }
        });

        // refresh the list of the types of jobs from firebase
        refreshTypeJob();

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
    private void refreshTypeJob() {
        FirebaseManager.getTypeJobs(new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                mTypeJobList = (ArrayList<String>) o;
                mTypeJobAdapter = new RoleAdapter(mTypeJobList, mCurrentUser);
                mRecyclerView.setAdapter(mTypeJobAdapter);
           }
        });
    }
}
