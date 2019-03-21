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
                user.setUid(dataSnapshot.getKey());
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

                List<String> jobs = new ArrayList<String>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    jobs.add(childDataSnapshot.getKey());
                }

                firebaseCallBack.onCallBack(jobs);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static void getListCompetions(final FirebaseCallBack firebaseCallBack) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_COMPETITIONS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> competitons = new ArrayList<String>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    competitons.add(childDataSnapshot.getKey());
                }
                firebaseCallBack.onCallBack(competitons);
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
                competition.setRefApi((String)dataSnapshot.child(FirebaseSession.NODE_REFAPI).getValue());
                Log.i("FBCB", competition.getStartDate()+"");
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
                firebaseCallBack.onCallBack(competition);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public static void getMissions(String competiton, String disciplines, final FirebaseCallBack firebaseCallBack) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_COMPETITIONS).child(competiton).child(FirebaseSession.NODE_DISCIPLINES).child(disciplines);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<MissionEntity> missions = new ArrayList<MissionEntity>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    MissionEntity mission = new MissionEntity();
                    mission.setMissionName(childDataSnapshot.getKey());
                    mission.setDescription((String)childDataSnapshot.child(FirebaseSession.NODE_DESCRIPTION).getValue());
                    mission.setDoors((String)childDataSnapshot.child(FirebaseSession.NODE_DOOR).getValue());
                    mission.setStartTime((long)childDataSnapshot.child(FirebaseSession.NODE_STARTDT).getValue());
                    mission.setEndTime((long)childDataSnapshot.child(FirebaseSession.NODE_ENDDT).getValue());
                    mission.setNbrPeople((((long) childDataSnapshot.child(FirebaseSession.NODE_NB).getValue())));
                    mission.setTypeJob((String)childDataSnapshot.child(FirebaseSession.NODE_TYPEOFJOB).getValue());

                    List<String> subs = new ArrayList<String>();
                    for(DataSnapshot childDataSnapshotSub : childDataSnapshot.child(FirebaseSession.NODE_SUBSCRIBED).getChildren()){
                        subs.add(childDataSnapshotSub.getKey());
                    };
                    mission.setSubscribeds(subs);

                    List<String> select = new ArrayList<String>();
                    for(DataSnapshot childDataSnapshotSel : childDataSnapshot.child(FirebaseSession.NODE_SELECTED).getChildren()){
                        select.add(childDataSnapshotSel.getKey());
                    };
                    mission.setSelecteds(select);


                    missions.add(mission);
                }
                firebaseCallBack.onCallBack(missions);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
