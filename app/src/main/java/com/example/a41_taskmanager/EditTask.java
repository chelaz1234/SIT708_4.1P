package com.example.a41_taskmanager;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

// Ensure proper class definition
public class EditTask extends AppCompatActivity {
    TextView heading;
    EditText title, desc;
    SQLiteDatabase database;

    MySQLiteHelper mySQLiteHelper;
    Task task = null;

    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);  // Correct layout setup

        mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();  // Initialize database

        // Initialize views
        heading = findViewById(R.id.heading);
        title = findViewById(R.id.et_title);
        desc = findViewById(R.id.et_description);

        // Handle incoming intent to edit a task
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");

        if(task != null) {  // If editing a task, set the text and button label
            heading.setText("Editing the Task: " + task.id);
            title.setText(task.title);
            desc.setText(task.description);
            date = task.dueDate;
            Button btnAddTaskToDb = findViewById(R.id.btnAddTaskToDb);
            btnAddTaskToDb.setText("Update");
        }

        // Add the button click listener to save data to the database
        findViewById(R.id.btnAddTaskToDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataToDB();
            }
        });

        // Listener for date picking
        findViewById(R.id.buttonPickDateTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Current date for the date picker
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create and show the DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditTask.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int sy, int sm, int sd) {

                                int correctedMonth = sm + 1;

                                // Set the date in a readable format
                                date = sd + "/" + correctedMonth + "/" + sy;
                            }
                        },
                        year, month, day  // Default values for the date picker
                );

                datePickerDialog.show();  // Show the date picker
            }
        });
    }  // Closing brace for onCreate()


    private void addDataToDB() {
        String titleText = title.getText().toString();
        String descText = desc.getText().toString();

        if(titleText.isEmpty() || descText.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data to be inserted into the database
        ContentValues values = new ContentValues();
        values.put("title", titleText);
        values.put("description", descText);
        values.put("dueDate", date);

        if(task == null) {
            database.insert("tasks", null, values);
            Toast.makeText(this, "Task created", Toast.LENGTH_SHORT).show();
        } else {
            database.update("tasks", values, "id = ?", new String[]{String.valueOf(task.id)});
            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
        }

        finish();  
    }
}
