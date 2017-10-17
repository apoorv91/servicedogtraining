package com.SDT.servicedog.TaskListsCollectionData;
import java.util.List;
/**
 * Created by laitkor3 on 18/07/16.
 */
public class Tasks
{

    public Task2List Task ;
    public UserTask User;
    public List<MediaTasks> Media ;

    public Task2List getTask() {
        return Task;
    }

    public void setTask(Task2List task) {
        Task = task;
    }

    public UserTask getUserTask() {
        return User;
    }

    public void setUserTask(UserTask userTask) {
        User = User;
    }

    public List<MediaTasks> getMedia() {
        return Media;
    }

    public void setMedia(List<MediaTasks> media) {
        Media = media;
    }


}