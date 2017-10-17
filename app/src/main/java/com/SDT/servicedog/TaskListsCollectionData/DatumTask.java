package com.SDT.servicedog.TaskListsCollectionData;

/**
 * Created by laitkor3 on 18/07/16.
 */
public class DatumTask {

    public Tasks Task;
    public String status;
    public Object time;


    public Tasks getTasks() {
        return Task;
    }

    public void setTasks(Tasks task) {
        Task = task;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }
}


