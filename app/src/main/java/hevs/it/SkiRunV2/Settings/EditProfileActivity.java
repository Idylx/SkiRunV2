package hevs.it.SkiRunV2.Settings;

import android.content.Intent;
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
import hevs.it.SkiRunV2.models.User;

public class EditProfileActivity extends AppCompatActivity {

    private Button mButtonOk;
    private FirebaseUser mFirebaseCurrentUser;
    private UserEntity mCurrentUser;
    private EditText mEmail_EditText;
    private EditText mLastname_EditText;
    private EditText mFirstname_EditText;
    private EditText mPhone_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // find the id of edit text
        mEmail_EditText = findViewById(R.id.edit_email_editText);
        mLastname_EditText = findViewById(R.id.edit_lastname_editText);
        mFirstname_EditText = findViewById(R.id.edit_firstname_editText);
        mPhone_EditText = findViewById(R.id.edit_phone_editText);

        // get current user
        mFirebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        // get all data of the current user
        FirebaseManager.getUser(mFirebaseCurrentUser.getUid(), new FirebaseCallBack() {
            @Override
            public void onCallBack(Object o) {
                mCurrentUser = (UserEntity) o;
                Log.i("email current user is", mCurrentUser.getEmail());
                mEmail_EditText.setText(mCurrentUser.getEmail());
                mLastname_EditText.setText(mCurrentUser.getLastname());
                mFirstname_EditText.setText(mCurrentUser.getFirstname());
                mPhone_EditText.setText(mCurrentUser.getPhone());
            }
        });

        try {
            onPressOkButton();
        }catch (NullPointerException e){
            Toast.makeText(this, R.string.retry, Toast.LENGTH_SHORT).show();
        }

    }

    public void saveEditedUserOnFirebase(){

        // get the edited text
        mCurrentUser.setEmail(mEmail_EditText.getText().toString());
        mCurrentUser.setLastname(mLastname_EditText.getText().toString());
        mCurrentUser.setFirstname(mFirstname_EditText.getText().toString());
        mCurrentUser.setPhone(mPhone_EditText.getText().toString());

        // add it to firebase
        FirebaseUserManager.updateUser(mCurrentUser);

    }

    public void onPressOkButton(){

        mButtonOk = (Button) findViewById(R.id.edit_button_ok);

        mButtonOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveEditedUserOnFirebase();
                finish();
            }
        });
    }
}
