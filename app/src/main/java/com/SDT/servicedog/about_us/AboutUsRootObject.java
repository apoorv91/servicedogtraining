package com.SDT.servicedog.about_us;

/**
 * Created by laitkor3 on 23/07/16.
 */
public class AboutUsRootObject {


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

    public AboutUsData getData() {
        return data;
    }

    public void setData(AboutUsData data) {
        this.data = data;
    }

    public int status ;
    public String message ;
    public AboutUsData data ;
}
