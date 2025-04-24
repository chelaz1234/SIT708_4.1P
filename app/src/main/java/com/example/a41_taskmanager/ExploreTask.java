package com.example.a41_taskmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExploreTask extends AppCompatActivity {

    private TextView tv_title, tv_description, tv_due_date, tv_task_id;

    private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase database;

    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_explore_task);
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");
        mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();
        tv_title = findViewById(R.id.tv_title);
        tv_description = findViewById(R.id.tv_description);
        tv_task_id = findViewById(R.id.id);
        tv_due_date = findViewById(R.id.tv_due_date);

        tv_task_id.setText("Viewing Task: " + task.id);
        tv_title.setText("Title: " + task.title);
        tv_description.setText(task.description);
        tv_due_date.setText("Task Due on: " + task.dueDate);
        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditTask.class);
                i.putExtra("task", task);
                startActivity(i);
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete("tasks", "id = ?", new String[]{String.valueOf(task.id)});
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}