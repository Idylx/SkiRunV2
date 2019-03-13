package hevs.it.SkiRunV2.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hevs.it.SkiRunV2.entity.ClubEntity;
import hevs.it.SkiRunV2.entity.CompetitionEntity;
import hevs.it.SkiRunV2.entity.TypeJobEntity;
import hevs.it.SkiRunV2.entity.UserEntity;

public class FirebaseManager {

    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public static void getUser(String email, final FirebaseCallBack firebaseCallBack) {
        //Get the entity Person from an email
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_USERS).child(email);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserEntity user = dataSnapshot.getValue(UserEntity.class);
                firebaseCallBack.onCallBack(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static void getClubs(final FirebaseCallBack firebaseCallBack) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_CLUBS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();

                List<ClubEntity> clubs = new ArrayList<ClubEntity>();
                for (Object obj : objectMap.values()) {
                    if (obj instanceof Map) {
                        Map<String, Object> mapObj = (Map<String, Object>) obj;
                        ClubEntity club = new ClubEntity();
                        club.setName((String) mapObj.get("name"));
                        clubs.add(club);
                    }
                }
                firebaseCallBack.onCallBack(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static void getTypeJobs(final FirebaseCallBack firebaseCallBack) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_TYPEJOBS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();

                List<TypeJobEntity> jobs = new ArrayList<TypeJobEntity>();
                for (Object obj : objectMap.values()) {
                    if (obj instanceof Map) {
                        Map<String, Object> mapObj = (Map<String, Object>) obj;
                        TypeJobEntity job = new TypeJobEntity();
                        job.setName((String) mapObj.get("name"));
                        jobs.add(job);
                    }
                }
                firebaseCallBack.onCallBack(jobs);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static void getCompetition(String name, final FirebaseCallBack firebaseCallBack) {
        //Get the entity Competition from an name
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_COMPETITIONS).child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CompetitionEntity competition = dataSnapshot.getValue(CompetitionEntity.class);
                firebaseCallBack.onCallBack(competition);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
