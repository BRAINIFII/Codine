package com.pocohuter.codine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditRecordActivity extends AppCompatActivity {

    private EditText pTitle,pDesc,pCommd;
    private Button editBtn;

    private String id,title,desc,commd;
    private boolean editMode = false;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        pTitle = findViewById(R.id.edit_rec_commandtitle);
        pDesc = findViewById(R.id.edit_rec_commanddesc);
        pCommd = findViewById(R.id.edit_rec_command);

        editBtn = findViewById(R.id.edit_button);

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode",editMode);
        id = intent.getStringExtra("ID");
        title = intent.getStringExtra("TITLE");
        desc = intent.getStringExtra("DESC");
        commd = intent.getStringExtra("COMMD");

        pTitle.setText(title);
        pDesc.setText(desc);
        pCommd.setText(commd);

        dbHelper = new DatabaseHelper(this);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                assert vib != null;
                vib.vibrate(50);
                getData();
                Toast.makeText(EditRecordActivity.this,"Updated record \nID: "+id,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditRecordActivity.this,MainActivity.class));
            }
        });
    }

    private void getData(){
        String title = ""+pTitle.getText().toString().trim();
        String desc = ""+pDesc.getText().toString().trim();
        String commd = ""+pCommd.getText().toString().trim();

        if (editMode){
            dbHelper.updateInfo(
                    ""+id,
                    ""+title,
                    ""+desc,
                    ""+commd
            );
        }

        Log.d("getData","Record added to id: "+id);



//        Intent intent = new Intent(getContext(),MainActivity.class);
//        intent.putExtra("editMode",false);
//        startActivity(intent);

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
