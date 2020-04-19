package com.brainifii.codine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private Context context;
    private ArrayList<Model> arrayList;
    // dataBase object
    private DatabaseHelper databaseHelper;

    Adapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        // initialize here
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        Model model = arrayList.get(position);

        //get view
        final String id = model.getId();
        final String title = model.getTitle();
        final String desc = model.getDesc();
        final String commd = model.getCommd();

        //set views
        holder.title.setText(title);
        holder.desc.setText(desc);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                assert vib != null;
                vib.vibrate(50);
                editDialog( ""+position, ""+id, ""+title, ""+desc, ""+commd);
            }
        });


        holder.runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                assert vib != null;
                vib.vibrate(500);
                Essentials essential = new Essentials();

                String command_output = essential.sudo(commd);
                Log.wtf("Command",commd);
                Toast.makeText(context,"Command Output:  "+command_output,Toast.LENGTH_SHORT).show();

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                assert vib != null;
                vib.vibrate(50);
                deleteDialog(
                        ""+id,
                        ""+title
                );
                return false;
            }
        });
    }

    private void deleteDialog(final String id, final String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage(Html.fromHtml("<html>Are you sure you want to delete this command ?<br><br><b>Command</b>: " + title + "</html>"));
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_delete);

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteInfo(id);
                ((MainActivity)context).onResume();
                Toast.makeText(context,"Command\"" + title + "\" Deleted Successfully.",Toast.LENGTH_LONG).show();
            }
        });
        builder.create().show();
    }

    private void editDialog(final String position, final String id, final String title, final String desc, final String commd) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Edit");
        builder1.setMessage("Are you sure you want to update?");
        builder1.setCancelable(false);
        builder1.setIcon(R.drawable.ic_action_edit);
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                assert vib != null;
                vib.vibrate(50);
                // Debug
                if (Constants.DEBUG_MODE) {
                    debug("DEBUG MODE:"
                            +" \nposition:" + position
                            + "\nID: " + id
                            + "\nTitle: " + title
                            + "\nDescription: " + desc
                            + "\nCommand: " + commd);

                }
                Intent intent = new Intent(context,EditRecordActivity.class);
                intent.putExtra("ID",id);
                intent.putExtra("TITLE",title);
                intent.putExtra("DESC",desc);
                intent.putExtra("COMMD",commd);
                intent.putExtra("editMode",true);
                context.startActivity(intent);
            }
        });

        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder1.create().show();
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class Holder extends RecyclerView.ViewHolder{

        TextView title,desc;
        Button editButton;
        Button runButton;

        Holder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.card_title);
            desc = itemView.findViewById(R.id.card_title_desc);
            editButton = itemView.findViewById(R.id.card_edit_button);
            runButton = itemView.findViewById(R.id.card_run_button);

        }
    }


    private void debug(String string){
        Log.d("DEBUG",string);
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }

}
