package com.brainifii.codine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;
import com.jediburrell.customfab.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity implements Dialog.DialogListener {
    RecyclerView mRecyclerView;
    DatabaseHelper databaseHelper;

//    private Button add;
    FloatingActionButton fabm,fabadd,fabbkp,fabbackup,fabrestore;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen= false;
    boolean bkp= false;
    boolean suStatus = false;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSU();
        bkpchk();

        mRecyclerView = findViewById(R.id.RecyclerView);
        databaseHelper = new DatabaseHelper(this);

        showRecord();

        fabm = findViewById(R.id.mainFab);
        fabadd = findViewById(R.id.addFab);
        fabbkp = findViewById(R.id.bkpFab);
        fabbackup = findViewById(R.id.bkp_fab_backup);
        fabrestore = findViewById(R.id.bkp_fab_restore);


        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                openDialog();
                animateFab();
            }
        });

        fabm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                animateFab();
            }
        });

        fabbkp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFabbkp();
                vibrate();
            }
        });

        fabbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFabbkp();
                vibrate();
                Essentials ess = new Essentials();
                String op = ess.backup();
                Toast.makeText(getApplicationContext(),"Output: "+op,Toast.LENGTH_LONG).show();
            }
        });

        fabrestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFabbkp();
                vibrate();
                Toast.makeText(getApplicationContext(),"Restore feature is not yet available.",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void bkpchk(){
        Essentials ess = new Essentials();
        boolean op = ess.filecheck();
        if(op){
            Toast.makeText(getApplicationContext(),"Backup Found." ,Toast.LENGTH_SHORT).show();
//            String res = ess.restore();
//            Toast.makeText(getApplicationContext(),"Restoring...: "+res ,Toast.LENGTH_LONG).show();

        }
    }

    private void checkSU(){
        Essentials essentials = new Essentials();
        try {
            CommandResult command_output = Shell.SU.run("su -v");
            String op = ""+command_output;
            if (op==""){
                suStatus=true;
                Toast.makeText(getApplicationContext(),"Root Mode disabled.",Toast.LENGTH_SHORT).show();
            }else {
                suStatus = false;
//                Toast.makeText(getApplicationContext(), "Root Mode enabled.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "ERROR: "+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void vibrate(){
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(50);
    }

    private void animateFab(){
        if(isOpen){
            fabm.startAnimation(rotateForward);
            fabadd.startAnimation(fabClose);
            fabbkp.startAnimation(fabClose);
            fabadd.setClickable(false);
            fabbkp.setClickable(false);
            isOpen=false;
            if(bkp){
                fabbackup.startAnimation(fabClose);
                fabrestore.startAnimation(fabClose);
                fabbackup.setClickable(false);
                fabrestore.setClickable(false);
                bkp=false;
            }
        }
        else {
            fabm.startAnimation(rotateBackward);
            fabadd.startAnimation(fabOpen);
            fabbkp.startAnimation(fabOpen);
            fabadd.setClickable(true);
            fabbkp.setClickable(true);
            isOpen=true;
        }
    }

    private void animateFabbkp(){
        if(bkp){
            fabm.startAnimation(rotateForward);
            fabbackup.startAnimation(fabClose);
            fabrestore.startAnimation(fabClose);
            fabbackup.setClickable(false);
            fabrestore.setClickable(false);
            fabadd.startAnimation(fabClose);
            fabbkp.startAnimation(fabClose);
            fabadd.setClickable(false);
            fabbkp.setClickable(false);
            bkp=false;
            isOpen=false;
        }
        else {
            fabbackup.startAnimation(fabOpen);
            fabrestore.startAnimation(fabOpen);
            fabbackup.setClickable(true);
            fabrestore.setClickable(true);
            bkp=true;
        }
    }

    private void showRecord() {

        Adapter adapter = new Adapter(MainActivity.this,databaseHelper.getAllData(Constants.C_ID+" DESC"));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showRecord();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
        }

        return super.onKeyDown(keyCode, event);
    }

    public void openDialog(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(),"Add dialog");

    }

    @Override
    public void applyText(String title, String desc, String commd) {
//        cmd_title.setText(title);
//        cmd_desc.setText(desc);
//        command.setText(commd);
    }
}
