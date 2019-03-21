package hevs.it.SkiRunV2.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.login.LoginActivity;


public class SettingsFragment extends Fragment {

    private Button mEditProfileButton, mRoleButton, mClubButton, mLanguageButton, mLogoutButton;
    private View mView;
    private FirebaseUser mFirebaseCurrentUser;
    private TextView mEmail;
    private FirebaseAuth auth;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_settings, container, false);

        // get the current user
        mFirebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        // display the email in the settings fragment
        mEmail = mView.findViewById(R.id.username);
        mEmail.setText( "Username : " + mFirebaseCurrentUser.getEmail());

        // button 'Edit profile'
        onPressEditButton();

        // button 'Change password'
        onChangePassword();

        // button 'Role'
        onPressRoleButton();

        // button 'Club'
        onPressClubButton();

        // button 'language'
        onPressLanguageButton();

        // button 'logout'
        onPressLogoutButton();

        // Inflate the layout for this fragment
        return mView;
    }

    // button 'edit profile'
    public void onPressEditButton (){

        mEditProfileButton = (Button) mView.findViewById(R.id.editProfileButton);

        // open the activity edit profile
        mEditProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });
    }

    // button 'change password'
    public void onChangePassword(){

    }

    // button 'role
    public void onPressRoleButton(){
        mRoleButton = (Button) mView.findViewById(R.id.roleButton);

        // start the role activity
        mRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RoleActivity.class));
            }
        });
    }

    // button 'club'
    public void onPressClubButton(){
        mClubButton = (Button) mView.findViewById(R.id.clubButton);

        // start the club activity
        mClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ClubActivity.class));
            }
        });
    }

    public void onPressLanguageButton(){
        mLanguageButton = (Button) mView.findViewById(R.id.languageButton);

        mLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LanguageActivity.class));
            }
        });
    }

    // button 'logout'
    public void onPressLogoutButton(){
        mLogoutButton = (Button) mView.findViewById(R.id.logoutButton);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logout the current user
                auth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }
}
