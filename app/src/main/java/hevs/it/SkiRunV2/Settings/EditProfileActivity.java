package hevs.it.SkiRunV2.Settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hevs.it.SkiRunV2.R;

public class EditProfileActivity extends AppCompatActivity {

    private Button mButtonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        onPressOkButton();

    }


    public void onPressOkButton(){
        mButtonOk = (Button) findViewById(R.id.edit_button_ok);

        mButtonOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
    }
}
