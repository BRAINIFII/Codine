package com.brainifii.codine;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Constants {

    // DEBUG MODE
    public static final Boolean DEBUG_MODE = false;

    // sd path
    public static final File sd = Environment.getExternalStorageDirectory();
    File dir = new File(sd.getAbsolutePath()+"/Codine_Test");

    // db name
    public static final String DB_NAME =  "CodineFragment";

    // db version
    public static final int DB_VERSION = 1;

    // db table
    public static final String TABLE_NAME = "CodineData";

    // table columns
    public static final String C_ID  = "ID";
    public static final String C_TITLE  = "TITLE";
    public static final String C_DESC  = "DESC";
    public static final String C_COMMAND  = "COMMAND";

    // create query for table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + C_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
            + C_TITLE + " TEXT,"
            + C_DESC +  " TEXT,"
            + C_COMMAND + " TEXT"
            + ");";

}