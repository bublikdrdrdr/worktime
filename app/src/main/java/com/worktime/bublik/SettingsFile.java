package com.worktime.bublik;

import android.content.Context;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Bublik on 23-Jun-16.
 */
public class SettingsFile {

    Settings_v2 sv2;
    byte[] arr;

    public SettingsFile(Context context)
    {
        sv2 = new Settings_v2(Settings_v2.createFileName(context, "settings.bublik"));
        if (sv2.get(0)==null)
        {
            sv2.set(0,true);
            sv2.set(1, (double)0);
            sv2.set(3, 0);
            arr = new byte[0];
            sv2.set(2, arr);
            sv2.Save();
        }
        int n = ((int)sv2.get(3) / 8);
        arr = (byte[])sv2.get(2);
    }

    public long[] getPunchesArray()
    {
        long[] res = new long[arr.length/8];
        for (int i = 0; i < res.length; i++)
        {
            res[i] = bytesToLong(Arrays.copyOfRange(arr, i*8, (i+1)*8));
        }
        return res;
    }

    public void setArray(long[] array)
    {
        arr = new byte[array.length*8];
        for (int i = 0; i < array.length; i++)
        {
            byte[] current = longToBytes(array[i]);
            for (int o = 0; o < 8; o++)
            {
                arr[i*8+o] = current[o];
            }
        }
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

    public void saveFile()
    {
        sv2.set(2, arr);
        sv2.Save();
    }

    public void deleteFile()
    {
        sv2.removeFile();
    }


}
