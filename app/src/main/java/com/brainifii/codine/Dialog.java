package com.brainifii.codine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
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

import static android.content.Context.VIBRATOR_SERVICE;

public class Dialog extends AppCompatDialogFragment {
    private EditText maincommandtitle;
    private EditText maincommanddesc;
    private EditText maincommand;
    private DialogListener listener;
    private DatabaseHelper dbHelper;


    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),R.style.DialogTheme);

        dbHelper = new DatabaseHelper(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View Dilaogview = inflater.inflate(R.layout.activity_add_record,null);



        builder.setView(Dilaogview)
                .setTitle("Add Command")
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
                        Toast.makeText(getContext(),"Added Successfully...!!!",Toast.LENGTH_SHORT).show();

                    }
                });

        maincommandtitle = Dilaogview.findViewById(R.id.add_rec_commandtitle);
        maincommanddesc = Dilaogview.findViewById(R.id.add_rec_commanddesc);
        maincommand = Dilaogview.findViewById(R.id.add_rec_command);


        return builder.create();
    }



    private void getData(){
        String title = ""+maincommandtitle.getText().toString();
        String desc = ""+maincommanddesc.getText().toString();
        String commd = ""+maincommand.getText().toString();
        Log.d("TAG","Title: "+title+"\nDesc: "+ desc +"\nCommand: "+commd);

        long id = dbHelper.insertInfo(
                ""+title,
                ""+desc,
                ""+commd);

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
        void applyText(String title,String desc,String commd);

    }
}
