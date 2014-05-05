package com.smartapps.accel;

import android.app.Fragment;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by admin on 5/4/14.
 */
public class TestFragment extends Fragment implements View.OnClickListener{
    private Button btnStartTest, btnStopTest, btnGoToResults;
    private SensorManager sensorManager;
    private boolean started;

    private ArrayList<AccelData> trainingData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final MainActivity act =(MainActivity) getActivity();
        trainingData = act.getTrainingData();
        View v =  inflater.inflate(R.layout.test_fragment, container, false);

        started = false;

        btnStartTest = (Button) v.findViewById(R.id.btnStartTest);
        btnStopTest = (Button) v.findViewById(R.id.btnStopTest);
        btnGoToResults = (Button) v.findViewById(R.id.from_test_to_results);

        btnStartTest.setOnClickListener(this);
        btnStopTest.setOnClickListener(this);
        btnGoToResults.setOnClickListener(this);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activ = (MainActivity)getActivity();
        activ.setTitle("Test");

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTest:
                btnStartTest.setEnabled(false);
                btnStopTest.setEnabled(true);

                ((MainActivity)getActivity()).setTestDataTemp(new ArrayList<AccelData>());
                // save prev data if available
                started = true;
                ((MainActivity)getActivity()).startSensor();
                break;
            case R.id.btnStopTest:

                btnStartTest.setEnabled(true);
                btnStopTest.setEnabled(false);
                started = false;
                MainActivity activ = ((MainActivity)getActivity());

                //First stop accelerometer
                activ.endSensor();
                //Second KNN - let's classify this points :)
                activ.classifyData(activ.getTrainingData(), activ.getTestDataTemp());
                //Third oh! Now we need to know which group of data this belongs...
                activ.whereDoYouBelong();
                break;
            case R.id.from_test_to_results:
                ResultsFragment frag = new ResultsFragment();
                ((MainActivity)getActivity()).switchContent(frag);
            default:
                break;
        }
    }
}
