package com.georgcantor.counter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.georgcantor.counter.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private TextView counterDisplay;
    private int counter;
    private boolean autoIncrement = false;
    private boolean autoDecrement = false;
    private final long REPEAT_DELAY = 50;
    private Handler repeatUpdateHandler = new Handler();
    private boolean doubleTap = false;
    private Chronometer chronometer;
    private boolean isStart;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);

        Button buttonPlus = findViewById(R.id.button_plus);
        Button buttonMinus = findViewById(R.id.button_minus);
        counterDisplay = findViewById(R.id.textViewCounter);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int restoreMinutes = sharedPref.getInt("minutes", 0);
        int restoreCount = sharedPref.getInt("counter", counter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int minutes = extras.getInt("timer");
            if (minutes != 0) {
                counter += minutes;
                counterDisplay.setText(Integer.toString(counter));
            }
        }

        if (restoreCount != 0) {
            counter = restoreCount;
            counterDisplay.setText(Integer.toString(restoreCount));

            if (restoreMinutes != 0) {
                counter += restoreMinutes;
                counterDisplay.setText(Integer.toString(counter));
            }
        } else {
            counterDisplay.setText("0");
        }

        chronometer = findViewById(R.id.my_chrono);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                chronometer = chronometerChanged;
            }
        });


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

    public void startStopChronometer(View view) {
        if (isStart) {
            chronometer.stop();
            isStart = false;
            ((Button) view).setText(R.string.start_chrono);
            long timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
            int hours = (int) (timeElapsed / 3600000);
            int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class).putExtra("timer", minutes);
            startActivity(intent);
            Toast.makeText(this, Integer.toString(minutes), Toast.LENGTH_SHORT).show();
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isStart = true;
            ((Button) view).setText(R.string.stop_chrono);
        }
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
            case R.id.action_add:
                openAddToHistoryDialog();
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
                try {
                    counter = Integer.parseInt(input.getText().toString());
                    counterDisplay.setText(Integer.toString(counter));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, R.string.enter_int_toast,
                            Toast.LENGTH_SHORT).show();
                }
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

    private void openAddToHistoryDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(R.string.set_time_title);
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        final Editable time = input.getText();
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                db.insertHistory(String.valueOf(time));
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        alert.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
        int hours = (int) (timeElapsed / 3600000);
        int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
        SharedPreferences.Editor sharedPref = this.getPreferences(Context.MODE_PRIVATE).edit();
        sharedPref.putInt("counter", counter);
        sharedPref.putInt("minutes", minutes);
        sharedPref.apply();
    }

    @Override
    public void onBackPressed() {
        if (doubleTap) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.press_back),
                    Toast.LENGTH_SHORT).show();
            doubleTap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTap = false;
                }
            }, 2000);
        }
    }
}
