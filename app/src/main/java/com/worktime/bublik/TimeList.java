package com.worktime.bublik;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bublik on 23-Jun-16.
 */
public class TimeList {

    List<WorkingTime> list = new LinkedList<WorkingTime>();
    long startpaytime;

    public TimeList(long[] array, long payday)
    {
        for (int i = 0; i < array.length; i+=2)
        {
            if (i!=array.length-1)
            {
                list.add(new WorkingTime(array[i], array[i+1]));
            } else
            {
                list.add(new WorkingTime(array[i], -1));
            }
        }


        long now = (new Date()).getTime()+MainActivity.timeZone;
        while (now-payday>(14*1000*60*60*24))
        {
            payday+=14*1000*60*60*24;
        }
        startpaytime = payday;

        //test:
        String messageToEmployer = "if you are bored and reading this code (my CV background)" +
                "so what i can tell you... this is real code from my last project" +
                "just hire me to work :)";
    }

    public double getPayPeriodHours()
    {
        double res = 0;
        long sum = 0;
        long now = (new Date()).getTime()+MainActivity.timeZone;
        Iterator<WorkingTime> iterator = list.iterator();
        while (iterator.hasNext())
        {
            WorkingTime wt = iterator.next();
            sum += wt.getDurationBetweenTimes(startpaytime, now);
        }

        res = sum/1000.;
        res = res/(60.*60);
        return res;
    }


    public double getPreviousPayPeriodHours()
    {
        double res = 0;
        long sum = 0;
        long to = startpaytime;
        long from = startpaytime-14*24*60*60*1000;
        Iterator<WorkingTime> iterator = list.iterator();
        while (iterator.hasNext())
        {
            WorkingTime wt = iterator.next();
            sum += wt.getDurationBetweenTimes(from, to);
        }

        res = sum/1000.;
        res = res/(60.*60);
        return res;
    }

    public long[] getLongArray()
    {
        int n;
        boolean in = this.isClockedIn();
        if (in)
        {
            n = list.size()*2-1;
        } else
        {
            n = list.size()*2;
        }

        long[] res = new long[n];

        int i = 0;
        Iterator<WorkingTime> iterator = list.iterator();
        while (iterator.hasNext()) {
            WorkingTime wt = iterator.next();
            res[i] = wt.start;
            i++;
            if (i<=res.length-1)
            {
                res[i] = wt.end;
                i++;
            }
        }
        return res;

    }

    public double getDayHours()
    {
        long from;
        Date date = new Date();
        date.setTime(date.getTime() + MainActivity.timeZone);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setTime(date.getTime() - 1000 * 60 * 60 * 4);
        date.setTime(date.getTime()/1000);
        date.setTime(date.getTime()*1000);


        from = date.getTime();
        long now = (new Date()).getTime()+MainActivity.timeZone;


        double res = 0;
        long sum = 0;
        Iterator<WorkingTime> iterator = list.iterator();
        while (iterator.hasNext())
        {
            WorkingTime wt = iterator.next();
            sum += wt.getDurationBetweenTimes(from, now);
        }

        res = sum/1000.;
        res = res/(60.*60);
        return res;
    }

    public boolean isClockedIn()
    {
        if (list.size()==0)
        {
            return false;
        } else {
            if (list.get(list.size() - 1).end==-1)
            {
                return true;
            } else
            {
                return false;
            }
        }
    }

    public double getSallary(double perhour, boolean overtime)
    {
        double res = 0;
        if (overtime)
        {
            if (this.getPayPeriodHours()>=80)
            {
                return 80*perhour + (this.getPayPeriodHours()-80)*perhour*1.5;
            } else
            {
                return perhour*this.getPayPeriodHours();
            }
        } else
        {
            return perhour*this.getPayPeriodHours();
        }
    }

    public void setPunch()
    {
        if (this.isClockedIn())
        {
            list.get(list.size()-1).end = (new Date()).getTime()+MainActivity.timeZone;
        } else
        {
            list.add(new WorkingTime((new Date()).getTime()+MainActivity.timeZone, -1));
        }
    }

    public boolean tryAddHours(long start, long end)
    {
        if (getDurationBetweenTimes(start, end)>0)
        {
            return false;
        } else
        {
            int index = 0;
            Iterator<WorkingTime> iterator = list.iterator();
            while (iterator.hasNext()) {
                WorkingTime wt = iterator.next();
                if (wt.start>end)
                {
                    break;
                }
                if (wt.end<start)
                {
                    index++;
                    break;
                }
                index++;
            }
            list.add(index, new WorkingTime(start, end));
            return true;
        }
    }


    public long getDurationBetweenTimes(long start, long end)
    {
        long sum = 0;
        Iterator<WorkingTime> iterator = list.iterator();
        while (iterator.hasNext())
        {
            WorkingTime wt = iterator.next();
            sum += wt.getDurationBetweenTimes(start, end);
        }
        return sum;
    }
}
