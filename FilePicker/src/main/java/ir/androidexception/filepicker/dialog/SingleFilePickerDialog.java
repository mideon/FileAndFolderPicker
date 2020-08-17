package ir.androidexception.filepicker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import ir.androidexception.filepicker.R;
import ir.androidexception.filepicker.adapter.FileAdapter;
import ir.androidexception.filepicker.databinding.DialogPickerBinding;
import ir.androidexception.filepicker.interfaces.OnCancelPickerDialogListener;
import ir.androidexception.filepicker.interfaces.OnChangeInitialPathListener;
import ir.androidexception.filepicker.interfaces.OnConfirmDialogListener;
import ir.androidexception.filepicker.interfaces.OnPathChangeListener;
import ir.androidexception.filepicker.interfaces.OnSelectItemListener;
import ir.androidexception.filepicker.model.Item;
import ir.androidexception.filepicker.utility.FileUtils;
import ir.androidexception.filepicker.utility.Util;


public class SingleFilePickerDialog extends Dialog implements OnPathChangeListener, OnSelectItemListener, OnChangeInitialPathListener {
    private RecyclerView recyclerViewDirectories;
    private FloatingActionButton fab;
    private ImageView close;
    private Context context;
    private DialogPickerBinding binding;
    private FileAdapter adapter;
    private OnCancelPickerDialogListener onCancelPickerDialogListener;
    private OnConfirmDialogListener onConfirmDialogListener;
    private File file;
    private String initialPath = "";
    private String fileExtType = "";

    public SingleFilePickerDialog(@NonNull Context context, OnCancelPickerDialogListener onCancelPickerDialogListener,
                                  OnConfirmDialogListener onConfirmDialogListener) {
        super(context);
        this.context = context;
        this.onCancelPickerDialogListener = onCancelPickerDialogListener;
        this.onConfirmDialogListener = onConfirmDialogListener;
    }

    public SingleFilePickerDialog(@NonNull Context context, OnCancelPickerDialogListener onCancelPickerDialogListener,
                                  OnConfirmDialogListener onConfirmDialogListener, String initialPath) {
        this(context, onCancelPickerDialogListener, onConfirmDialogListener);
        this.initialPath = initialPath;
    }

    public SingleFilePickerDialog(@NonNull Context context, OnCancelPickerDialogListener onCancelPickerDialogListener,
                                  OnConfirmDialogListener onConfirmDialogListener, String initialPath, String fileExtType) {
        this(context, onCancelPickerDialogListener, onConfirmDialogListener, initialPath);
        this.fileExtType = fileExtType;
    }

    public void setFileExtType(String fileExtType) {
        this.fileExtType = fileExtType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_picker, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        recyclerViewDirectories = binding.rvDialogPickerDirectories;
        fab = binding.fab;
        close = binding.ivClose;

        if(Util.permissionGranted(context)){
            binding.setPath("Internal Storage" + context.getString(R.string.arrow));
            binding.setBusySpace(Util.bytesToHuman(Util.busyMemory()));
            binding.setTotalSpace(Util.bytesToHuman(Util.totalMemory()));
            int busySpacePercent = (int)(((float)Util.busyMemory() / Util.totalMemory()) * 100);
            binding.setBusySpacePercent(busySpacePercent + "%");
            binding.progressView.setProgress(busySpacePercent);

            setupDirectoriesListRecyclerView();
            setupClickListener();
        }
    }


    private void setupClickListener(){
        close.setOnClickListener(v -> {
            onCancelPickerDialogListener.onCanceled();
            this.cancel();
        });

        fab.setOnClickListener(v -> {
            onConfirmDialogListener.onConfirmed(file);
            this.cancel();
        });
    }

    private void setupDirectoriesListRecyclerView() {
        List<Item> items = new ArrayList<>();

        File internalStorage = FileUtils.getCurrentPath(initialPath, this);

        if(Util.permissionGranted(context) && internalStorage.exists()) {
            binding.setPath("Internal Storage" + context.getString(R.string.arrow) + initialPath);
        }

        List<File> children = new ArrayList<>(Arrays.asList(Objects.requireNonNull(internalStorage.listFiles())));
        for (File file : children){
            items.add(new Item(file));
        }
        adapter = new FileAdapter(context, items, this, this, internalStorage.getPath(), fileExtType);
        recyclerViewDirectories.setAdapter(adapter);
        recyclerViewDirectories.setNestedScrollingEnabled(false);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onBackPressed() {
        adapter.back();
    }

    @Override
    public void onChanged(String path) {
        binding.setPath(Util.changePathFormat(context, path));
    }

    @Override
    public void onSelected(File f) {
        file = f;
        if(f==null) fab.setVisibility(View.GONE);
        else fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChangeInitialPath(String newPath) {
        initialPath = newPath;
    }
}
