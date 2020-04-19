package com.brainifii.codine;

import android.os.Environment;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;
import java.io.File;

public class Essentials {
    String sdPath;

    public String sudo(String Command){
        CommandResult command_output = Shell.SU.run(Command);
        return ""+command_output;
    }

    public boolean filecheck(){
        File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard.getAbsolutePath()+Constants.DB_BACKUP_PATH+Constants.DB_BACKUP_NAME);
        File dir = new File(sdCard.getAbsolutePath()+Constants.DB_BACKUP_PATH);
//        if (dir.exists() && file.isFile())
        return file.exists();
    }

}

