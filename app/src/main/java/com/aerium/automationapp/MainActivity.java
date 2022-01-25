package com.aerium.automationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

public class MainActivity extends AppCompatActivity {
    //Making objects for the Text and Button element
    TextView txtView1, txtView2, txtView3, masterSwitch;
    Switch s1,s2,s3,s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting reference to our database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        //To create the connection to the root to the database tree
        DatabaseReference smartAc = myRef.child("SmartAc").child("status");

        //Instantiate the created objects
        txtView1 = (TextView) findViewById(R.id.textView1);
        //txtView2 = (TextView) findViewById(R.id.textView2);
        //txtView3 = (TextView) findViewById(R.id.textView3);
        masterSwitch = (TextView) findViewById(R.id.master_switch); //To check whether masterSwitch is ON or OFF.
        s1 = (Switch) findViewById(R.id.switch1);
        //s2 = (Switch) findViewById(R.id.switch2);
        //s3 = (Switch) findViewById(R.id.switch3);
        s4 = (Switch) findViewById(R.id.switch4);

        //Grabbing database from firebase by using firebase API, Two methods
        smartAc.addValueEventListener(new ValueEventListener() {
            boolean firstTime = true;
            @Override
            //The if-else condition check inside onDataChange is to check for initial state of master
            // switch4 on app startup and allow s1 t0 toggle only if master switch is on
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                txtView1.setText("SmartAC " + value);
                if (firstTime){
                    if (value.equals("ON")){
                        s4.setChecked(true);
                        s1.setChecked(true);
                        firstTime=false;
                    }else{
                        s1.setChecked(false);
                        firstTime=false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("file", "fails to read the value.", error.toException());
            }
        });

        //update the database whenever we toggle any of the switch.
        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (s4.isChecked())
                    if (s1.isChecked())
                        smartAc.setValue("ON");
                    else
                        smartAc.setValue("OFF");
                    else
                        if (s1.isChecked())
                            s1.setChecked(false);
                Toast.makeText(getApplicationContext(),"Enable master switch", Toast.LENGTH_SHORT).show();
            }
        });

        s4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!s4.isChecked()){
                    s1.setChecked(false);
                    s2.setChecked(false);
                    s3.setChecked(false);
                    smartAc.setValue("OFF");
                    masterSwitch.setText("Master Switch Off");
                }else{
                    masterSwitch.setText("MAster Switch On");
                }
            }
        });
    }
}