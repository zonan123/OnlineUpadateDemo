package com.example.fileupdatedemo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  public static final String TAG = "Mytest";

  private Context mContext;
  private TextView tv;
  private FloatingActionButton fab;
  private RecyclerView recycleView;
  private List<Myfile> zipfiles = new ArrayList<>();
  private RecycleviewAdapter mAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate: ");
    super.onCreate(savedInstanceState);
    getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);
    mContext = this;
    initData();
    initRecycleView();

    tv = findViewById(R.id.notice_text);
    fab = findViewById(R.id.fab);
    fab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        recycleView.setVisibility(View.VISIBLE);
      }
    });
  }

  public void initData() {
    Log.d(TAG, "initData: ");
    Uri fileUri = Files.getContentUri("/external");
    String[] projection = new String[]{FileColumns.TITLE, FileColumns.DATE_MODIFIED,
        FileColumns.DATA};
    String selection = FileColumns.DATA + " LIKE '%.zip'";
    String sortOrder = FileColumns.DATE_MODIFIED;
    ContentResolver resolver = mContext.getContentResolver();
    Cursor cursor = resolver.query(fileUri, projection, selection, null, sortOrder);

    if (cursor != null) {
      Log.d(TAG, "initData: ===cursor");

      while (cursor.moveToNext()) {
        Myfile myfile = new Myfile();
        myfile.setFileTitle(cursor.getString(cursor.getColumnIndex(FileColumns.TITLE)));
        myfile.setFileDate(
            cursor.getString(cursor.getColumnIndex(FileColumns.DATE_MODIFIED)));
        myfile.setFileDetails(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));
        zipfiles.add(myfile);

        Log.d(TAG, "getSearchFiles: ===" + myfile.toString());
        tv.setText(myfile.getFileDetails());

      }
      cursor.close();
    }
  }

  private void initRecycleView() {
    Log.d(TAG, "initRecycleView: ");

    recycleView = findViewById(R.id.recycleView);
    recycleView.setLayoutManager(new LinearLayoutManager(mContext));
    Log.d(TAG, "initRecycleView: ===layoutmanager" + recycleView.getLayoutManager());
    mAdapter = new RecycleviewAdapter(mContext, zipfiles);
    recycleView.setAdapter(mAdapter);
    Log.d(TAG, "initRecycleView: adapter" + recycleView.getAdapter());

    recycleView.addItemDecoration(new RecycleViewDecorate(mContext, 1));
    mAdapter.setItemClickListener(new ItemClickListener() {
      @Override
      public void onItemclick(View view, int position) {
        Log.d(TAG, "onItemclick: fileDetalis===" + zipfiles.get(position).getFileDetails());
      }
    });
  }

  class RecycleviewAdapter extends
      RecyclerView.Adapter<RecycleviewAdapter.RecycleViewHolder> {

    private Context context;
    private List<Myfile> myfileList;

    public void notifyChange() {

    }

    public RecycleviewAdapter(Context context, List<Myfile> mfilesList) {
      this.context = context;
      this.myfileList = mfilesList;
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
      this.itemClickListener = itemClickListener;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      Log.d(TAG, "onCreateViewHolder: ===");
      RecycleViewHolder mholder = new RecycleViewHolder(LayoutInflater.from(mContext).
          inflate(R.layout.item_list, parent, false));
      Log.d(TAG, "onCreateViewHolder: ===" + (mholder != null));
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
        filedate = itemView.findViewById(R.id.file_date);
        filetitle = itemView.findViewById(R.id.file_title);
      }
    }

  }

  public interface ItemClickListener {

    void onItemclick(View view, int position);
  }
}


