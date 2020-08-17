package ir.androidexception.filepickerexample;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import ir.androidexception.filepicker.dialog.DirectoryPickerDialog;
import ir.androidexception.filepicker.dialog.MultiFilePickerDialog;
import ir.androidexception.filepicker.dialog.SingleFilePickerDialog;
import ir.androidexception.filepicker.interfaces.OnCancelPickerDialogListener;
import ir.androidexception.filepicker.interfaces.OnConfirmDialogListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String subfolder = "Download";


        Button singleFilePickerButton = findViewById(R.id.btn_single_file_picker);
        singleFilePickerButton.setOnClickListener(view -> {
            if(permissionGranted()) {
                SingleFilePickerDialog singleFilePickerDialog = new SingleFilePickerDialog(this,
                        () -> Toast.makeText(MainActivity.this, "Canceled!!", Toast.LENGTH_SHORT).show(),
                        files -> Toast.makeText(MainActivity.this, files[0].getPath(), Toast.LENGTH_SHORT).show(),
                        subfolder,
                        ".json"
                );
                singleFilePickerDialog.show();
            }
            else{
                requestPermission();
            }
        });


        Button multiFilePickerButton = findViewById(R.id.btn_multi_file_picker);
        multiFilePickerButton.setOnClickListener(view -> {
            if(permissionGranted()) {
                MultiFilePickerDialog multiFilePickerDialog = new MultiFilePickerDialog(this,
                        () -> Toast.makeText(MainActivity.this, "Canceled!!", Toast.LENGTH_SHORT).show(),
                        files -> Toast.makeText(MainActivity.this, files[0].getPath(), Toast.LENGTH_SHORT).show(),
                        subfolder
                );
                multiFilePickerDialog.setFileExtType(".xml");
                multiFilePickerDialog.show();
            }
            else{
                requestPermission();
            }
        });



        Button directoryPickerButton = findViewById(R.id.btn_directory_picker);
        directoryPickerButton.setOnClickListener(view -> {
            if(permissionGranted()) {
                DirectoryPickerDialog directoryPickerDialog = new DirectoryPickerDialog(this,
                        () -> Toast.makeText(MainActivity.this, "Canceled!!", Toast.LENGTH_SHORT).show(),
                        files -> Toast.makeText(MainActivity.this, files[0].getPath(), Toast.LENGTH_SHORT).show(),
                        subfolder
                );
                directoryPickerDialog.show();
            }
            else{
                requestPermission();
            }
        });

    }


    private boolean permissionGranted(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

}