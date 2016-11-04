package com.worktime.bublik;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePick extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public int TPhour;
    public int TPminute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // устанавливаем текущее время для TimePicker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // создаем TimePickerDialog и возвращаем его
        Dialog picker = new TimePickerDialog(getActivity(), this, hour, minute, true);
        picker.setTitle(getResources().getString(R.string.choose_time));

        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton =  ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(getResources().getString(R.string.ready));

    }
    @Override
    public void onTimeSet(TimePicker view, int hours, int minute) {
        // Выводим выбранное время
        TPhour = hours;
        TPminute = minute;
    }

}