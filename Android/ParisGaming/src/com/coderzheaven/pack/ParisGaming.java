package com.coderzheaven.pack;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.os.Bundle;
import android.view.View;

public class ParisGaming extends Activity implements View.OnTouchListener  {

    private ImageView imageView;
//    private Button refreshButton;

    private DataBaseHelper currentDbHelper;

    private Place currentPlace;
    private Boolean _isImageToucheEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

//        refreshButton = (Button) findViewById(R.id.bouton);
//        refreshButton.setOnTouchListener(this);

        imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.setOnTouchListener(this);
        copyDB();
        getNewPlace();
    }

    // Fonction qui sera lancée à chaque fois qu'un toucher est détecté sur le bouton rattaché
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(view.equals(imageView) && _isImageToucheEnabled)
            {
                 if(currentPlace != null)
                 {
                     _isImageToucheEnabled = false;
                     float goodX = imageView.getWidth() * currentPlace.map_x;
                     float goodY = imageView.getHeight() * currentPlace.map_y;
                     float currentX = event.getX();
                     float currentY = event.getY();
                     double finalement = Math.sqrt(Math.pow(goodX - currentX, 2) + Math.pow(goodY - currentY, 2));
                     TextView t = (TextView) findViewById(R.id.score);
                     t.setText("distance : " + finalement);
                     startTimer();
                 }
            }
//            else if(view.equals(refreshButton))
//            {
//                getNewPlace();
//            }
        }
        return true;
    }

    private void startTimer(){
        MyCount counter = new MyCount(5000,1000);
        counter.start();
    }

    private void getNewPlace(){
        currentPlace = currentDbHelper.getRandomPlace();
        TextView t = (TextView) findViewById(R.id.placeName);
        t.setText("Ou se trouve " + currentPlace.name + " ?");
        _isImageToucheEnabled = true;
    }

    private void copyDB()
    {
        currentDbHelper = new DataBaseHelper(this);

        try {

            currentDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
        try {
            currentDbHelper.openDataBase();
        }catch(SQLException sqle){

            throw new Error("Unable to create database sql exception");

        }
    }

    private void rotate()
    {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        imageView.startAnimation(rotation);
    }

    //countdowntimer is an abstract class, so extend it and fill in methods
    public class MyCount extends CountDownTimer{
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            getNewPlace();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }
}