package com.example.fileupdatedemo.test;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fileupdatedemo.R;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

  private static final String TAG = "Mytest_test";

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState,
      @Nullable PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
    setContentView(R.layout.test_layout);
    initData();
    RecyclerView testRecyclerView = findViewById(R.id.testRecycle);
    testRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    testRecyclerView.setAdapter(new RecycleviewAdapter());
  }

  List<String> list = new ArrayList<>();

  private void initData() {
    list.clear();
    for (int i = 0; i < 5; i++) {
      list.add("test===" + i);
    }
  }

  class RecycleviewAdapter extends
      RecyclerView.Adapter<TestActivity.RecycleviewAdapter.RecycleViewHolder> {


    @Override
    public TestActivity.RecycleviewAdapter.RecycleViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {

      Log.d(TAG, "onCreateViewHolder: ===");
      TestActivity.RecycleviewAdapter.RecycleViewHolder mholder = new TestActivity.RecycleviewAdapter.RecycleViewHolder(
          LayoutInflater.from(TestActivity.this).
              inflate(R.layout.test_list, parent, false));

      return mholder;
    }

    @Override
    public void onBindViewHolder(final TestActivity.RecycleviewAdapter.RecycleViewHolder holder,
        final int position) {

      Log.d(TAG, "onBindViewHolder: ===");
      holder.testText.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
      Log.d(TAG, "getItemCount: ==="+list.size());
      return list.size();
    }

    class RecycleViewHolder extends ViewHolder {

      TextView testText;

      public RecycleViewHolder(View itemView) {
        super(itemView);
        testText = findViewById(R.id.test_text);
      }
    }

  }
}
