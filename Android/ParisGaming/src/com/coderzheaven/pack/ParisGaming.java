package com.coderzheaven.pack;

import java.io.IOException;
import java.sql.SQLException;

import android.app.Activity;
import android.view.MotionEvent;
import android.widget.*;
import android.os.Bundle;
import android.view.View;

public class ParisGaming extends Activity implements View.OnTouchListener  {

    private ImageView imageView;
    private Button refreshButton;

    private DataBaseHelper currentDbHelper;

    private Place currentPlace;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        refreshButton = (Button) findViewById(R.id.bouton);
        refreshButton.setOnTouchListener(this);

        imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.setOnTouchListener(this);

        copyDB();

    }

    // Fonction qui sera lancée à chaque fois qu'un toucher est détecté sur le bouton rattaché
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(view.equals(imageView))
            {
                 if(currentPlace != null)
                 {
                     float goodX = imageView.getWidth() * currentPlace.map_x;
                     float goodY = imageView.getHeight() * currentPlace.map_y;
                     float currentX = event.getX();
                     float currentY = event.getY();
                     double finalement = Math.sqrt(Math.pow(goodX - currentX, 2) + Math.pow(goodY - currentY, 2));
                     TextView t = (TextView) findViewById(R.id.placeName);
                     t.setText("distance : " + finalement);
                 }
            }
            else if(view.equals(refreshButton))
            {
                currentPlace = currentDbHelper.getRandomPlace();
                TextView t = (TextView) findViewById(R.id.placeName);
                t.setText("Ou se trouve " + currentPlace.name + " ?");
               // showthesqllove();
            }
        }
        return true;
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
}