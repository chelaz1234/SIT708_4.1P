package com.example.a41_taskmanager;

import java.io.Serializable;

public class Task implements Serializable {
    long id;
    String title;
    String description;
    String dueDate;

    public Task(long id, String title, String description, String dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }
}
