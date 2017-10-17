package com.SDT.servicedog;

import java.util.List;

public class RootObject {

	public int status ;
    public String message ;
    public List<Datum> data ;
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
	public List<Datum> getData() {
		return data;
	}
	public void setData(List<Datum> data) {
		this.data = data;
	}
}


