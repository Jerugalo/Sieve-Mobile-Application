package com.example.a53914.sievemobileapplication;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a53914.sievemobileapplication.db.Task;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>{


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle;
        public ImageView taskPriority;
        public Button detailsButton;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            taskTitle = (TextView) itemView.findViewById(R.id.TaskTitle);
            taskPriority = (ImageView) itemView.findViewById(R.id.TaskPriority);
            detailsButton = (Button) itemView.findViewById(R.id.TaskToDetails);
        }
    }

    private List<Task> mTasks;

    public TaskListAdapter(List<Task> tasks) {
        mTasks = tasks;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_task_list, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TaskListAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Task task = mTasks.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.taskTitle;
        textView.setText(task.getNameID());

        ImageView imageView = viewHolder.taskPriority;
        TypedArray mPriority = ctx.getResources().obtainTypedArray(R.array.priority);
        imageView.setImageResource(mPriority[task.getPriority()]);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
