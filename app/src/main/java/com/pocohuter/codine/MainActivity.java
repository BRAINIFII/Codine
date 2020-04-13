package com.pocohuter.codine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.jediburrell.customfab.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements Dialog.DialogListener {
    RecyclerView mRecyclerView;
    DatabaseHelper databaseHelper;

//    private Button add;
    FloatingActionButton fab;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.RecyclerView);
        databaseHelper = new DatabaseHelper(this);

        showRecord();

        fab = findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);
                openDialog();
            }
        });

//        add = (Button) findViewById(R.id.add);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDialog();
//            }
//        });
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
