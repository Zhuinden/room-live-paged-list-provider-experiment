package com.zhuinden.roomlivepagedlistproviderexperiment.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import java.util.Date;

/**
 * Created by Owner on 2017. 10. 09..
 */

@Entity(tableName = Task.TABLE_NAME)
public class Task {
    public static DiffCallback<Task> DIFF_CALLBACK = new DiffCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem);
        }
    };

    public static final String TABLE_NAME = "TASK";
    public static final String COLUMN_ID = "task_id";
    public static final String COLUMN_TEXT = "task_text";
    public static final String COLUMN_DATE = "task_date";

    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    private int id;

    @ColumnInfo(name = COLUMN_TEXT)
    private String text;

    @ColumnInfo(name = COLUMN_DATE)
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Task)) {
            return false;
        }

        Task task = (Task) o;

        if(id != task.id) {
            return false;
        }
        if(text != null ? !text.equals(task.text) : task.text != null) {
            return false;
        }
        return date != null ? date.equals(task.date) : task.date == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
