package com.brainifii.codine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class EditRecordActivitybkp extends AppCompatDialogFragment {
    private EditText maincommandtitle;
    private EditText maincommanddesc;
    private EditText maincommand;
    private EditText editcommandtitle;
    private EditText editcommanddesc;
    private EditText editcommand;
    private DialogListener listener;
    private DatabaseHelper dbHelper;
    private String id,title,desc,commd;
    private boolean editMode = false;


    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),R.style.DialogTheme);

        dbHelper = new DatabaseHelper(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_add_record,null);



        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = maincommandtitle.getText().toString();
                        String desc = maincommanddesc.getText().toString();
                        String commd = maincommand.getText().toString();
                        listener.applyText(title,desc,commd);
                        getData();
                        startActivity(new Intent(getContext(),MainActivity.class));
                        Toast.makeText(getContext(),"Updated Successfully...!!!",Toast.LENGTH_SHORT).show();
                    }
                });

        maincommandtitle = view.findViewById(R.id.add_rec_commandtitle);
        maincommanddesc = view.findViewById(R.id.add_rec_commanddesc);
        maincommand = view.findViewById(R.id.add_rec_command);
        editcommandtitle = view.findViewById(R.id.edit_rec_commandtitle);
        editcommanddesc = view.findViewById(R.id.edit_rec_commanddesc);
        editcommand = view.findViewById(R.id.edit_rec_command);


        Intent intent = getActivity().getIntent();
        editMode = intent.getBooleanExtra("editMode",editMode);
        id = intent.getStringExtra("ID");
        title = intent.getStringExtra("TITLE");
        desc = intent.getStringExtra("DESC");
        commd = intent.getStringExtra("COMMD");

        if (editMode){
            builder.setTitle("Edit");

            editMode = intent.getBooleanExtra("editMode",editMode);
            id = intent.getStringExtra("ID");
            title = intent.getStringExtra("TITLE");
            desc = intent.getStringExtra("DESC");
            commd = intent.getStringExtra("COMMD");

            editcommandtitle.setText(title);
            editcommanddesc.setText(desc);
            editcommand.setText(commd);


        }
        else {
            builder.setTitle("Add");
        }

        return builder.create();
    }

    private void getData(){
        String title = ""+maincommandtitle.getText().toString();
        String desc = ""+maincommanddesc.getText().toString();
        String commd = ""+maincommand.getText().toString();
        Log.d("TAG","Title: "+title+"\nDesc: "+ desc +"\nCommand: "+commd);

        if(editMode){
            dbHelper.updateInfo(
                    "" + id,
                    "" + title,
                    "" + desc,
                    "" + commd
            );
        } else {

            dbHelper.insertInfo(
                    "" + title,
                    "" + desc,
                    "" + commd);
        }
        Log.d("getData","Record added to id: "+id);

        Toast.makeText(getContext(),"Id: "+id,Toast.LENGTH_SHORT).show();

//        startActivity(new Intent(getContext(),MainActivity.class));

//        Intent intent = new Intent(getContext(),MainActivity.class);
//        intent.putExtra("editMode",false);
//        startActivity(intent);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DialogListener");
        }
    }

    public interface DialogListener{
        void applyText(String title, String desc, String commd);

    }
}
