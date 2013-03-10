package com.coderzheaven.pack;

import java.text.SimpleDateFormat;
import java.util.*;

import android.view.MotionEvent;
import android.widget.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;

public class JuminVelib extends ListActivity implements View.OnTouchListener  {

    private static String XML_URL = "http://www.velib.paris.fr/service/stationdetails/paris/";

    private long updatedSeconds = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button b = (Button) findViewById(R.id.bouton);
        b.setOnTouchListener(this);

        refresh();
    }

    // Fonction qui sera lancée à chaque fois qu'un toucher est détecté sur le bouton rattaché
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            refresh();
        }
        return true;
    }

    private void refresh()
    {
        Station [] stations = new Station[2];
        stations[0] = new Station(19018, "Avenue Jean Jaures");
        stations[1] =  new Station(19019, "Rue Petit");

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (int j = 0; j < stations.length; j++) {
            String xml = ParseXMLMethods.getXML(XML_URL + stations[j].id);
            Document doc = ParseXMLMethods.XMLfromString(xml);

//        int numResults = ParseXMLMethods.numResults(doc);
//
//        if((numResults <= 0)){
//            Toast.makeText(JuminVelib.this, "There is no data in the xml file", Toast.LENGTH_LONG).show();
//            finish();
//        }

            NodeList children = doc.getElementsByTagName("station");

            for (int i = 0; i < children.getLength(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                Element e = (Element)children.item(i);
                if (i == 0)
                {
                    Station station = stations[j];
                    station.available = Integer.parseInt(ParseXMLMethods.getValue(e, "available"));
                    station.free = Integer.parseInt(ParseXMLMethods.getValue(e, "free"));
                    station.isOpen = ParseXMLMethods.getValue(e, "open").equals("1");
                    map.put("address", stations[j].address);
                    if (station.isOpen)
                    {
                        map.put("available", "Vélos disponibles : " + station.available);
                        map.put("free", "Places disponibles : " + station.free);
                    }
                    else
                    {
                        map.put("available", "Attention station FERMEE");
                    }

                    list.add(map);
                    if (Integer.parseInt(ParseXMLMethods.getValue(e, "updated")) > updatedSeconds)
                    {
                     //   Log.d(ParseXMLMethods.getValue(e, "updated"), "testt");
                        updatedSeconds = Integer.parseInt(ParseXMLMethods.getValue(e, "updated"));
                    }
                }
            }
        }

        ListAdapter adapter = new SimpleAdapter(this, list , R.layout.list_layout,
                new String[] { "address","available", "free" },
                new int[] { R.id.title, R.id.available_text, R.id.free_text});

        setListAdapter(adapter);

//        final ListView lv = getListView();
//        lv.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                @SuppressWarnings("unchecked")
//                HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);
//                Toast.makeText(JuminVelib.this,o.get("free"), Toast.LENGTH_LONG).show();
//            }
//        });

        TextView t = (TextView) findViewById(R.id.my_update);
        t.setText("My last update : " + getCurrentDate());

        TextView tvelib = (TextView) findViewById(R.id.velib_update);
        tvelib.setText("Velib last update : " + secondsToDate(updatedSeconds));

    }

    private String getCurrentDate()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.FRANCE);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        return sdf.format(date);
    }

    private String secondsToDate(long seconds)
    {
        long millis = seconds * 1000;
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.FRANCE);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}