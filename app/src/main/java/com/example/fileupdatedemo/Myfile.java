package com.example.fileupdatedemo;

public class Myfile {

  private String fileTitle;
  private String fileDate;
  private String fileDetails;

  public String getFileDetails() {
    return fileDetails;
  }

  public void setFileDetails(String fileDetails) {
    this.fileDetails = fileDetails;
  }


  public String getFileDate() {
    return fileDate;
  }

  public void setFileDate(String fileDate) {
    this.fileDate = fileDate;
  }


  public String getFileTitle() {
    return fileTitle;
  }

  public void setFileTitle(String fileTitle) {
    this.fileTitle = fileTitle;
  }

  @Override
  public String toString() {
    return "Myfile{" +
        "fileTitle='" + fileTitle + '\'' +
        ", fileDate='" + fileDate + '\'' +
        ", fileDetails='" + fileDetails + '\'' +
        '}';
  }
}
