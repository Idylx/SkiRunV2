package hevs.it.SkiRunV2.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMissionManager {


    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public static void updateMission(String competiton, String disciplines, String mission) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_COMPETITIONS).child(competiton).child(FirebaseSession.NODE_DISCIPLINES).child(disciplines).child(mission).child(FirebaseSession.NODE_SUBSCRIBED);
        ref.child(FirebaseSession.UID_USER).setValue(true);
    }

    public static void removeSubscriberMission(String competiton, String disciplines, String mission) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_COMPETITIONS).child(competiton).child(FirebaseSession.NODE_DISCIPLINES).child(disciplines).child(mission).child(FirebaseSession.NODE_SUBSCRIBED);
        ref.child(FirebaseSession.UID_USER).removeValue();
    }

}
