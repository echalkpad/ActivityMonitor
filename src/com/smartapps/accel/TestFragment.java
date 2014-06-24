package com.smartapps.accel;

import android.app.Fragment;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Second page of our app
 * Allows the user to test the algorithm, doing a movement without saying to the app what movement is...
 * then with the algorithm... the app will know which movement is (idle, walk or run)
 */
public class TestFragment extends Fragment implements View.OnClickListener{

    private static final String MSG_NO_TEST = "You didn't perform a test. Please click button Start.";
    private static final String MSG_TEST_NOT_FINISHED = "You're test is not complete. If you want to finish, please select stop.";
    private static final String MSG_MORE_THAN_3_TESTS = "At least 3 tests were performed. If you start a new test, the data will be overwritten.";

    private Button btnStartTest, btnStopTest, btnGoToResults;
    private int nrOfTests;
    private boolean started;


    private ArrayList<AccelData> trainingData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final MainActivity act =(MainActivity) getActivity();
        trainingData = act.getTrainingData();
        View v =  inflater.inflate(R.layout.test_fragment, container, false);

        nrOfTests = 0;
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


    /**
	* Here we say what the app needs to do when the user clicks in a button
	* btnStart is the button to the start the test - the app will start collecting data
	* btnStop is the button to stop the test - no more data to collect and then we classify the data
	*/
    @Override
    public void onClick(View v) {

        MainActivity activ = ((MainActivity) getActivity());
        switch (v.getId()) {
            case R.id.btnStartTest:

                if(nrOfTests >= 3){
                    Toast.makeText(activ.getApplicationContext(), MSG_MORE_THAN_3_TESTS,Toast.LENGTH_LONG).show();
                }else {
                    btnStartTest.setEnabled(false);
                    btnStopTest.setEnabled(true);

                    activ.setTestDataTemp(new ArrayList<AccelData>());
                    // save prev data if available
                    started = true;
                    nrOfTests++;
                    ((MainActivity) getActivity()).startSensor();
                }
                break;
            case R.id.btnStopTest:

                btnStartTest.setEnabled(true);
                btnStopTest.setEnabled(false);
                started = false;

                //First stop accelerometer
                activ.endSensor();
                //Second KNN - let's classify this points :)
                activ.classifyData(activ.getTrainingData(), activ.getTestDataTemp());
                //Third oh! Now we need to know which group of data this belongs...
                activ.whereDoYouBelong();
                break;
            case R.id.from_test_to_results:

                if(started){
                    Toast.makeText(activ.getApplicationContext(), MSG_TEST_NOT_FINISHED, Toast.LENGTH_LONG).show();
                }else if(nrOfTests == 0){
                    Toast.makeText(activ.getApplicationContext(), MSG_NO_TEST,Toast.LENGTH_LONG).show();
                }else{
                    activ.setNrOfTests(this.nrOfTests);
                    ResultsFragment frag = new ResultsFragment();
                    activ.switchContent(frag);
                }
            default:
                break;
        }
    }
}
