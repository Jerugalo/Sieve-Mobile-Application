package org.stemacademy.akmeier.sievemobileapplication;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.stemacademy.akmeier.sievemobileapplication.db.Task;
import org.stemacademy.akmeier.sievemobileapplication.db.TaskDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.toIntExact;

/**
 * Receives values from the SQL database and assigns the code to multiple views. These views are
 * compiled into a TaskViewHolder in preparation for use by the RecyclerView.
 */
public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context C;
    boolean add1=false;
    public static final int TASK_ID = 1;
    public List<Task> items;
    private TaskDatabase taskDatabase;
    private final Task DIVIDER = new Task(0, "", "", "" ,
            "", -1, 0, "");


    public TaskListAdapter(Context C) {
        this.C=C;
        items = new ArrayList<>();
        taskDatabase = TaskDatabase.getInstance(C);
    }

    public void updateItems(final List<Task> newItems){
        final List oldItems = new ArrayList<>(this.items);
        this.items.clear();
        if (newItems != null) {
            newItems.add(dividerPosition(), DIVIDER);
            this.items.addAll(newItems);
        }
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldItems.size();
            }

            @Override
            public int getNewListSize() {
                return items.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
            }
        }).dispatchUpdatesTo(this);
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
            taskViewHolder.setItem(items.get(position));
        } else if (holder.getItemViewType() == 1) {
            DividerViewHolder dividerViewHolder = (DividerViewHolder) holder;
            dividerViewHolder.setItem(items.get(position));
        }
   }

   @Override
   public int getItemViewType(int position){
        if(items.get(position).getTypeID() != -1){
            return 0;
        }
        else{
            return 1;
        }
   }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void deleteTask(Task task){
        items.remove(task);
        notifyItemRemoved(items.indexOf(task));
    }

    private static int getColorByThemeAttr(Context context,int attr,int defaultColor){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean got = theme.resolveAttribute(attr,typedValue,true);
        return got ? typedValue.data : defaultColor;
    }

    public int dividerPosition() {
        int adapterPos = 0;
        for (int i = 0; i < taskDatabase.taskDao().getAll().size(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.AM_PM, Calendar.PM);
            Date date1 = calendar.getTime();
            String incorrectDate = taskDatabase.taskDao().getAll().get(i).getDueDate();
            List<String> divided1 = new ArrayList<>(Arrays.asList(incorrectDate.split("/")));
            String cTD1 = divided1.get(1) + "/" + divided1.get(0) + "/" + divided1.get(2);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            try {
                date1 = sdf1.parse(cTD1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long check = date1.getTime() - calendar.getTime().getTime();
            int days1 = 0;
            days1 = abs(toIntExact(TimeUnit.DAYS.convert(check, TimeUnit.MILLISECONDS)));
            if (days1 == 0) {
                adapterPos += 1;
            }
        }
        return adapterPos;
    }

    /* Task Item ViewHolder */
    public class TaskViewHolder extends RecyclerView.ViewHolder {
        final TextView taskTitle;
        final View taskPriority;
        Task task;

        TaskViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any TaskViewHolder instance.
            super(itemView);

            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskPriority = itemView.findViewById(R.id.Priority);
        }
        public void setItem(Task task) {
            this.task = task;
            taskTitle.setText(task.getNameID());
            switch (task.getPriority()) {
                case 0: taskPriority.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityLow,
                        R.color.defaultLow)); break;
                case 1: taskPriority.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityMed,
                        R.color.defaultMed)); break;
                case 2: taskPriority.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityHigh,
                        R.color.defaultHigh)); break;
                default: taskPriority.setBackgroundColor(getColorByThemeAttr(C, R.attr.priorityMed,
                        R.color.defaultMed)); break;
            }
        }
    }



    /* Divider ViewHolder */
    public class DividerViewHolder extends RecyclerView.ViewHolder{
        final View divider;
        Task task;

        DividerViewHolder(View itemView){
            super(itemView);

            divider=itemView.findViewById(R.id.dividerView);
        }
        public void onClick(View v){

        }

        public void setItem(Task task){
            this.task = task;
            if(GlobalVars.getgDivPos() == 0){
                divider.setBackgroundColor(getColorByThemeAttr(C, R.attr.dividerHidden,
                        R.color.defaultBackground));
            }else{
                divider.setBackgroundColor(getColorByThemeAttr(C, R.attr.dividerColor,
                        R.color.defaultBar));
            }
        }
    }
}
