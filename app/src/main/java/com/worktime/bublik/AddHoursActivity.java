package com.worktime.bublik;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddHoursActivity extends AppCompatActivity {


    private Button chooseTimeFrom;
    private Button chooseDateFrom;
    private Button chooseTimeTo;
    private Button chooseDateTo;
    private Button add;
    private Button cancel;

    private TextView fromTimeView;
    private TextView toTimeView;
    private TextView hoursTV;

    private Context m_this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hours);

        chooseDateFrom = (Button) findViewById(R.id.chooseDateFrom);
        chooseTimeFrom = (Button) findViewById(R.id.chooseTimeFrom);
        chooseDateTo = (Button) findViewById(R.id.chooseDateTo);
        chooseTimeTo = (Button) findViewById(R.id.chooseTimeTo);
        add = (Button) findViewById(R.id.add);
        cancel = (Button) findViewById(R.id.cancel);

        chooseDateFrom.setOnClickListener(onClickListener);
        chooseTimeFrom.setOnClickListener(onClickListener);
        chooseDateTo.setOnClickListener(onClickListener);
        chooseTimeTo.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        add.setOnClickListener(onClickListener);
        m_this = this;

    }

    private void exit()
    {
        this.finish();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.add:
                    break;
                case R.id.cancel:
                    exit();
                    break;
                case R.id.chooseDateFrom:
                    break;
                case R.id.chooseDateTo:
                    break;
                case R.id.chooseTimeFrom:
                    TimePick timePick = new TimePick();
                    timePick.show(getSupportFragmentManager(), "timePick");
                    break;
                case R.id.chooseTimeTo:
                    break;
            }
        }
    };
}
