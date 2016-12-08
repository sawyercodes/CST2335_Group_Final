package com.cst2335_group_final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLaunchAutomobile = (Button)findViewById(R.id.btnLaunchAutomobile);
        btnLaunchAutomobile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AutoMenuListActivity.class);
                startActivity(intent);
            }
        });

    }

    Button btnLaunchAutomobile;
}
