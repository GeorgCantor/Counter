package com.georgcantor.counter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.georgcantor.counter.adapter.HistoryAdapter;
import com.georgcantor.counter.db.DatabaseHelper;
import com.georgcantor.counter.db.History;
import com.georgcantor.counter.utils.MyDividerItemDecoration;
import com.georgcantor.counter.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter adapter;
    private List<History> historyList = new ArrayList<>();
    private DatabaseHelper db;
    private TextView mTextViewNoHistory;
    private History history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        history = new History();

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_history);
        mTextViewNoHistory = findViewById(R.id.empty_history_tv);

        db = new DatabaseHelper(this);

        historyList.addAll(db.getAllHistory());

        adapter = new HistoryAdapter(this, historyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this,
                LinearLayoutManager.VERTICAL, 16));
        mRecyclerView.setAdapter(adapter);

        toggleEmptyHistory();

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showUpdateDialog(true, history, position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showDeleteDialog(history, position);
            }
        }));
    }

    private void showDeleteDialog(final History history, final int position) {
        final History historyNote = historyList.get(position);

        AlertDialog alertDialog = new AlertDialog.Builder(HistoryActivity.this).create();
        alertDialog.setMessage("Удалить запись?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Да",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteHistory(historyNote);
                        historyList.set(position, historyNote);
                        adapter.notifyItemChanged(position);
                        restartActivity();
                        toggleEmptyHistory();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Отмена",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.show();
    }

    private void restartActivity() {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateHistoryNote(String time, int position) {
        History historyNote = historyList.get(position);
        historyNote.setTime(time);
        db.updateHistory(historyNote);

        historyList.set(position, historyNote);
        adapter.notifyItemChanged(position);
        toggleEmptyHistory();
    }

    private void showUpdateDialog(final boolean shouldUpdate,
                                  final History history, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.history_note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput =
                new AlertDialog.Builder(HistoryActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);

        if (shouldUpdate && history != null) {
            inputNote.setInputType(InputType.TYPE_CLASS_NUMBER);
            inputNote.setRawInputType(Configuration.KEYBOARD_12KEY);
            inputNote.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            inputNote.setText(history.getTime());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "обновить" : "сохранить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                            }
                        })
                .setNegativeButton("отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(inputNote.getText().toString())) {
                            Toast.makeText(HistoryActivity.this, "Введите время!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            alertDialog.dismiss();
                        }

                        if (shouldUpdate && history != null) {
                            updateHistoryNote(inputNote.getText().toString(), position);
                        }
                    }
                });
    }

    private void toggleEmptyHistory() {
        if (db.getHistoryCount() > 0) {
            mTextViewNoHistory.setVisibility(View.GONE);
        } else {
            mTextViewNoHistory.setVisibility(View.VISIBLE);
        }
    }
}
