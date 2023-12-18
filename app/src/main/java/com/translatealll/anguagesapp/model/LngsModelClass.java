package com.translatealll.anguagesapp.model;


public class LngsModelClass {
    String download_status;
    int image;
    String title;

    public LngsModelClass(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getDownloadStatus() {
        return download_status;
    }

    public void setDownloadStatus(String download_status) {
        this.download_status = download_status;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
