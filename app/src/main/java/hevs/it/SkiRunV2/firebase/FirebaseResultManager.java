package hevs.it.SkiRunV2.firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hevs.it.SkiRunV2.entity.ResultEntity;

public class FirebaseResultManager {

    private static final String TAG = "FirebaseManager";

    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    //update the subscription with the current user
    public static void updateResult(String competiton, String disciplines, String mission, ResultEntity resultEntity) {
        try{
            if(mission.equals("Null")|| competiton.equals("Null") ||disciplines.equals("NUll"))
                Log.d(TAG, "NULL RECEIVE, DO NOTHING");
            else{
                DatabaseReference ref = mFirebaseDatabase.getReference().child(FirebaseSession.NODE_COMPETITIONS).child(competiton).child(FirebaseSession.NODE_DISCIPLINES).child(disciplines).child(mission).child(FirebaseSession.NODE_RESULTS);
                ref.child(Integer.toString(resultEntity.getBibNumber())).setValue(resultEntity,  new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError error, DatabaseReference reference) {
                        try {
                            throw new Exception(error.getMessage());
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    };
                });
            }
        }
        catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

    }
}
