package com.example.fileupdatedemo;

import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Updatefile {

  private String fileTitle;
  private String fileDate;
  private String fileDetails;

  public String getFileDetails() {
    return fileDetails;
  }

  public void setFileDetails(String fileDetails) {
    this.fileDetails = fileDetails;
  }

/*
  public String getFileDate() throws ParseException {
    DateFormat df = DateFormat.getDateInstance();
    String date = df.format(df.parse(fileDate));
    Log.d("testLog", "getFileDate: ===getDate" + date);
    return date;
  }*/

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
        ", fileDetails='" + fileDetails + '\'' +
        '}';
  }
}
