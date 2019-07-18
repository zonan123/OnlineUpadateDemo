package com.example.fileupdatedemo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import com.example.fileupdatedemo.utils.RecycleViewDecorate;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  public static final String TAG = "testLog";

  private Context mContext;
  private TextView tv;
  private FloatingActionButton fab;
  private RecyclerView recycleView;
  private List<Updatefile> zipfiles = new ArrayList<>();
  private RecycleviewAdapter mAdapter = new RecycleviewAdapter();

  @SuppressLint("HandlerLeak")
  private Handler mhandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (msg != null) {
        Log.d(TAG, "handleMessage: ===" + msg.what);
        switch (msg.what) {
          case 1:
            tv.setText((CharSequence) msg.obj);
            mAdapter.notifyDataSetChanged();
            break;
          case 0:
            tv.setText("No file found");
            break;
        }
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate: ===");
    super.onCreate(savedInstanceState);

    getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);
    mContext = this;
    initView();
    tv = findViewById(R.id.notice_text);
    fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View view) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            Log.d(TAG, "run: ===" + this.toString());
            initData();
          }
        }).start();
      }
    });
  }

  private void initData() {
    Log.d(TAG, "initData: ===");
    zipfiles.clear();
    Uri fileUri = Files.getContentUri("/external");
    String[] projection = new String[]{FileColumns.TITLE, FileColumns.DATA};
    String selection = FileColumns.DATA + " LIKE ?"; //FileColumns.DATA + " LIKE ? AND "+
    String[] args = new String[]{"%.zip"};//"%.zip","%_update"
    String sortOrder = FileColumns.DATE_MODIFIED;
    ContentResolver resolver = mContext.getContentResolver();
    Cursor cursor = resolver.query(fileUri, projection, selection, args, sortOrder);
    if (cursor != null) {
      if (cursor.getCount() == 0) {
        mhandler.sendEmptyMessage(0);
        Log.d(TAG, "send message==0");
      } else {
        while (cursor.moveToNext()) {
          Updatefile updatefile = new Updatefile();
          updatefile.setFileTitle(cursor.getString(cursor.getColumnIndex(FileColumns.TITLE)));
          updatefile.setFileDetails(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));
          //需要判断路径文件是否存在
          File file = new File(updatefile.getFileDetails());
          if (file.exists()) {
            Message message = mhandler.obtainMessage();
            message.what = 1;
            message.obj = updatefile.getFileDetails();
            mhandler.sendMessage(message);
            Log.d(TAG, "send message==1" + updatefile.toString());
            zipfiles.add(updatefile);
            Log.d(TAG, "zipfiles===size " + zipfiles.size());
          }
        }
        cursor.close();
      }
    } else {
      Log.d(TAG, "initData:cursor==null");
    }
  }

  private void initView() {

    Log.d(TAG, "initView: ===");
    recycleView = findViewById(R.id.recycleView);
    recycleView.setLayoutManager(new LinearLayoutManager(mContext));
    recycleView.setAdapter(mAdapter);
    recycleView.addItemDecoration(new RecycleViewDecorate(mContext, 1));
    mAdapter.setItemClickListener(new ItemClickListener() {
      @Override
      public void onItemclick(View view, int position) {
        Log.d(TAG, "onItemclick: fileDetalis===" + zipfiles.get(position).getFileDetails());
      }
    });
  }

  class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.RecycleViewHolder> {

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
      this.itemClickListener = itemClickListener;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      Log.d(TAG, "onCreateViewHolder: ===");
      RecycleViewHolder mholder = new RecycleViewHolder(LayoutInflater.from(mContext).
          inflate(R.layout.item_list, parent, false));
      Log.d(TAG, "onCreateViewHolder: ===" + mholder);
      return mholder;
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, final int position) {
      Log.d(TAG, "onBindViewHolder: ===");

      holder.filedata.setText(zipfiles.get(position).getFileDetails());
      holder.filetitle.setText(zipfiles.get(position).getFileTitle());

      if (itemClickListener != null) {
        Log.d(TAG, "onBindViewHolder: ===itemClickListener===");
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

      TextView filedata;
      TextView filetitle;

      public RecycleViewHolder(View itemView) {
        super(itemView);
        Log.d(TAG, "RecycleViewHolder: itemView" + itemView);
        filedata = itemView.findViewById(R.id.file_data);
        filetitle = itemView.findViewById(R.id.file_title);
      }
    }

  }

  public interface ItemClickListener {

    void onItemclick(View view, int position);
  }
}


