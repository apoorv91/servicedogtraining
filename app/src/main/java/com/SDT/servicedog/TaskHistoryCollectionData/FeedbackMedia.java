package com.SDT.servicedog.TaskHistoryCollectionData;

/**
 * Created by laitkor3 on 20/07/16.
 */
public class FeedbackMedia {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public String id ;
    public String feedback_id ;
    public String media_type ;
    public String media ;
    public String created_at ;
    public String modified_at ;
}
