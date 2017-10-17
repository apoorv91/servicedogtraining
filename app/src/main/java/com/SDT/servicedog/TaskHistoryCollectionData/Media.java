package com.SDT.servicedog.TaskHistoryCollectionData;

/**
 * Created by laitkor3 on 20/07/16.
 */
public class Media {

    public com.SDT.servicedog.TaskHistoryCollectionData.FeedbackMedia getFeedbackMedia() {
        return FeedbackMedia;
    }

    public void setFeedbackMedia(com.SDT.servicedog.TaskHistoryCollectionData.FeedbackMedia feedbackMedia) {
        FeedbackMedia = feedbackMedia;
    }

    public Feedback3 getFeedback() {
        return Feedback;
    }

    public void setFeedback(Feedback3 feedback) {
        Feedback = feedback;
    }

    public FeedbackMedia FeedbackMedia ;
    public Feedback3 Feedback ;
}
