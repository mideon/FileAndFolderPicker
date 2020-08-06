package ir.androidexception.filepicker.utility;

import android.os.Environment;

import java.io.File;

import ir.androidexception.filepicker.interfaces.OnChangeInitialPathListener;

public class FileUtils {
    public static File getCurrentPath(String initialPath, OnChangeInitialPathListener listener) {
        File rootPath = Environment.getExternalStorageDirectory();
        File currentPath = null;
        if(initialPath != null && initialPath.length() > 0) {
            currentPath = new File(rootPath, initialPath);
            if(currentPath.exists()) {
               return  currentPath;
            } else {
                listener.onChangeInitialPath("");
            }
        }
        return rootPath;
    }
}
