package com.brainifii.codine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME,null,Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    // insert info function

    public long insertInfo(String title,String desc,String command){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.C_TITLE,title);
        values.put(Constants.C_DESC,desc);
        values.put(Constants.C_COMMAND,command);

        long id = db.insert(Constants.TABLE_NAME,null,values);
        db.close();
        return id;

    }

    public void updateInfo(String id,String title,String desc,String command){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.C_TITLE,title);
        values.put(Constants.C_DESC,desc);
        values.put(Constants.C_COMMAND,command);

        db.update(Constants.TABLE_NAME,values,Constants.C_ID+" = ?",new String[]{id});
        db.close();
    }

    public void deleteInfo(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.C_ID+ " = ? ",new String[]{id});
        db.close();
    }

    public ArrayList<Model> getAllData(String orderBy){
        ArrayList<Model> arrayList = new ArrayList<>();

        // query for select all info in database
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery,null);

        // when we select all info from database new get the data from columns
        if(cursor.moveToNext()){
            do{
                //do is used because first it gets the data from column then moves to next condition
                Model model = new Model(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_TITLE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_DESC)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_COMMAND))
                );

                arrayList.add(model);
            }while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }
}
