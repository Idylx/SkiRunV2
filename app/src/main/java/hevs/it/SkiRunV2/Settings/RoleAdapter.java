package hevs.it.SkiRunV2.Settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.UserEntity;
import hevs.it.SkiRunV2.firebase.FirebaseUserManager;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RolesViewHolder> {

    private List<String> mTypeJobList;
    private UserEntity mCurrentUser;
    private String typeJobName;

    public RoleAdapter(List<String> mRolesDataSet, UserEntity mCurrentUser){
        this.mTypeJobList = mRolesDataSet;
        this.mCurrentUser = mCurrentUser;
    }

    public class RolesViewHolder extends RecyclerView.ViewHolder {

        public RadioButton mRadioBtnNameRole;
        public String mCurrentTypeJob;

        public RolesViewHolder(View v) {
            super(v);
            mRadioBtnNameRole =  (RadioButton) v.findViewById(R.id.role_radioButton);

            mRadioBtnNameRole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkLogic();
                    notifyDataSetChanged();
                }
            });
        }

        public void checkLogic(){
            //checkbox click event handling
            mRadioBtnNameRole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){
                        typeJobName = mCurrentTypeJob;
                        mCurrentUser.setJobPreference(typeJobName);
                        // add it to firebase
                        FirebaseUserManager.updateUser(mCurrentUser);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RolesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.roles_recycler_view, parent, false);

        return new RolesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RolesViewHolder holder, int position) {
        typeJobName = mTypeJobList.get(position);
        holder.mRadioBtnNameRole.setText(typeJobName);
        holder.mCurrentTypeJob = mTypeJobList.get(position);

        if (typeJobName.equals(mCurrentUser.getJobPreference())){
            holder.mRadioBtnNameRole.setChecked(true);
        }
        else {
            holder.mRadioBtnNameRole.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mTypeJobList.size();
    }

}
