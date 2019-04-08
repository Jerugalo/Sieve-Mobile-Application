package org.stemacademy.akmeier.sievemobileapplication;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.stemacademy.akmeier.sievemobileapplication.db.Task;

import java.util.List;

/**
 * Receives values from the SQL database and assigns the code to multiple views. These views are
 * compiled into a TaskViewHolder in preparation for use by the RecyclerView.
 */
public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context C;
    GlobalVars global = GlobalVars.getInstance();
    boolean add1=false;
    public static final int TASK_ID = 1;
    public final List<Task> tasks;

    public TaskListAdapter(List<Task> tasks, Context c) {
        this.tasks = tasks;
        this.C=c;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.item_task_list, parent, false);
        View dividerView=inflater.inflate(R.layout.item_due_today_list,parent,false);
        switch(viewType) {
            case 0:
                return new TaskViewHolder(taskView);
            case 1:
                return new DividerViewHolder(dividerView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        switch(viewHolder.getItemViewType()) {
            case 0:
                TaskViewHolder taskViewHolder = (TaskViewHolder)viewHolder;
                if(add1){
                    position=position-1;
                }

                Task task = tasks.get(position);
                taskViewHolder.itemView.setTag(position);

                TextView textView = taskViewHolder.taskTitle;
                textView.setText(task.getNameID());

                View imageView = taskViewHolder.taskPriority;
                switch (task.getPriority()) {
                    case 0: imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityLow,
                            R.color.defaultLow)); break;
                    case 1: imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityMed,
                            R.color.defaultMed)); break;
                    case 2: imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityHigh,
                            R.color.defaultHigh)); break;
                    default: imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityMed,
                            R.color.defaultMed)); break;
                }
                break;

            case 1:
                DividerViewHolder dividerViewHolder = (DividerViewHolder)viewHolder;
                View dividerView = dividerViewHolder.divider;
                dividerViewHolder.itemView.setTag(-1);
                if(global.getgDivPos() == 0){
                    dividerView.setBackgroundColor(getColorByThemeAttr(C, R.attr.dividerHidden,
                            R.color.defaultBackground));
                }else{
                    dividerView.setBackgroundColor(getColorByThemeAttr(C, R.attr.dividerColor,
                            R.color.defaultBar));
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public int getItemViewType(int position){
        if(position!=GlobalVars.getgDivPos()){
            return 0;
        }
        else{
            int checker=GlobalVars.getgDivPos();
            add1=true;
            return 1;
        }
    }

    public static int getColorByThemeAttr(Context context,int attr,int defaultColor){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean got = theme.resolveAttribute(attr,typedValue,true);
        return got ? typedValue.data : defaultColor;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        final TextView taskTitle;
        final View taskPriority;

        TaskViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any TaskViewHolder instance.
            super(itemView);

            taskTitle = itemView.findViewById(R.id.Title);
            taskPriority = itemView.findViewById(R.id.Priority);
        }
    }

    public class DividerViewHolder extends RecyclerView.ViewHolder{
        final View divider;

        DividerViewHolder(View itemView){
            super(itemView);

            divider=itemView.findViewById(R.id.dividerView);
        }
        public void onClick(View v){

        }
    }
}