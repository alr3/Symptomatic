package ca.ualberta.symptomaticapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class AddRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 100; // to access the gallery to choose an image
    static final int REQUEST_IMAGE_CAPTURE = 1; // to access the camera to take an image
    static final int REQUEST_TAKE_PHOTO = 1;

    String mCurrentPhotoPath; // the photo's file path
    Problem problem;
    int year;
    int month;
    int day;
    private DatePickerDialog.OnDateSetListener DateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        Toolbar toolbar = findViewById(R.id.addRecord_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Record");
        final Calendar cal = Calendar.getInstance();

        problem = (Problem)getIntent().getSerializableExtra("problem");

        TextView textView = findViewById(R.id.InputProblemTextView);
        textView.setText(problem.getTitle());

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);


        Button dateButton = findViewById(R.id.changeDateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentYear = cal.get(Calendar.YEAR);
                int currentMonth = cal.get(Calendar.MONTH);
                int currentDay = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddRecordActivity.this, android.R.style.Theme_DeviceDefault_Dialog, DateSetListener, currentYear, currentMonth, currentDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                dialog.show();
            }
        });

        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int chosenYear, int chosenMonth, int chosenDay) {
                year = chosenYear;
                month = chosenMonth;
                day = chosenDay;
            }
        };

        // ------------------------------ WORKING ON SAVING PHOTOS -------------------------------
        // CURRENT STATUS: Not working!
        // Set the file path
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File outputFile = new File(path, "Symptomatic");
        // If the directory doesn't exist, create it
        if (!outputFile.exists()) {
            outputFile.mkdir();
        }

        final Button savedPhoto = findViewById(R.id.savedPhoto);
        final Button takePhoto = findViewById(R.id.takePhoto);

        savedPhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_record_menu, menu);
        return true;
    }

    public void viewHome(MenuItem menu) {
        Intent intent = new Intent(AddRecordActivity.this, AddProblemActivity.class);
        startActivity(intent);
    }

    public void viewViewProblems(MenuItem menu) {
        Intent intent = new Intent(AddRecordActivity.this, ListProblemsActivity.class);
        startActivity(intent);
    }

    public void viewAddProblem(MenuItem menu) {
        Intent intent = new Intent(AddRecordActivity.this, AddProblemActivity.class);
        startActivity(intent);
    }

    public void viewLogout(MenuItem menu){
        Login.thisCaregiver = null;
        Login.thisUser = null;
        Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);
        startActivity(intent);
    }


    // ---------------------------------- PHOTO FUNCTIONALITY ----------------------------------
    public void onClick(View v) {
        int viewId = v.getId();
        // Gets images from gallery - Reference: https://en.proft.me/2017/07/1/how-get-image-gallery-or-camera-android/
        if (viewId == R.id.savedPhoto) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }

        // If the user wants to take a photo using their camera
        // Reference: https://developer.android.com/training/camera/photobasics#java
        if (viewId == R.id.takePhoto) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            File newPhotoFile = null;
            try {
                newPhotoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("Error in creating the file for the image");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:

                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();

                }
        }
    }

    // TODO: Edit
    // Reference: https://developer.android.com/training/camera/photobasics#java
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Reference: https://developer.android.com/training/camera/photobasics#java
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("Error in creating the file for the image");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

}
