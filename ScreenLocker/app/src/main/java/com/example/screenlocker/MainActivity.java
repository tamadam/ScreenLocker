package com.example.screenlocker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.lang.Thread;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String STATE_KEY_PASSWORD = "com.example.screenlocker.password";
    private final static String SAVE = "text.android.app";

    String password = "";
    public static PatternLockView patternLockView;
    private boolean setPattern = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        patternLockView = findViewById(R.id.patternView);


        if (savedInstanceState!=null){
            password = savedInstanceState.getString(STATE_KEY_PASSWORD);
        }
        else{
            password = "012";
        }

        SharedPreferences settings = getSharedPreferences(SAVE, 0);
        password = settings.getString(STATE_KEY_PASSWORD, "012");


        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List progressPattern) {

            }

            @Override
            public void onComplete(List pattern) {


                Intent intent = new Intent(MainActivity.this, SensorsActivity.class);

                if (setPattern) {
                    Log.d(getClass().getName(), "Pattern complete: " +
                            PatternLockUtils.patternToString(patternLockView, pattern));
                    if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase(password)) {
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                        Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } else {
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                        Toast.makeText(MainActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    patternLockView.setPathEndAnimationDuration(100);
                    Log.d(getClass().getName(), "Pattern complete: " +
                            PatternLockUtils.patternToString(patternLockView, pattern));
                    password = PatternLockUtils.patternToString(patternLockView,pattern);
                    Toast.makeText(MainActivity.this, "Pattern changed", Toast.LENGTH_SHORT).show();

                    setPattern = true;
                }
            }

            @Override
            public void onCleared() {
                Log.d(getClass().getName(), "CLEARED");
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(SAVE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(STATE_KEY_PASSWORD, password);

        // Commit the edits!
        editor.commit();
    }

    public void handleSetPatternButton(View view)
    {
        setPattern = false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_KEY_PASSWORD, password);
    }



}