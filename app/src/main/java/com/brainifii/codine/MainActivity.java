package com.brainifii.codine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;
import com.jediburrell.customfab.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements Dialog.DialogListener {
private static final int REQUEST_CODE = 123;

    RecyclerView mRecyclerView;
    DatabaseHelper databaseHelper;
    Intent intent;
    String backup_path = null;

//    private Button add;
    FloatingActionButton fabm,fabadd,fabbkp,fabbackup,fabrestore;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen= false;
    boolean bkp= false;
    public static boolean suStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSU();
        grantpermission();

        SharedPreferences pref = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = pref.getBoolean("firstStart", true);
        if(firstStart){
            bkpchk();
        }

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
                if(!checkpermission()) {
                    grantpermission();
                }
                try {
                    backup();
                }catch (Exception e){

                    Toast.makeText(getApplicationContext(),"Error: Storage permission denied.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabrestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFabbkp();
                vibrate();
                if(!checkpermission()) {
                    grantpermission();
                }
                try {
                    restore();
                }catch (Exception e){

                    Toast.makeText(getApplicationContext(),"Error: Storage permission denied.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:

                if (resultCode == RESULT_OK) {
                    assert data != null;
                    backup_path = Objects.requireNonNull(data.getData()).getPath();
                }

                break;
        }
    }

    private void restore(){
        String src = Environment.getExternalStorageDirectory() + "/Codine_files/"+Constants.DB_BACKUP_NAME;
        String des = Environment.getDataDirectory() + "/data/" + BuildConfig.APPLICATION_ID + "/databases/" + Constants.DB_NAME;
        try {
            copyFile(src,des);
            restart();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error restoring: " + e,Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(),"Restore successful",Toast.LENGTH_SHORT).show();

    }

    private void restart(){
        finish();
        startActivity(getIntent());
    }

    private void backup(){
        String src = Environment.getDataDirectory() + "/data/" + BuildConfig.APPLICATION_ID + "/databases/" + Constants.DB_NAME;
        String des = "/storage/emulated/0/Codine_files/"+Constants.DB_BACKUP_NAME;
        try {
            copyFile(src,des);
            Toast.makeText(getApplicationContext(),"Backup created successfully at "+des,Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void copyFile(String Source,String Destination) throws IOException {
        File srcFile = new File(Source);
        File desFile = new File(Destination);
        if (!desFile.exists()){
            desFile.createNewFile();
        }

        FileInputStream in = null;
        FileOutputStream out = null;
        FileChannel inc = null;
        FileChannel outc = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(desFile);
            inc = in.getChannel();
            outc = out.getChannel();
            long count = 0;
            long size = inc.size();
            while (count < size) {
                count += outc.transferFrom(inc, 0, size - count);
            }
        }finally {
            assert in != null;
            in.close();
            assert out != null;
            out.close();
            assert inc != null;
            inc.close();
            assert outc != null;
            outc.close();
        }
    }

    private void bkpchk(){
        if (checkpermission()) {
            Essentials ess = new Essentials();
            boolean op = ess.filecheck();
            if (op) {
                Toast.makeText(getApplicationContext(), "Backup Found.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.DialogTheme);
                alert.setTitle("Backup found");
                alert.setMessage("Do you want to restore previous Configs ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restore();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create().show();
            }
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }
    }

    private void checkSU(){
        try {
            CommandResult command_output = Shell.SU.run("su -v");
            String op = ""+command_output;
            if (op.equals("")){
                suStatus=true;
                Toast.makeText(getApplicationContext(),"Root Mode disabled.",Toast.LENGTH_SHORT).show();
            }else {
                suStatus = false;
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "ERROR: "+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void vibrate(){
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        assert vib != null;
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

        Adapter adapter;
        adapter = new Adapter(MainActivity.this,databaseHelper.getAllData(Constants.C_ID+" DESC"));
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

    }


    public boolean checkpermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            //When Permission not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                grantpermission();
                Toast.makeText(getApplicationContext(),"Storage Permission Denied",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void grantpermission(){
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CODE
        );
    }


}
