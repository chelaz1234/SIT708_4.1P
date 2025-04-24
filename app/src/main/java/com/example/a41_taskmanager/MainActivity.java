package com.example.a41_taskmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Task> taskList;
    ImageView addTaskBtn;
    SQLiteDatabase database;
    MySQLiteHelper mySQLiteHelper;
    MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.rv);
        addTaskBtn = findViewById(R.id.btn_add_task);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        adapter = new MyAdapter(this, taskList);
        fetchDataFromDB();
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditTask.class);
                startActivity(i);
            }
        });
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataFromDB();
            }
        });
    }

    public void fetchDataFromDB() {
        taskList.clear();
        Cursor cursor = database.query(
                "tasks",
                null,
                null,
                null,
                null,
                null,
                "dueDate DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                taskList.add(new Task(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("dueDate"))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        recyclerView.setAdapter(adapter);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Task> dataList;
        private Context context;

        public MyAdapter(Context context, List<Task> taskList) {
            this.context = context;
            this.dataList = taskList;
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
            Task task = dataList.get(position);
            holder.textView.setText(task.title);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_task_name);
                itemView.setOnClickListener(v -> {
                        Intent i = new Intent(context, ExploreTask.class);
                        i.putExtra("task", dataList.get(getAdapterPosition()));
                        context.startActivity(i);
                });
            }
        }
    }
}