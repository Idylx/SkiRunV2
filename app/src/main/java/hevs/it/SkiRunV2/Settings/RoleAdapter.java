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

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RolesViewHolder> {

    private List<String> mTypeJobList;
    private int lastSelectedPosition = -1;
    private UserEntity mCurrentUser;
    private String typeJobName;

    public RoleAdapter(List<String> mRolesDataSet){
        this.mTypeJobList = mRolesDataSet;
    }

    public class RolesViewHolder extends RecyclerView.ViewHolder {

        public RadioButton mRadioBtnNameRole;

        public RolesViewHolder(View v) {
            super(v);
            mRadioBtnNameRole =  (RadioButton) v.findViewById(R.id.role_radioButton);

            mRadioBtnNameRole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
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
        holder.mRadioBtnNameRole.setChecked(lastSelectedPosition == position);

        /*
        if (typeJobName.equals("Door Controller")){
            holder.mRadioBtnNameRole.setChecked(true);
        }else {
            holder.mRadioBtnNameRole.setChecked(false);
        }
        */

    }

    @Override
    public int getItemCount() {
        return mTypeJobList.size();
    }





}
