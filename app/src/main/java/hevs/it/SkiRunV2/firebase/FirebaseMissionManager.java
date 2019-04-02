package hevs.it.SkiRunV2.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hevs.it.SkiRunV2.entity.MissionEntity;

public class FirebaseMissionManager {


    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private static final String TAG = "MissionManager";

    //update the subscription with the current user
    public static void updateSubscriberMission(String competiton, String disciplines, String mission) {
        try{
            if(mission.equals("Null"))
                Log.d(TAG, "NULL RECEIVE, DO NOTHING");
            else{
                DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_COMPETITIONS).child(competiton).child(FirebaseSession.NODE_DISCIPLINES).child(disciplines).child(mission).child(FirebaseSession.NODE_SUBSCRIBED);
                ref.child(FirebaseAuth.getInstance().getUid()).setValue(true);
            }
        }
        catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

    }

    //remove the subscription with the current user
    public static void removeSubscriberMission(String competiton, String disciplines, String mission) {
        try{
            if(mission.equals("Null"))
                Log.d(TAG, "NULL RECEIVE, DO NOTHING");
            else{
                DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_COMPETITIONS).child(competiton).child(FirebaseSession.NODE_DISCIPLINES).child(disciplines).child(mission).child(FirebaseSession.NODE_SUBSCRIBED);
                ref.child(FirebaseAuth.getInstance().getUid()).removeValue();
            }
        }
        catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    public static void getMission(String competiton, String discipline, String missionName,final FirebaseCallBack firebaseCallBack) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_COMPETITIONS).child(competiton).child(FirebaseSession.NODE_DISCIPLINES).child(discipline);
        ref.child(missionName).addValueEventListener(new ValueEventListener() {
            MissionEntity mission = new MissionEntity();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                mission.setMissionName(dataSnapshot.getKey());
                try {
                    mission.setDescription((String) dataSnapshot.child(FirebaseSession.NODE_DESCRIPTION).getValue());
                    mission.setLocation((String) dataSnapshot.child(FirebaseSession.NODE_LOCATION).getValue());
                    mission.setStartTime((long) dataSnapshot.child(FirebaseSession.NODE_STARTDT).getValue());
                    mission.setEndTime((long) dataSnapshot.child(FirebaseSession.NODE_ENDDT).getValue());
                    mission.setNbrPeople((((long) dataSnapshot.child(FirebaseSession.NODE_NB).getValue())));
                    mission.setTypeJob((String) dataSnapshot.child(FirebaseSession.NODE_TYPEOFJOB).getValue());
                } catch (NullPointerException e) {
                    mission.setMissionName("Null");
                    mission.setDescription("Null");
                    mission.setLocation("null");
                    mission.setStartTime(0L);
                    mission.setEndTime(0L);
                    mission.setNbrPeople(0L);
                    mission.setTypeJob("");
                    Log.wtf(TAG, e.getMessage());
                }

                List<String> subs = new ArrayList<String>();
                for (DataSnapshot childDataSnapshotSub : dataSnapshot.child(FirebaseSession.NODE_SUBSCRIBED).getChildren()) {
                    subs.add(childDataSnapshotSub.getKey());
                }
                mission.setSubscribeds(subs);

                List<String> select = new ArrayList<String>();
                for (DataSnapshot childDataSnapshotSel : dataSnapshot.child(FirebaseSession.NODE_SELECTED).getChildren()) {
                    select.add(childDataSnapshotSel.getKey());
                }
                mission.setSelecteds(select);
                firebaseCallBack.onCallBack(mission);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.wtf(TAG, "I don't know why but something went wrong, here is the exception :"+ databaseError.getMessage());
            }
        });

    }
}
