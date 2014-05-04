package com.smartapps.accel;

import android.app.Fragment;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by admin on 5/4/14.
 */
public class TestFragment extends Fragment {
    private Button btnStartTest, btnStopTest;
    private boolean started = false;
    private SensorManager sensorManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity act =(MainActivity) getActivity();
        return inflater.inflate(R.layout.test_fragment, container, false);

    }















    }
