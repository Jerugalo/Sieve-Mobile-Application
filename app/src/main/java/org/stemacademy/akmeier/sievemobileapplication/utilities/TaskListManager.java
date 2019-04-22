package org.stemacademy.akmeier.sievemobileapplication.utilities;


import org.stemacademy.akmeier.sievemobileapplication.db.Task;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.StrictMath.abs;

public class TaskListManager {

    private static TaskListManager instance = new TaskListManager();

    private TaskListManager(){

    }

    public static List<Task> getProjectList(List<Task> tasks){
        List<Task> projects = null;
        for (Task task : tasks) {
            if (task.getTypeID() == Task.TypeID.PROJECT){
                projects.add(task);
            }
        }
        projects = getSortedList(projects);
        return projects;
    }

    public static List<Task> getProjectChildrenList(List<Task> tasks, Task task){
        tasks = getSortedList(tasks);
        return tasks;
    }

    public static List<Task> getSortedList(List<Task> tasks){
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                float compare1;
                float compare2;
                float days1=0;
                float days2=0;
                Calendar calendar =Calendar.getInstance();
                calendar.set(Calendar.MILLISECOND,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.HOUR_OF_DAY,12);
                calendar.set(Calendar.HOUR,0);
                calendar.set(Calendar.AM_PM,Calendar.PM);
                Date date1=calendar.getTime();
                Date date2=calendar.getTime();
                String incorrectTaskDate1=o1.getDueDate();
                String incorrectTaskDate2=o2.getDueDate();
                List<String> divided1=new ArrayList<>(Arrays.asList(incorrectTaskDate1.split("/")));
                String cTD1=divided1.get(1)+"/"+divided1.get(0)+"/"+divided1.get(2);
                List<String> divided2=new ArrayList<>(Arrays.asList(incorrectTaskDate2.split("/")));
                String cTD2=divided2.get(1)+"/"+divided2.get(0)+"/"+divided2.get(2);
                SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date1=sdf1.parse(cTD1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date2=sdf2.parse(cTD2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(date1!=null&&date2!=null){
                    long diff1= date1.getTime()-calendar.getTime().getTime();
                    long diff2= date2.getTime()-calendar.getTime().getTime();
                    days1=abs(TimeUnit.DAYS.convert(diff1,TimeUnit.MILLISECONDS));
                    days2=abs(TimeUnit.DAYS.convert(diff2,TimeUnit.MILLISECONDS));
                }
                if(days1!=0&&days2!=0){
                    int sample=o1.getPriority();
                    int sample2= o2.getPriority();
                    compare1=((o1.getPriority()+1)*10)/((days1+1)*50);
                    compare2=((o2.getPriority()+1)*10)/((days2+1)*50);
                }else{
                    compare1=0;
                    compare2=0;
                }
                if(days1==0){
                    compare1=(100*(o1.getPriority()+1))+1000;
                }
                if(days2==0){
                    compare2=(100*(o2.getPriority()+1))+1000;
                }
                if(o1.getTypeID() == Task.TypeID.PROJECT){
                    compare1+=250;
                }
                if(o2.getTypeID()== Task.TypeID.PROJECT ){
                    compare2+=250;
                }
                return compare1>compare2 ? -1:(compare1<compare2) ? 1: 0;
            }
        });
        return tasks;
    }
}
