package com.SDT.servicedog;

public class State {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String state;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String status;

    public String getCreated_at() {
        return created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String created_at;

    public String getModified_at() {
        return modified_at;
    }
    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }
    public String modified_at;
}
