package com.example.fileupdatedemo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class FilesUtils {

  public static final String TAG = "UtilsTest";

  public FilesUtils(Context context, String contentUri, String[] extend,
      @Nullable String[] fileClumns, @Nullable String orderSort) {
    this.context = context;
    this.contentUri = contentUri;
    this.extend = extend;
    this.fileClumns = fileClumns;
    this.orderSort = orderSort;
  }

  private Context context;
  private String contentUri;
  private String[] extend;
  private String[] fileClumns;
  private String orderSort;
  private ContentResolver resolver;
  private Uri fileUri = Files.getContentUri(contentUri);
  private String selection = FileColumns.DATA + " LIKE '%?'";

  public List<String[]> getFile() {
    List<String[]> list = new ArrayList<>();
    resolver = context.getContentResolver();
    Cursor cursor = resolver.query(fileUri, fileClumns, selection, extend, orderSort);

    if (cursor != null) {
      String[] obj = new String[cursor.getColumnCount()];
      while (cursor.moveToNext()) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
          obj[i] = cursor.getString(i);
          Log.d(TAG, "getFile: ==="+obj[i]);
        }
      }
      cursor.close();
      list.add(obj);
    }
    return list;
  }
}

