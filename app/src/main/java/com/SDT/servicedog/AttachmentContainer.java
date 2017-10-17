package com.SDT.servicedog;

import java.util.ArrayList;

/**
 * Created by laitkor3 on 25/08/16.
 */
public class AttachmentContainer {

    private String task_id;
    private ArrayList<String> imageArray;
    private ArrayList<String> videoArray;
    private ArrayList<String> audioArray;
    private ArrayList<String> docArray;
    private ArrayList<String> pdfArray;
    private ArrayList<String> storylineArray;

    public String getTask_id() {
        return task_id;
    }
    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }



    public ArrayList<String> getImageArray() {
        return imageArray;
    }
    public void setImageArray(ArrayList<String> imageArray) {
        this.imageArray = imageArray;
    }



    public ArrayList<String> getVideoArray() {
        return videoArray;
    }
    public void setVideoArray(ArrayList<String> videoArray) {
        this.videoArray = videoArray;
    }



    public ArrayList<String> getAudioArray() {
        return audioArray;
    }
    public void setAudioArray(ArrayList<String> audioArray) {
        this.audioArray = audioArray;
    }



    public ArrayList<String> getDocArray() {
        return docArray;
    }

    public void setDocArray(ArrayList<String> docArray) {
        this.docArray = docArray;
    }



    public ArrayList<String> getPdfArray() {
        return pdfArray;
    }

    public void setPdfArray(ArrayList<String> pdfArray) {
        this.pdfArray = pdfArray;
    }



    public ArrayList<String> getStorylineArray() {
        return pdfArray;
    }

    public void setStorylineArray(ArrayList<String> storylineArray) {
        this.storylineArray = storylineArray;
    }
}
