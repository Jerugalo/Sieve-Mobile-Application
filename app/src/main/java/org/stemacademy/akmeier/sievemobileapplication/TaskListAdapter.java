package org.stemacademy.akmeier.sievemobileapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.stemacademy.akmeier.sievemobileapplication.R;

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
    private final List<Task> mTasks;
    public TaskListAdapter(List<Task> tasks, Context c) {
        mTasks = tasks;
        Task task=new Task(0,"Name","Class","3/3/3","Thingy",1,0,"Alert");
        mTasks.add(task);
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
                return new ViewHolder(taskView, 1);
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

                Task task = mTasks.get(position);

                ((ViewHolder) viewHolder).taskID = position;

                TextView textView = ((ViewHolder) viewHolder).taskTitle;
                textView.setText(task.getNameID());

                View imageView = ((ViewHolder) viewHolder).taskPriority;
                //TypedArray mPriority = textView.getContext().getResources().obtainTypedArray(R.array.priority);
                //int x = mPriority.getResourceId(task.getPriority(),1);
                //imageView.setImageResource(mPriority.getResourceId(task.getPriority(), 0));
                //mPriority.recycle();

                if (task.getPriority() == 0) {
                    imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityLow, R.color.defaultLow));
                } else if (task.getPriority() == 1) {
                    imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityMed, R.color.defaultMed));
                } else if (task.getPriority() == 2) {
                    imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityHigh, R.color.defaultHigh));
                } else {
                    imageView.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityMed, R.color.defaultMed));
                }
                break;
            case 1:
                ViewHolder2 viewHolder2=(ViewHolder2)viewHolder;
                TextView text=((ViewHolder2) viewHolder).text;
                text=viewHolder2.text;
                break;
        }
}

    /** Returns the total count of items in the list */
    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{
        final TextView text;

        ViewHolder2(View itemView){
            super(itemView);

            text=itemView.findViewById(R.id.DueTodayText);
        }
        public void onClick(View v){

        }
    }

    /** Assigns layout values to current task item */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView taskTitle;
        final View taskPriority;
        final Button detailsButton;
        int taskID;

        ViewHolder(View itemView, int taskID) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            taskTitle = itemView.findViewById(R.id.Title);
            taskPriority = itemView.findViewById(R.id.Priority);
            detailsButton = itemView.findViewById(R.id.ToDetails);

            detailsButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent toDetails = new Intent(detailsButton.getContext(), AssignmentDetails.class);
            global.setCurrentTask(mTasks.get(taskID));
            detailsButton.getContext().startActivity(toDetails);
        }
    }
    public static int getColorByThemeAttr(Context context,int attr,int defaultColor){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean got = theme.resolveAttribute(attr,typedValue,true);
        return got ? typedValue.data : defaultColor;
    }

}
