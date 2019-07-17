package com.example.fileupdatedemo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fileupdatedemo.test.TestActivity;
import com.example.fileupdatedemo.utils.FilesUtils;
import com.example.fileupdatedemo.utils.RecycleViewDecorate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  public static final String TAG = "Mytest";

  @SuppressLint("HandlerLeak")
  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (msg != null) {
        Log.d(TAG, "handleMessage: ===" + msg.what);
        switch (msg.what) {
          case 1:
            tv.append(":New Version");
//            recycleView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "GET FILE", Toast.LENGTH_SHORT).show();
            break;
          case 0:
            tv.append(":NoFile");
            break;
        }

      }

    }
  };

  private TextView tv;
  private FloatingActionButton fab;
  private RecyclerView recycleView;
  private List<Myfile> zipfiles = new ArrayList<>();
  private RecycleviewAdapter mAdapter = new RecycleviewAdapter();
  private Button testbtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);
    tv = findViewById(R.id.notice_text);
    testbtn = findViewById(R.id.testBtn);
    testbtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, TestActivity.class);
        startActivity(intent);
      }
    });

    fab = findViewById(R.id.fab);
    initRecycleView();
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            zipfiles.clear();
            getSearchFiles();
//            getSearchFilesByutils();
            Message msg = mHandler.obtainMessage();
            if (zipfiles != null && zipfiles.size() != 0) {
              mHandler.sendEmptyMessage(1);
              Log.d(TAG, "run: send message === 1");
            } else {
              mHandler.sendEmptyMessage(0);
              Log.d(TAG, "run: send message === 0");
            }
          }
        }).start();

      }
    });
  }


  private void initRecycleView() {
    recycleView = findViewById(R.id.recycleView);
    recycleView.setLayoutManager(new LinearLayoutManager(this));
    recycleView.setAdapter(mAdapter);
    recycleView.addItemDecoration(new RecycleViewDecorate(this, 1));
    mAdapter.setItemClickListener(new ItemClickListener() {
      @Override
      public void onItemclick(View view, int position) {
        Log.d(TAG, "onItemclick: fileDetalis===" + zipfiles.get(position).getFileDetails());
      }
    });
  }

  private void getSearchFilesByutils() {
    FilesUtils filesUtils = new FilesUtils(this, "external", new String[]{".zip"}, null,
        FileColumns.DATE_MODIFIED);
    List<String[]> list = filesUtils.getFile();
    for (String[] s :list
    ) {
      Log.d(TAG, "getSearchFilesByutils: ==="+s);
    }
  }

  public void getSearchFiles() {

    Uri fileUri = Files.getContentUri("/external");
    String[] projection = new String[]{FileColumns.TITLE, FileColumns.DATE_MODIFIED,
        FileColumns.DATA};
    String selection = FileColumns.DATA + " LIKE '%.zip'";
    String sortOrder = FileColumns.DATE_MODIFIED;
    ContentResolver resolver = this.getContentResolver();
    Cursor cursor = resolver.query(fileUri, projection, selection, null, sortOrder);

    if (cursor != null) {
      while (cursor.moveToNext()) {
        Myfile myfile = new Myfile();
        myfile.setFileTitle(cursor.getString(cursor.getColumnIndex(FileColumns.TITLE)));
        myfile.setFileDate(cursor.getString(cursor.getColumnIndex(FileColumns.DATE_MODIFIED)));
        myfile.setFileDetails(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));

        Log.d(TAG, "getSearchFiles: ===" + myfile.toString());
        zipfiles.add(myfile);
      }
      cursor.close();
    }
  }

  class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.RecycleViewHolder> {

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
      this.itemClickListener = itemClickListener;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      Log.d(TAG, "onCreateViewHolder: ===");
      RecycleViewHolder mholder = new RecycleViewHolder(LayoutInflater.from(MainActivity.this).
          inflate(R.layout.item_list, parent, false));
      Log.d(TAG, "onCreateViewHolder: ==="+(mholder!=null));
      return mholder;
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, final int position) {

      Log.d(TAG, "onBindViewHolder: ===" + holder);
      holder.filedate.setText(zipfiles.get(position).getFileDate());
      Log.d(TAG, "onBindViewHolder: ===" + zipfiles.get(position).getFileDate());
      holder.filetitle.setText(zipfiles.get(position).getFileTitle());

      if (itemClickListener != null) {
        holder.filetitle.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            int pos = holder.getLayoutPosition();
            itemClickListener.onItemclick(holder.filetitle, pos);
          }
        });
      }
    }

    @Override
    public int getItemCount() {

      Log.d(TAG, "getItemCount: ===" + zipfiles.size());
      return zipfiles.size();
    }

    class RecycleViewHolder extends ViewHolder {

      TextView filedate;
      TextView filetitle;

      public RecycleViewHolder(View itemView) {
        super(itemView);
        filedate = findViewById(R.id.file_date);
        filetitle = findViewById(R.id.file_title);
      }
    }

  }

  public interface ItemClickListener {

    void onItemclick(View view, int position);
  }
}


