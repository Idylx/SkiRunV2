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

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubsViewHolder>  {

    private static final String TAG = "ClubAdapter";

    // variables
    private List<String> mClubList;
    private UserEntity mCurrentUser;
    private String mClubName;
    private int lastSelectedPosition = -1;

    // Constructors
    public ClubAdapter(List<String> mClubsDataSet, UserEntity mCurrentUser){
        this.mClubList = mClubsDataSet;
        this.mCurrentUser = mCurrentUser;
    }

    @NonNull
    @Override
    public ClubsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.clubs_recycler_view, parent, false);
        return new ClubsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubsViewHolder holder, int position) {

        // get the club name
        mClubName = mClubList.get(position);
        // set the text of the radio button with the club name
        holder.mRadioBtnNameClub.setText(mClubName);
        // Get the current club
        holder.mCurrentClub = mClubList.get(position);
        // if the name of the club is the same name of the club of the user
        if (mClubName.equals(mCurrentUser.getClub())){
            // check the radiobutton
            holder.mRadioBtnNameClub.setChecked(true);
            lastSelectedPosition= position;
        }
        else {
            // uncheck the radiobutton
            holder.mRadioBtnNameClub.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        // size of the list of club
        return mClubList.size();
    }

    // Class View Holder
    public class ClubsViewHolder extends RecyclerView.ViewHolder {

        // variables
        public RadioButton mRadioBtnNameClub;
        public String mCurrentClub;

        public ClubsViewHolder(View v) {
            super(v);
            mRadioBtnNameClub =  (RadioButton) v.findViewById(R.id.club_radioButton);

            mRadioBtnNameClub.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    // get the position of the adapter
                    lastSelectedPosition = getAdapterPosition();
                    mClubName = mCurrentClub;
                    // set the user with the new club
                    mCurrentUser.setClub(mClubName);
                    // add it to firebase
                    FirebaseUserManager.updateUser(mCurrentUser);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
