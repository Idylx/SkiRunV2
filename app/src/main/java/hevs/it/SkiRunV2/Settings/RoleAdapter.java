package hevs.it.SkiRunV2.Settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import java.util.List;
import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.UserEntity;
import hevs.it.SkiRunV2.firebase.FirebaseUserManager;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RolesViewHolder> {

    private static final String TAG = "RoleAdapter";

    // variables
    private List<String> mTypeJobList;
    private UserEntity mCurrentUser;
    private String typeJobName;
    private int lastSelectedPosition = -1;

    // constructor
    public RoleAdapter(List<String> mRolesDataSet, UserEntity mCurrentUser){
        this.mTypeJobList = mRolesDataSet;
        this.mCurrentUser = mCurrentUser;

    }

    @NonNull
    @Override
    public RolesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.roles_recycler_view, parent, false);
        return new RolesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RolesViewHolder holder, int position) {
        // get type job name
        typeJobName = mTypeJobList.get(position);
        // set the text of the radio button with a type of job
        holder.mRadioBtnNameRole.setText(typeJobName);
        // get current type of job
        holder.mCurrentTypeJob = mTypeJobList.get(position);
        // if type of job is equals of the type of job of the current user
        if (typeJobName.equals(mCurrentUser.getJobPreference())){
            // check the radio button
            holder.mRadioBtnNameRole.setChecked(true);
            lastSelectedPosition= position;
        }
        else {
            // uncheck the radio button
            holder.mRadioBtnNameRole.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        // return the size of the list
        return mTypeJobList.size();
    }

    // class view Holder
    public class RolesViewHolder extends RecyclerView.ViewHolder {

        // Variables
        public RadioButton mRadioBtnNameRole;
        public String mCurrentTypeJob;

        // constructor
        public RolesViewHolder(View v) {
            super(v);
            mRadioBtnNameRole =  (RadioButton) v.findViewById(R.id.role_radioButton);
            mRadioBtnNameRole.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    // get the last position of the adapter
                    lastSelectedPosition = getAdapterPosition();
                    typeJobName = mCurrentTypeJob;
                    // set the new type of job of the current user
                    mCurrentUser.setJobPreference(typeJobName);
                    // add it to firebase
                    FirebaseUserManager.updateUser(mCurrentUser);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
