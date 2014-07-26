package bushi.makesapps.workouttimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    private Button mStartButton;
    //private Button mPauseButton;
    private Button mResetButton;

    private TextView timerValue;

    private long startTime = 0L;

    private Handler mHandler = new Handler();

    long timeInMs = 0L;
    long timeSwapBuffer = 0L;
    long updatedTime = 0L;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerValue = (TextView)findViewById(R.id.timerText);

        mStartButton = (Button)findViewById(R.id.btn_Start);

        //mPauseButton = (Button)findViewById(R.id.btn_Pause);

        mStartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String TAG = "StartButton";

                // Case 1 - start
                if (mStartButton.getText().toString().equals("Start")) {
                    //Log.d(TAG, "Case 1 - Start Button is present");
                    mStartButton.setText(R.string.pauseBtnLabel);
                    startTime = SystemClock.uptimeMillis();
                    mHandler.postDelayed(updateTimerThread, 0);

                }
                // Case 2 - Pause
                else
                {
                    mStartButton.setText(R.string.startBtnLabel);
                    timeSwapBuffer += timeInMs;
                    mHandler.removeCallbacks(updateTimerThread);
                }
                // Case 3 - Resume


            }
        });

        mResetButton = (Button)findViewById(R.id.btn_Reset);

        mResetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Case 4 - Reset

                // Reset counters to 0 - Could implement a method for this
                resetCounters();
                mHandler.removeCallbacks(updateTimerThread);

            }
        });

        /*
        mStartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTime = SystemClock.uptimeMillis();
                mHandler.postDelayed(updateTimerThread, 0);
            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timeSwapBuffer += timeInMs;
                mHandler.removeCallbacks(updateTimerThread);
            }
        }); */



    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMs = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuffer + timeInMs;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs%60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            mHandler.postDelayed(this, 0);
        }
    };

    private void resetCounters() {
        timeSwapBuffer = 0L;
        startTime = 0L;
        timeInMs = 0L;
        // Remove Callbacks from Handler


        // Set text back to "Start"
        mStartButton.setText(R.string.startBtnLabel);
        // Reset other values back to 0
        timerValue.setText("0:00:000");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
