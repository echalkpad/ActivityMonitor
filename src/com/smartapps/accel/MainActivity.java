package com.smartapps.accel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.IntentFilter;


import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import javax.vecmath.Point3d;


public class MainActivity extends Activity implements SensorEventListener,
		OnClickListener {

    private static final String MSG_CHOOSE_ACTION= "Please choose your action";
    private static final String MSG_NO_TRAINING = "No training data found. Please perform the 3 actions";
    private static final String MSG_ANOTHER_ACTION = "You already performed this action. Please select another.";
    private static final String MSG_NOT_ENOUGH_ACTIONS_1 = "Only 1 action was performed. Please do: ";
    private static final String MSG_NOT_ENOUGH_ACTIONS_2 = "Only 2 actions were performed. Please do ";
    private static final String MSG_FINISH_ACTION = "You must first complete the action. Please press button STOP to finish it.";



	private SensorManager sensorManager;
	private Button btnStart, btnStop, btnTest, btnCheckWifi;
    private TextView txtview, txtviewwifi;
    private RadioButton radbtnWalking, radbtnIdle, radbtnRunning;
	private boolean started = false;
    private boolean istesting = false;
    private ArrayList<AccelData> trainingData;
    private ArrayList<AccelData> testDataTemp;
	private GroupData testDataIdle;
    private GroupData testDataWalk;
    private GroupData testDataRun;



   private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    private int hasTrainingDataIdle;
    private int hasTrainingDataWalk;
    private int hasTrainingDataRun;
    private int nrOfTests;
    private Sensor accel;
    private int timeToSave;
    private List<ScanResult> wifiList;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        hasTrainingDataIdle = 0;
        hasTrainingDataWalk = 0;
        hasTrainingDataRun = 0;
        nrOfTests = 0;
        istesting = false;
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        trainingData = new ArrayList<AccelData>();
        testDataTemp = new ArrayList<AccelData>();
        testDataIdle = new GroupData(new ArrayList<AccelData>());
        testDataWalk = new GroupData(new ArrayList<AccelData>());
        testDataRun = new GroupData(new ArrayList<AccelData>());



		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
        btnTest = (Button) findViewById(R.id.btnTest);


        radbtnIdle = (RadioButton) findViewById(R.id.radbotIdle);
        radbtnWalking =(RadioButton) findViewById(R.id.radbtnwalking);
        radbtnRunning = (RadioButton) findViewById(R.id.radbutrunning);

		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
        btnTest.setOnClickListener(this);

        btnStart.setEnabled(true);
		btnStop.setEnabled(false);

        setTitle("Activity Monitoring");

        timeToSave = 0;

	}


    public void switchContent(Fragment fragment)
    {
        if(fragment instanceof TestFragment){
            setContentView(R.layout.test_fragment);
            FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame,fragment,TAG_FRAGMENT);
            transaction.addToBackStack("TestFragment");
            transaction.commit();

        }else if(fragment instanceof  ResultsFragment){
            setContentView(R.layout.results_fragment);
            FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
            transaction.replace(R.id.results_page,fragment, TAG_FRAGMENT);
            transaction.addToBackStack("ResultsFragment");
            transaction.commit();
        }
    }

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (started) {
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
        if (started) {

            double x = event.values[0];
			double y = event.values[1];
			double z = event.values[2];
            Point3d acelPoint = new Point3d();
            acelPoint.setX(x);
            acelPoint.setY(y);
            acelPoint.setZ(z);
			long timestamp = System.currentTimeMillis();
            timeToSave++;

            if(!istesting){
                if ((timeToSave % 2) == 0) {

                    AccelData data = new AccelData(timestamp, acelPoint);
                    if (radbtnIdle.isChecked()) {
                        data.setPointState(AccelData.State.Idle);
                    }
                    if (radbtnWalking.isChecked()) {
                        data.setPointState(AccelData.State.Walk);
                    }
                    if (radbtnRunning.isChecked()) {
                        data.setPointState(AccelData.State.Run);
                    }
                    this.trainingData.add(data);
                }
		    }else {

                if ((timeToSave % 8) == 0) {
                    AccelData data = new AccelData(timestamp, acelPoint);
                    this.testDataTemp.add(data);
                }
            }
       }
	}


    public void onRadioButtonClick(View view) {
        System.out.println();
        // Is the button now checked?
        // boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radbotIdle:
                radbtnWalking.setChecked(false);
                radbtnRunning.setChecked(false);
                break;
            case R.id.radbtnwalking:
                radbtnIdle.setChecked(false);
                radbtnRunning.setChecked(false);
                break;
            case R.id.radbutrunning:
                radbtnIdle.setChecked(false);
                radbtnWalking.setChecked(false);
                break;

            default:
                break;
        }

    }


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:


            if(this.radbtnIdle.isChecked()){
                if(this.hasTrainingDataIdle == 1){
                    Toast.makeText(getApplicationContext(),MSG_ANOTHER_ACTION,Toast.LENGTH_SHORT).show();
                    break;
                }else this.hasTrainingDataIdle = 1;
            }else if(this.radbtnWalking.isChecked()){
                if(this.hasTrainingDataWalk == 1){
                    Toast.makeText(getApplicationContext(),MSG_ANOTHER_ACTION,Toast.LENGTH_SHORT).show();
                    break;
                }else this.hasTrainingDataWalk = 1;
            }else if(this.radbtnRunning.isChecked()){
                if(this.hasTrainingDataRun == 1){
                    Toast.makeText(getApplicationContext(),MSG_ANOTHER_ACTION,Toast.LENGTH_SHORT).show();
                    break;
                }else this.hasTrainingDataRun = 1;
            }else{
                Toast.makeText(getApplicationContext(),MSG_CHOOSE_ACTION,Toast.LENGTH_SHORT).show();
                break;
            }

            istesting =false;
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);

            radbtnRunning.setEnabled(false);
            radbtnWalking.setEnabled(false);
            radbtnIdle.setEnabled(false);

            // save prev data if available
            started = true;
            this.accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accel,SensorManager.SENSOR_DELAY_FASTEST);

			break;
		case R.id.btnStop:
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
            istesting =false;
			started = false;
			sensorManager.unregisterListener(this);

            radbtnRunning.setEnabled(true);
            radbtnWalking.setEnabled(true);
            radbtnIdle.setEnabled(true);
			break;
        case R.id.btnTest:

            //temporary value
            int tempNrActions = this.hasTrainingDataIdle + this.hasTrainingDataRun + this.hasTrainingDataWalk;
            if(started){
                Toast.makeText(getApplicationContext(), MSG_FINISH_ACTION, Toast.LENGTH_LONG).show();
            }else if( tempNrActions == 0) {
                Toast.makeText(getApplicationContext(), MSG_NO_TRAINING, Toast.LENGTH_LONG).show();
            }else if(tempNrActions == 1) {
                if(this.hasTrainingDataWalk == 1) {
                    Toast.makeText(getApplicationContext(), MSG_NOT_ENOUGH_ACTIONS_1 + "Run and Idle", Toast.LENGTH_LONG).show();
                }else if (this.hasTrainingDataRun == 1){
                    Toast.makeText(getApplicationContext(), MSG_NOT_ENOUGH_ACTIONS_1 + "Walk and Idle", Toast.LENGTH_LONG).show();
                }else{ //this.hasTrainingDataIdle == 1
                    Toast.makeText(getApplicationContext(), MSG_NOT_ENOUGH_ACTIONS_1 + "Walk and Run", Toast.LENGTH_LONG).show();
                }
            }else if(tempNrActions == 2) {
                if (this.hasTrainingDataIdle == 0){
                    Toast.makeText(getApplicationContext(), MSG_NOT_ENOUGH_ACTIONS_2 + "Idle", Toast.LENGTH_LONG).show();
                }else if(this.hasTrainingDataWalk == 0) {
                    Toast.makeText(getApplicationContext(), MSG_NOT_ENOUGH_ACTIONS_2 + "Walk", Toast.LENGTH_LONG).show();
                }else { //this.hasTrainingDataRun == 0
                    Toast.makeText(getApplicationContext(), MSG_NOT_ENOUGH_ACTIONS_2 + "Run", Toast.LENGTH_LONG).show();
                }
            }else { //tempNrActions = 3

                istesting = true;
                TestFragment tf = new TestFragment();
                switchContent(tf);

            }
            break;

		default:
			break;
		}

	}

    // This function checks the sample data against the test data.
    public void classifyData(ArrayList<AccelData> sampledata, ArrayList<AccelData> testdata)    {




        ArrayList<Neighbour> neighbours = new ArrayList<Neighbour>();
        for(int j = 0; j < testdata.size(); j++){
            for (int i = 0; i < sampledata.size(); i++) {
                double distance = sampledata.get(i).getPoint3D().distance(testdata.get(j).getPoint3D());
                neighbours.add(new Neighbour(sampledata.get(i), distance));
            }
            //sort
            Collections.sort(neighbours);
            //add to j (testdata) the first 5 neighbours
            ArrayList<Neighbour> temporary = new ArrayList<Neighbour>();
            int nrRuns = 0, nrWalks = 0, nrIdle = 0;
            for(int t = 0; t < 5; t++){

                temporary.add(neighbours.get(t));
                switch(neighbours.get(t).getNeighbour().getPointState()){
                    case Idle:
                        nrIdle++;
                        break;
                    case Run:
                        nrRuns++;
                        break;
                    case Walk:
                        nrWalks++;
                        break;
                    default:
                        break;
                }

                System.out.println("Le voisin " + t + " " + neighbours.get(t).getDistance());
            }
            AccelData.State st;
            if(nrRuns > nrIdle){
                st = (nrRuns>nrWalks)? AccelData.State.Run : AccelData.State.Walk;
            }else st = (nrIdle>nrWalks)?  AccelData.State.Idle : AccelData.State.Walk;
            testdata.get(j).setPointState(st);
            testdata.get(j).setNeighbours(temporary);

            System.out.println("Le donnees " + testdata.get(j).getPointState().name());

            neighbours = new ArrayList<Neighbour>();
        }

    }


    public void whereDoYouBelong() {
        int nrIdle = 0, nrRuns = 0, nrWalks = 0;
        for (int i = 0; i < this.testDataTemp.size(); i++) {
            switch (this.testDataTemp.get(i).getPointState()) {
                case Idle:
                    nrIdle++;
                    break;
                case Run:
                    nrRuns++;
                    break;
                case Walk:
                    nrWalks++;
                    break;
                default:
                    break;
            }
        }

        GroupData temporary = new GroupData(new ArrayList<AccelData>(testDataTemp));
        temporary.setnrOfIdle(nrIdle);
        temporary.setnrOfWalks(nrWalks);
        temporary.setnrOfRuns(nrRuns);
        //Log.w("Test", "Nr of runs is " + nrRuns + " Nr of walks is " + nrWalks + " Nr of Idle is " + nrIdle);
        if (nrRuns > nrIdle) {
            if(nrRuns > nrWalks) {
                testDataRun = new GroupData(temporary);
            }else  testDataWalk = new GroupData(temporary);
        } else {
            if(nrIdle > nrWalks) {
                testDataIdle = new GroupData(temporary);
            }else testDataWalk = new GroupData(temporary);
        }
    }


    public void startSensor(){
        started = true;
        this.accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void endSensor(){
        started = false;
        sensorManager.unregisterListener(this);
    }

    public ArrayList<AccelData> getTrainingData()
    {
        return this.trainingData;
    }
    public GroupData getTestDataIdle()
    {
        return this.testDataIdle;
    }
    public GroupData getTestDataWalk()
    {
        return this.testDataWalk;
    }
    public GroupData getTestDataRun()
    {
        return this.testDataRun;
    }

    public ArrayList<AccelData> getTestDataTemp()
    {
        return this.testDataTemp;
    }

    public void setTestDataTemp(ArrayList<AccelData> testData)
    {
        this.testDataTemp = testData;
    }


    public int getNrOfTests(){
        return this.nrOfTests;
    }

    public void setNrOfTests(int nrOfTests){
        this.nrOfTests = nrOfTests;
    }
}
