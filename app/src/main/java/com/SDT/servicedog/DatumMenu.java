package com.SDT.servicedog;

import com.SDT.servicedog.TaskListsCollectionData.MediaTasks;

import java.util.List;

/**
 * Created by laitkor on 12/12/16.
 */

public class DatumMenu {

    public DatumSubMenu Page;
    public List<MediaTasks> PageMedia;


    public DatumSubMenu getPage() {
        return Page;
    }

    public void setPage(DatumSubMenu page) {
        Page = page;
    }



    public List<MediaTasks> getPageMedia() {
        return PageMedia;
    }

    public void setPageMedia(List<MediaTasks> pageMedia) {
        PageMedia = pageMedia;
    }


}
