package com.georgcantor.counter;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonPlus;
    private Button buttonMinus;
    private TextView counterDisplay;
    private int counter = 0;

    private boolean autoIncrement = false;
    private boolean autoDecrement = false;
    private final long REPEAT_DELAY = 50;
    private Handler repeatUpdateHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlus = findViewById(R.id.button_plus);
        buttonMinus = findViewById(R.id.button_minus);
        counterDisplay = findViewById(R.id.textViewCounter);
        counterDisplay.setText("0");

        class RepetitiveUpdater implements Runnable {
            @Override
            public void run() {
                if (autoIncrement) {
                    increment();
                    repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
                } else if (autoDecrement) {
                    decrement();
                    repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
                }
            }
        }

        buttonPlus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                autoIncrement = true;
                repeatUpdateHandler.post(new RepetitiveUpdater());
                return false;
            }
        });

        buttonPlus.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                    autoIncrement = false;
                }
                return false;
            }
        });


        buttonMinus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                autoDecrement = true;
                repeatUpdateHandler.post(new RepetitiveUpdater());
                return false;
            }
        });

        buttonMinus.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                    autoDecrement = false;
                }
                return false;
            }
        });
    }

    public void increment() {
        if (counter < 10000) {
            counter++;
            counterDisplay.setText(String.valueOf(counter));
        }
    }

    public void decrement() {
        if (counter < 10000) {
            counter--;
            counterDisplay.setText(String.valueOf(counter));
        }
    }

    public void plusClick(View view) {
        counter++;
        counterDisplay.setText(Integer.toString(counter));
    }

    public void minusClick(View view) {
        counter--;
        counterDisplay.setText(Integer.toString(counter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set:
                openSetDialog();
                break;
            case R.id.action_reset:
                openResetDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSetDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(R.string.set_count_title);
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                counter = Integer.parseInt(input.getText().toString());
                counterDisplay.setText(Integer.toString(counter));
            }
        });
        alert.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    private void openResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.reset_dialog_message);
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                counter = 0;
                counterDisplay.setText("0");
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }
}
