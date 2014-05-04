package com.smartapps.accel;

import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by admin on 5/4/14.
 */
public class TestFragment extends Fragment implements View.OnClickListener{
    private Button btnStartTest, btnStopTest;
    private SensorManager sensorManager;
    private boolean started;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final MainActivity act =(MainActivity) getActivity();
        View v =  inflater.inflate(R.layout.test_fragment, container, false);

        started = false;

        btnStartTest = (Button) v.findViewById(R.id.btnStartTest);
        btnStopTest = (Button) v.findViewById(R.id.btnStopTest);

        btnStartTest.setOnClickListener(this);
        btnStopTest.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTest:
                btnStartTest.setEnabled(false);
                btnStopTest.setEnabled(true);

                // save prev data if available
                started = true;
                ((MainActivity)getActivity()).startSensor();
                break;
            case R.id.btnStopTest:

                btnStartTest.setEnabled(true);
                btnStopTest.setEnabled(false);

                started = false;
                ((MainActivity)getActivity()).endSensor();
                break;
            default:
                break;
        }
    }
}
