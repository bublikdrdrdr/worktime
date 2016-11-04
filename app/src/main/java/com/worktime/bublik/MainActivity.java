package com.worktime.bublik;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {


    public Settings_v2 settings;

    public long[] punches;
    public byte[] arr;

    public Date startPayWeekDay;

    public Context c_this;

    public static long timeZone = -21600000;

    public TextView tStatus;
    public TextView tDaysHours;
    public TextView tWeekHours;
    public TextView tPreviousWeekHours;
    public TextView tMoney;
    public EditText perhour;
    public Button calculate;
    public Button punch;
    public Button man_hours;
    public Button schedule;
    public Button delete;

    //tags:
    // 0 - must be true, if file was used
    // 1 - $ per hour
    //2 - array of times
    //3 - array length
    public SettingsFile settingsFile;
    public TimeList timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c_this = this;

        tStatus = (TextView) findViewById(R.id.tStatus);
        tDaysHours = (TextView) findViewById(R.id.tDayHours);
        tWeekHours = (TextView) findViewById(R.id.tWeekHours);
        tMoney = (TextView) findViewById(R.id.tMoney);
        perhour = (EditText) findViewById(R.id.perhour);
        calculate = (Button) findViewById(R.id.calculate);
        punch = (Button) findViewById(R.id.punch);
        man_hours = (Button) findViewById(R.id.man_hours);
        schedule = (Button) findViewById(R.id.schedule);
        delete = (Button) findViewById(R.id.delete);
        tPreviousWeekHours = (TextView) findViewById(R.id.tPreviousWeekHours);

        calculate.setOnClickListener(onClickListener);
        punch.setOnClickListener(onClickListener);
        man_hours.setOnClickListener(onClickListener);
        schedule.setOnClickListener(onClickListener);
        delete.setOnClickListener(onClickListener);




        loadAll();
        ///doThisShit();

    }

    public void loadAll()
    {
        settingsFile = new SettingsFile(this);
        Calendar c = new GregorianCalendar(2016,5,19,0,0,0);
        long pday = c.getTime().getTime();
        timeList = new TimeList(settingsFile.getPunchesArray(), pday);

        if (!timeList.isClockedIn())
        {
            tStatus.setText("You are clocked out");
            tStatus.setTextColor(0xFFFF0000);
        } else
        {
            tStatus.setText("You are clocked in");
            tStatus.setTextColor(0xFF00A810);
        }

        tWeekHours.setText("Hours this pay period: "+ (new DecimalFormat("#0.0").format(timeList.getPayPeriodHours())));

        tDaysHours.setText("Hours this day: " + (new DecimalFormat("#0.0").format(timeList.getDayHours())));

        tPreviousWeekHours.setText("Hours previous pay period: " + (new DecimalFormat("#0.0").format(timeList.getPreviousPayPeriodHours())));

        showMoney();
    }

    public void showMoney()
    {
        try {
            double d = Double.parseDouble(perhour.getText().toString());
            double rs = timeList.getSallary(d, true);
            tMoney.setText("Your money: $" + (new DecimalFormat("#0.00").format(rs)));
        } catch (Exception e)
        {
            tMoney.setText("");
        }
    }

    public void doThisShit()
    {
        settings = new Settings_v2(Settings_v2.createFileName(c_this, "settings.bublik"));
        if (settings.get(0)==null)
        {
            settings.set(0,true);
            settings.set(1, (double)0);
            settings.set(3, 0);
            arr = new byte[0];
            settings.set(2, arr);
            settings.Save();
        }
        int n = (int)((int)settings.get(3) / 8);
        arr = (byte[])settings.get(2);
        punches = new long[n];
        for(int i = 0; i < n; i++)
        {
            punches[i] = bytesToLong(Arrays.copyOfRange(arr, i*8, (i+1)*8));
        }

        Calendar c = new GregorianCalendar(2016,6,19,0,0,0);
        startPayWeekDay = c.getTime();
        Date now = new Date();
        now.setTime(now.getTime()+timeZone);
        while(now.getTime()-startPayWeekDay.getTime()>=14*24*60*60*1000)
        {
            startPayWeekDay.setTime(startPayWeekDay.getTime()+14*24*60*60*1000);
        }

        long previous;
        if (punches.length>0)
        {
             previous = punches[0];
        }

        int index = 0;
        for (int i = 0; i < n; i++)
        {

            if (startPayWeekDay.before(new Date(punches[i])))
            {
                index = i;
            }
            previous = punches[i];
        }

        long suma_2week = 0;
        long suma_day = 0;
        Date today = new Date();
        now.setTime(today.getTime()+timeZone);
        today.setSeconds(0);
        today.setMinutes(0);
        today.setHours(0);

        long[] copyArr = punches.clone();
        if (index%2!=0)
        {
            if (index<=0)
            {
                showMessage("хня якась, вадім хуйовий програміст");
                return;
            }
            copyArr[index-1] = startPayWeekDay.getTime();
            index -= 1;
        }

        try {
            if (copyArr.length % 2 != 0) {
                copyArr = biggerArray(copyArr, (new Date()).getTime()+timeZone);
            }


            for (int i = index; i < n; i += 2) {
                suma_2week += copyArr[i + 1] - copyArr[i];
                if (today.before(new Date(copyArr[i]))) {
                    suma_day += copyArr[i + 1] - copyArr[i];
                } else {
                    if (today.before(new Date(copyArr[i + 1]))) {
                        suma_day += copyArr[i + 1] - today.getTime();
                    }
                }
            }
        } catch (Exception e)
        {
            showMessage("якийсь піздєц, покажіть вадіму: " + e.getLocalizedMessage());
            return;
        }

        if (punches.length%2==0)
        {
            tStatus.setText("You are clocked out");
            tStatus.setTextColor(0xFFFF0000);
        } else
        {
            tStatus.setText("You are clocked in");
            tStatus.setTextColor(0xFF00A810);
        }

        double week = suma_2week/1000;
        week = week/60.;
        week = week/60.;
        tWeekHours.setText("Hours this pay period: "+ (new DecimalFormat("#0.0").format(week)));

        double day = suma_day/1000;
        day = day/60.;
        day = day/60.;
        tDaysHours.setText("Hours this day: " + (new DecimalFormat("#0.0").format(day)));


        try {
            tMoney.setText("Your money: $" + (new DecimalFormat("#0.00").format((Double.parseDouble(perhour.getText().toString()) * week))));
        } catch (Exception e)
        {
            tMoney.setText("");
        }
    }

    public long[] biggerArray(long[] barr, long last_value)
    {
        long[] newarr = new long[barr.length+1];
        for (int i = 0; i < barr.length; i++)
        {
            newarr[i] = barr[i];
        }
        newarr[newarr.length-1] = last_value;
        return newarr;
    }

    public double[] getHours(long[] array)
    {
        double[] ret = new double[3];
        return ret;
    }



    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public void showMessage(String message)
    {
        Toast.makeText(c_this, message, Toast.LENGTH_LONG).show();
    }

    public void showFastMessage(String message)
    {
        Toast.makeText(c_this, message, Toast.LENGTH_SHORT).show();
    }



    long duration = 1000*2;
    long lastPunchClick = new Date().getTime();
    int punchClicks = 0;

    long deleteDuration = 3000;
    long lastDeleteClick = new Date().getTime();
    int deleteClicks = 0;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.punch:
                    if ((new Date()).getTime()-lastPunchClick>duration)
                    {
                        punchClicks = 0;

                    }

                    if ((punchClicks == 0)) {
                        showFastMessage("click one more time to continue");
                        punchClicks++;
                    } else {
                        punchClicks = 0;
                        timeList.setPunch();
                        settingsFile.setArray(timeList.getLongArray());
                        settingsFile.saveFile();
                        loadAll();
                    }
                    lastPunchClick = new Date().getTime();

                    break;
                case R.id.calculate:
                    showMoney();
                    break;
                case R.id.man_hours:
                    //show new activity and add hours
                    showAddHours();
                    break;
                case R.id.schedule:
                    //show new activity with schedule
                    showSchedule();
                    break;
                case R.id.delete:

                    if ((new Date()).getTime() - lastDeleteClick > deleteDuration)
                    {
                        deleteClicks = 0;
                    }

                    switch (deleteClicks)
                    {
                        case 0:
                            showFastMessage("click 2 more times, ALL DATA WILL BE DELETED!!!");
                            deleteClicks++;
                            lastDeleteClick = new Date().getTime();
                            break;
                        case 1:
                            showFastMessage("click 1 more time, ALL DATA WILL BE DELETED!!!");
                            deleteClicks++;
                            break;
                        case 2:
                            settingsFile.deleteFile();
                            System.exit(0);
                            break;
                    }


                    break;
                    /*punches = biggerArray(punches, (new Date()).getTime());
                    arr = new byte[punches.length*8];
                    for (int i = 0; i < punches.length; i++)
                    {
                        byte[] tarr = longToBytes(punches[i]);
                        for (int o = 0; o < 8; o++)
                        {
                            arr[i+o] = tarr[o];
                        }
                    }
                    settings.set(2,arr);
                    settings.set(3,arr.length);
                    settings.Save();
                    doThisShit();
                    break;*/
            }
        }
    };


    public void showSchedule()
    {
        Intent intent = new Intent(MainActivity.this, Schedule.class);
        intent.putExtra("schedule", timeList.getLongArray());
        startActivity(intent);
    }

    public void showAddHours()
    {
        Intent intent = new Intent(MainActivity.this, AddHoursActivity.class);
        startActivity(intent);
    }
}
