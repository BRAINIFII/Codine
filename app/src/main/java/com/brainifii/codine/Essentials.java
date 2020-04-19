package com.brainifii.codine;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import static com.brainifii.codine.Constants.sd;

public class Essentials {
    String sdPath;

    public String sudo(String Command){
        CommandResult command_output = Shell.SU.run(Command);
        return ""+command_output;
    }

    public boolean filecheck(){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath()+"/Codine_Test");
        String commd = sudo("ls "+dir+"/CodineFragment");
        Boolean cont = commd.contains("CodineFragment");
        if(cont){
//            return "Backup found: \n\""+commd+"\"";
            return true;
        }
        else {
//            return "Backup not found: \n\""+commd+"\"";
            return false;
        }
    }

    public void file() throws FileNotFoundException {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath()+"/Codine_Test");
        File file = new File(dir,"Test");
        FileOutputStream f = new FileOutputStream(file);
//        String commd = sudo("mkdir "+dir);
//        return "";
    }

    public String backup(){
        boolean valid = filecheck();
        if (valid){
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath()+"/Codine_Test");
            File rootdir = Environment.getDataDirectory();
//            String commd = sudo("cp "+dir+"/CodineFragment "+rootdir);
            String commd = "sudo cp "+rootdir+"/data/com.brainifii.codine/databases/CodineFragment "+dir+"/CodineFragmentbkp";
            Log.d("Command",""+commd);
            return "\n\n"+commd;
        }

        return null;
    }

    public String bkp() {
        Context context = null;
        String state = Environment.getExternalStorageState();
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        boolean sdperm = Environment.MEDIA_MOUNTED.equals(state);
        String currentDBPath = "/data/data/com.pocohunter.codine/databases/"+Constants.DB_NAME+"";
        String backupDBPath = sd+"/Codine_Test/backupname.db";
        try {

            if (sdperm) {
                File currentDB = new File(currentDBPath);
                File backupDB = new File(backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                return ""+currentDBPath;
            }
        } catch (Exception e) {

        }
        return "\nDB PATH:"+currentDBPath+"\n\nBKP PATH:"+backupDBPath;
    }

}

