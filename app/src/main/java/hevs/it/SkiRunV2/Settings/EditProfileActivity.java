package hevs.it.SkiRunV2.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.UserEntity;
import hevs.it.SkiRunV2.firebase.FirebaseCallBack;
import hevs.it.SkiRunV2.firebase.FirebaseManager;
import hevs.it.SkiRunV2.firebase.FirebaseUserManager;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivityr";

    private Button mButtonOk;
    private FirebaseUser mFirebaseCurrentUser;
    private UserEntity mCurrentUser;
    private EditText mLastname_EditText;
    private EditText mFirstname_EditText;
    private EditText mPhone_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // find the id of edit text
        mLastname_EditText = findViewById(R.id.edit_lastname_editText);
        mFirstname_EditText = findViewById(R.id.edit_firstname_editText);
        mPhone_EditText = findViewById(R.id.edit_phone_editText);

        // get current user
        mFirebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

       try {
           // get all data of the current user
           FirebaseManager.getUser(mFirebaseCurrentUser.getUid(), new FirebaseCallBack() {
               @Override
               public void onCallBack(Object o) {
                   mCurrentUser = (UserEntity) o;
                   Log.i("email current user is", mCurrentUser.getEmail());

                   // set the edit text with the current data
                   mLastname_EditText.setText(mCurrentUser.getLastName());

                   // set the edit text with the current data
                   mFirstname_EditText.setText(mCurrentUser.getFirstName());

                   // set the edit text with the current data
                   mPhone_EditText.setText(mCurrentUser.getPhone());
               }
           });

       }catch (NullPointerException e){
           Toast.makeText(this, R.string.plzEnterData, Toast.LENGTH_SHORT).show();
       }

        try {
            // the user press on 'ok' --> he wants to save all the changes
            onPressOkButton();
        }catch (NullPointerException e){
            Toast.makeText(this, R.string.retry, Toast.LENGTH_SHORT).show();
        }
    }

    // the user press on 'ok'
    public void onPressOkButton(){

        mButtonOk = (Button) findViewById(R.id.edit_button_ok);

        mButtonOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // check that all the fields are not empty
                if (!mLastname_EditText.getText().toString().isEmpty()&&
                        !mFirstname_EditText.getText().toString().isEmpty() &&
                        !mPhone_EditText.getText().toString().isEmpty()){

                    // save the edited user in firebase
                    saveEditedUserOnFirebase();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), R.string.fieldsEmpty, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // save the user and his new data in firebase
    public void saveEditedUserOnFirebase(){

        // get the edited text
        mCurrentUser.setLastName(mLastname_EditText.getText().toString());
        mCurrentUser.setFirstName(mFirstname_EditText.getText().toString());
        mCurrentUser.setPhone(mPhone_EditText.getText().toString());

        // add it to firebase
        FirebaseUserManager.updateUser(mCurrentUser);
    }
}
