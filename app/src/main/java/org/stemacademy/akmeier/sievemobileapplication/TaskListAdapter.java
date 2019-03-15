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
public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context C;
    GlobalVars global = GlobalVars.getInstance();
    boolean add1=false;

    /** Assigns contents of input database to a local database */
    public final List<Task> tasks;
    public TaskListAdapter(List<Task> tasks, Context c) {
        this.tasks = tasks;
        Task task=new Task(0,"Name","Class","3/3/3","Thingy",1,0,"Alert");
        tasks.add(task);
        this.C=c;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==GlobalVars.getgDivPos()){
            int checker=GlobalVars.getgDivPos();
            add1=true;
            return 1;
        }
        else{
            return 0;
        }
    }

    /** Creates the ViewHolder */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.item_task_list, parent, false);
        View dividerView=inflater.inflate(R.layout.item_due_today_list,parent,false);
        switch(viewType) {
            case 0:
                return new ViewHolder(taskView);
            case 1:
                return new ViewHolder2(dividerView);
        }
        return null;
    }

    /** Gets values from the Task list*/
   @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        switch(viewHolder.getItemViewType()) {
            case 0:
                ViewHolder ViewHolder1=(ViewHolder)viewHolder;
                if(add1){
                    position=position-1;
                }

                Task task = tasks.get(position);
                ((ViewHolder) viewHolder).taskID = position;

                TextView textView = ((ViewHolder) viewHolder).taskTitle;
                textView.setText(task.getNameID());

                View imageView = ((ViewHolder) viewHolder).taskPriority;
                switch (task.getPriority()) {
                    case 0: imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityLow, R.color.defaultLow));
                    case 1: imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityMed, R.color.defaultMed));
                    case 2: imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityHigh, R.color.defaultHigh));
                    default: imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityMed, R.color.defaultMed));
                }
                break;

            case 1:
                ViewHolder2 viewHolder2=(ViewHolder2)viewHolder;
                View dividerView=((ViewHolder2) viewHolder).divider;
                dividerView=viewHolder2.divider;
                if(global.getgDivPos() == 0){dividerView.setBackgroundColor(getColorByThemeAttr(C, R.attr.dividerHidden, R.color.defaultBackground));}
                else{dividerView.setBackgroundColor(getColorByThemeAttr(C, R.attr.dividerColor, R.color.defaultBar));}
                break;
        }
   }

    /** Returns the total count of items in the list */
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{
        final View divider;

        ViewHolder2(View itemView){
            super(itemView);

            divider=itemView.findViewById(R.id.dividerView);
        }
        public void onClick(View v){

        }
    }

    /** Assigns layout values to current task item */
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView taskTitle;
        final View taskPriority;
        int taskID;

        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            taskTitle = itemView.findViewById(R.id.Title);
            taskPriority = itemView.findViewById(R.id.Priority);
            itemView.setTag(,taskID);
        }
    }
    public static int getColorByThemeAttr(Context context,int attr,int defaultColor){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean got = theme.resolveAttribute(attr,typedValue,true);
        return got ? typedValue.data : defaultColor;
    }
}
