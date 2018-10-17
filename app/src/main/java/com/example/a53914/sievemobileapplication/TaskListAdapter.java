package com.example.a53914.sievemobileapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a53914.sievemobileapplication.db.;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitleTextView;
        public ImageView taskPriorityImage;
        public Button detailsButton;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            taskTitleTextView = (TextView) itemView.findViewById(R.id.TaskTitle);
            taskPriorityImage = (ImageView) itemView.findViewById(R.id.TaskPriority);
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
        TextView textView = viewHolder.taskTitleTextView;
        textView.setText(task.getName());
        Button button = viewHolder.detailsButton;
        button.setText(task.isOnline() ? "Message" : "Offline");
        button.setEnabled(task.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
