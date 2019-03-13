package hevs.it.SkiRunV2.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

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
import hevs.it.SkiRunV2.entity.DisciplineEntity;
import hevs.it.SkiRunV2.entity.MissionEntity;
import hevs.it.SkiRunV2.entity.TypeJobEntity;
import hevs.it.SkiRunV2.entity.UserEntity;

public class FirebaseManager {

    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public static void getUser(String uid, final FirebaseCallBack firebaseCallBack) {
        //Get the entity Person from an email
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_USERS).child(uid);
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
                List<ClubEntity> clubs = new ArrayList<ClubEntity>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    ClubEntity club = new ClubEntity();
                    club.setName(childDataSnapshot.getKey());
                    clubs.add(club);
                }
                firebaseCallBack.onCallBack(clubs);
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

                List<TypeJobEntity> jobs = new ArrayList<TypeJobEntity>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    TypeJobEntity job = new TypeJobEntity();
                    job.setName(childDataSnapshot.getKey());
                    jobs.add(job);
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
                CompetitionEntity competition = new CompetitionEntity();
                competition.setName(dataSnapshot.getKey());
                competition.setStartDate((long)dataSnapshot.child(FirebaseSession.NODE_STARTDATE).getValue());
                competition.setEndDate((long)dataSnapshot.child(FirebaseSession.NODE_ENDDATE).getValue());
                competition.setRefApi(((String)dataSnapshot.child(FirebaseSession.NODE_REFAPI).getValue()));

                List<ClubEntity> clubs = new ArrayList<ClubEntity>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.child(FirebaseSession.NODE_GUESTCLUBS).getChildren()) {
                    ClubEntity club = new ClubEntity();
                    club.setName(childDataSnapshot.getKey());
                    clubs.add(club);
                }

                List<DisciplineEntity> disciplines = new ArrayList<DisciplineEntity>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.child(FirebaseSession.NODE_DISCIPLINES).getChildren()) {
                    DisciplineEntity discipline = new DisciplineEntity();
                    discipline.setName(childDataSnapshot.getKey());
                    disciplines.add(discipline);
                }



                competition.setGuestsClub(clubs);
                competition.setDisciplines(disciplines);
                Log.i("TestCallback", ""+competition.getDisciplines().size());
                Log.i("TestCallback", competition.toString());

                //CompetitionEntity competition = dataSnapshot.getValue(CompetitionEntity.class);
                //firebaseCallBack.onCallBack(competition);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
