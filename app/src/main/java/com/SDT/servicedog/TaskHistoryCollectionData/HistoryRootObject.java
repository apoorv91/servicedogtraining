package com.SDT.servicedog.TaskHistoryCollectionData;


import java.util.List;
/**
 * Created by laitkor3 on 20/07/16.
 */

public class HistoryRootObject {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DatumTaskHistory> getData() {
        return data;
    }

    public void setData(List<DatumTaskHistory> data) {
        this.data = data;
    }

    public int status ;
    public String message ;
    public List<DatumTaskHistory> data ;
}
