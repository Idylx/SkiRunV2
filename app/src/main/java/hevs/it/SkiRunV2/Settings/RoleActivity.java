package hevs.it.SkiRunV2.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.TypeJobEntity;
import hevs.it.SkiRunV2.entity.UserEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseManager;
import hevs.it.SkiRunV2.firebase.FirebaseUserManager;

public class RoleActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RolesAdapter mTypeJobAdapter;
    private FirebaseUser mFirebaseCurrentUser;
    private List<String> mTypeJobList = new ArrayList<>();
    private RadioButton mRadioButtonJobName;
    private UserEntity mCurrentUser;
    private Button mButtonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        mRadioButtonJobName = (RadioButton) findViewById(R.id.role_radioButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_roles);
        mButtonOk = (Button) findViewById(R.id.role_button_ok);

        // get current user
        mFirebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        // get all data of the current user
        FirebaseManager.getUser(mFirebaseCurrentUser.getUid(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                mCurrentUser = (UserEntity) o;
            }
        });

        refreshTypeJob();

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        onPressOkButton();
    }


    private void refreshTypeJob() {
        FirebaseManager.getTypeJobs(new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                mTypeJobList = (ArrayList<String>) o;
                mTypeJobAdapter = new RolesAdapter(mTypeJobList);
                mRecyclerView.setAdapter(mTypeJobAdapter);
                mTypeJobAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onPressOkButton(){

        mButtonOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                mCurrentUser.setJobPreference("Logistics");

                // add it to firebase
                FirebaseUserManager.updateUser(mCurrentUser);
                finish();
                }
        });
    }



}
