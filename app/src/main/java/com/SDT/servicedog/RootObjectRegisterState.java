package com.SDT.servicedog;

/**
 * Created by laitkor on 21/09/16.
 */

import java.util.List;

public class RootObjectRegisterState {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String message;

    public List<DatumState> getData() {
        return data;
    }

    public void setData(List<DatumState> data) {
        this.data = data;
    }

    public List<DatumState> data;

}

