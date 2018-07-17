package com.georgcantor.counter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.georgcantor.counter.adapter.HistoryAdapter;
import com.georgcantor.counter.db.DatabaseHelper;
import com.georgcantor.counter.db.History;
import com.georgcantor.counter.utils.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private List<History> historyList = new ArrayList<>();
    private DatabaseHelper db;
    private TextView mTextViewNoHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_history);
        mTextViewNoHistory = findViewById(R.id.empty_history_tv);

        db = new DatabaseHelper(this);

        historyList.addAll(db.getAllHistory());

        HistoryAdapter mAdapter = new HistoryAdapter(this, historyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this,
                LinearLayoutManager.VERTICAL, 16));
        mRecyclerView.setAdapter(mAdapter);

        toggleEmptyHistory();
    }

    private void toggleEmptyHistory() {
        if (db.getHistoryCount() > 0) {
            mTextViewNoHistory.setVisibility(View.GONE);
        } else {
            mTextViewNoHistory.setVisibility(View.VISIBLE);
        }
    }
}
