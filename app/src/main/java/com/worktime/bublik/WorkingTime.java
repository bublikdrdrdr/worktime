package com.worktime.bublik;

import java.util.Date;

/**
 * Created by Bublik on 23-Jun-16.
 */
public class WorkingTime {

    public long start;
    public long end;
    public boolean ok;

    public WorkingTime(long start, long end)
    {
        if(end==-1)
        {
            ok=true;
            this.start = start;
            this.end = end;
        } else {
            if (start <= end) {
                this.start = start;
                this.end = end;
                ok = true;
            } else {
                this.start = 0;
                this.end = 0;
                ok = false;
            }
        }
    }

    public long getDuration()
    {
        if (end!=-1) {
            return end - start;
        } else
        {
            Date d = new Date();
            d.setTime(d.getTime()+MainActivity.timeZone);
            return d.getTime() - start;
        }
    }

    public boolean isInside(long time)
    {
        if (end==-1)
        {
            if (start<=time)
            {
                return true;
            } else{
                return false;
            }
        } else {
            if ((start <= time) && (end >= time)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public long getDurationBetweenTimes(long from, long to)
    {
        if (!ok) return 0;
      //  from = from - 4*60*60*1000;
        long timefrom;
        long timeto;
        if (from<start)
        {
            timefrom = start;
        } else
        {
            timefrom = from;
        }

        if (end!=-1) {
            if (to < end) {
                timeto = to;
            } else {
                timeto = end;
            }
        } else
        {
            timeto = to;
        }

        if (timeto<timefrom) {
            return 0;
        } else {
            return timeto - timefrom;
        }
    }
}
