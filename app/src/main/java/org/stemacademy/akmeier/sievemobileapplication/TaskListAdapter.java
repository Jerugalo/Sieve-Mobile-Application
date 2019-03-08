package org.stemacademy.akmeier.sievemobileapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.stemacademy.akmeier.sievemobileapplication.db.Task;

import java.util.List;

/**
 * Receives values from the SQL database and assigns the code to multiple views. These views are
 * compiled into a ViewHolder in preparation for use by the RecyclerView.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    Context C;
    GlobalVars global = GlobalVars.getInstance();

    /** Assigns contents of input database to a local database */
    public final List<Task> tasks;
    public TaskListAdapter(List<Task> tasks, Context c) {
        this.tasks = tasks;
        this.C=c;

    }

    /** Creates the ViewHolder */
    @NonNull
    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.item_task_list, parent, false);
        return new ViewHolder(taskView, 1);
    }

    /** Gets values from the Task list*/
    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.ViewHolder viewHolder, int position) {
        Task task = tasks.get(position);


        viewHolder.taskID = position;

        TextView textView = viewHolder.taskTitle;
        textView.setText(task.getNameID());

        View imageView = viewHolder.taskPriority;
        //TypedArray mPriority = textView.getContext().getResources().obtainTypedArray(R.array.priority);
        //int x = mPriority.getResourceId(task.getPriority(),1);
        //imageView.setImageResource(mPriority.getResourceId(task.getPriority(), 0));
        //mPriority.recycle();

        if(task.getPriority()==0){
        imageView.setBackgroundColor(getColorByThemeAttr(C,R.attr.priorityLow,R.color.defaultLow));
    }
        else if(task.getPriority()==1){
        imageView.setBackgroundColor(getColorByThemeAttr(C,R.attr.priorityMed,R.color.defaultMed));
    }
        else if(task.getPriority()==2){
        imageView.setBackgroundColor(getColorByThemeAttr(C,R.attr.priorityHigh,R.color.defaultHigh));
    }
        else{
        imageView.setBackgroundColor(getColorByThemeAttr(C,R.attr.priorityMed,R.color.defaultMed));
    }
}

    /** Returns the total count of items in the list */
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    /** Assigns layout values to current task item */
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView taskTitle;
        final View taskPriority;
        int taskID;

        ViewHolder(View itemView, int taskID) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            taskTitle = itemView.findViewById(R.id.Title);
            taskPriority = itemView.findViewById(R.id.Priority);
        }
    }
    public static int getColorByThemeAttr(Context context,int attr,int defaultColor){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean got = theme.resolveAttribute(attr,typedValue,true);
        return got ? typedValue.data : defaultColor;
    }
}
