package com.worktime.bublik;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        doThisShit();
    }

    private void doThisShit()
    {
        Intent intent = getIntent();
        long[] arr = intent.getLongArrayExtra("schedule");
        if (arr!=null)
        {
            int firstColor = 0xFFFFFFFF;
            int secondColor = 0xFFDDDDDD;
            int clockedColor = 0xFF59E05C;
            int resColor;
            boolean color = false;
            LinearLayout punchList = (LinearLayout) findViewById(R.id.punchList);
            punchList.removeAllViews();
            for (int i = 0; i < arr.length; i++)
            {
                TextView textView = new TextView(this);
                if (i>=2)
                {
                    if (nextDay(arr[i-2], arr[i]))
                    {
                        if (color==true)
                        {
                            color = false;
                        } else
                        {
                            color = true;
                        }
                    }
                }

                if (color) {
                    resColor = secondColor;
                } else
                {
                    resColor = firstColor;
                }


                SimpleDateFormat dateFormat = new SimpleDateFormat("d.M.yy HH:mm:ss");

                String res;
                if (i==arr.length-1)
                {
                    res = dateFormat.format(new Date(arr[i]-MainActivity.timeZone)) + "  -  now  (" + (new DecimalFormat("#0.0")).format(((double)((((new Date()).getTime()+MainActivity.timeZone)-arr[i])/1000))/3600) + "h)";
                    resColor = clockedColor;
                } else {
                    res = dateFormat.format(new Date(arr[i]-MainActivity.timeZone)) + "  -  " +
                            dateFormat.format(new Date(arr[i+1]-MainActivity.timeZone)) + "  (" + (new DecimalFormat("#0.0")).format(((double)((arr[i+1]-arr[i])/1000))/3600) + "h)";
                    i++;
                }
                textView.setText(res);

                textView.setBackground(new ColorDrawable(resColor));
                punchList.addView(textView);
            }
        }
    }

    private boolean nextDay(long first, long second)
    {
        Date firstDate = new Date(first);
        Date secondDate = new Date(second);
        if (firstDate.getDay()!=secondDate.getDay())
        {
            return true;
        } else
        {
            return false;
        }
    }
}
