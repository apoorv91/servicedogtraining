package com.SDT.servicedog.TaskListsCollectionData;

import java.util.List;

/**
 * Created by laitkor3 on 18/07/16.
 */
public class TaskRootObject {

    public int status;
    public String message;
    public List<DatumTask> data ;

    public List<DatumTask> getData() {
        return data;
    }

    public void setData(List<DatumTask> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
