package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = (LineChart) findViewById(R.id.linechart);

//        mChart.setOnChartGestureListener(MainActivity.this);
//        mChart.setOnChartGestureListener(MainActivity.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<String> raw = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();

        try {
            BufferedReader br = null;
            String line = "";
            InputStream is = new URL("https://www.google.com").openStream();
            try {
                br = new BufferedReader(new InputStreamReader(is));

                while ((line = br.readLine()) != null) {
                    raw.add(line); // 쉼표로 구분
                }
                for (int i = 0; i < raw.size(); i++){
                    String [] arr = raw.get(i).split(",");
                    yValues.add(new Entry(i, Float.parseFloat(arr[2])));
                    date.add(arr[0]);
//                    System.out.println(yValues.get(i));
                }
                LineDataSet set1 = new LineDataSet(yValues, "Data set 1");
                mChart.setExtraBottomOffset((float)(raw.size()));
                set1.setFillAlpha(110);
                set1.setColor(Color.RED);
                set1.setLineWidth(3f);
                set1.setValueTextSize(10f);
                set1.setValueTextColor(Color.GREEN);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                LineData data = new LineData(dataSets);

                mChart.setData(data);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }
}