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

import hevs.it.SkiRunV2.MainActivity;
import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.login.LoginActivity;
import hevs.it.SkiRunV2.login.ResetPasswordActivity;


public class SettingsFragment extends Fragment {

    private Button mEditProfileButton, mRoleButton, mClubButton, mLanguageButton, mLogoutButton;
    private View mView;
    private FirebaseUser mFirebaseCurrentUser;
    private TextView mEmail;
    private FirebaseAuth auth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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

    public void onPressEditButton (){

        mEditProfileButton = (Button) mView.findViewById(R.id.editProfileButton);

        mEditProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });
    }

    public void onChangePassword(){
        auth.signOut();
        startActivity(new Intent(getActivity(), ResetPasswordActivity.class));
        getActivity().finish();
    }

    public void onPressRoleButton(){
        mRoleButton = (Button) mView.findViewById(R.id.roleButton);

        mRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RoleActivity.class));
            }
        });
    }

    public void onPressClubButton(){
        mClubButton = (Button) mView.findViewById(R.id.clubButton);

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

    public void onPressLogoutButton(){
        mLogoutButton = (Button) mView.findViewById(R.id.logoutButton);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }
}
