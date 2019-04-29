package hevs.it.SkiRunV2.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hevs.it.SkiRunV2.entity.UserEntity;

public class FirebaseUserManager{

    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    // update the user
    public static void updateUser(UserEntity userEntity) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_USERS);
        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userEntity);
    }
}
