package com.pocohuter.codine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class AddRecordActivity extends AppCompatActivity {

    private static final int STORAGE_REQUEST_CODE = 101;

    private String[] storagePermissions;

    private String title,desc,command;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


    }

    private boolean checkStoragePermission(){

        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);

    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        // this function moves our addrecord activity to mainactivity when back button pressed
//        onBackPressed();
//        return super.onSupportNavigateUp();
//    }
}
