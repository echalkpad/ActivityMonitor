package com.smartapps.accel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.Toast;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import javax.vecmath.Point3d;


public class MainActivity extends Activity implements SensorEventListener,
		OnClickListener {
	private SensorManager sensorManager;
	private Button btnStart, btnStop, btnTest;
    private RadioButton radbtnWalking, radbtnIdle, radbtnRunning;
	private boolean started = false;
    private boolean istesting = false;
    private ArrayList<AccelData> trainingData;
    private ArrayList<AccelData> testDataTemp;
	private ArrayList<AccelData> testDataIdle;
    private ArrayList<AccelData> testDataWalk;
    private ArrayList<AccelData> testDataRun;
   // private ConfusionMatrix  confusionMatrix;
   private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

	private View mChart;

    private Sensor accel;

    private int timeToSave;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        istesting = false;
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        trainingData = new ArrayList<AccelData>();
        testDataIdle = new ArrayList<AccelData>();
        testDataWalk = new ArrayList<AccelData>();
        testDataRun = new ArrayList<AccelData>();
        testDataTemp = new ArrayList<AccelData>();


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
		if (started == true) {
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
	  if(!istesting){
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
            if ((timeToSave % 8) == 0) {

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
                System.out.println("In Training!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
		}
      }
       else {
             // This part is for storing the testing data
                  double x = event.values[0];
                  double y = event.values[1];
                  double z = event.values[2];
                  Point3d acelPoint = new Point3d();
                  acelPoint.setX(x);
                  acelPoint.setY(y);
                  acelPoint.setZ(z);
                  long timestamp = System.currentTimeMillis();

                  timeToSave++;
                  if ((timeToSave % 8) == 0) {
                      AccelData data = new AccelData(timestamp, acelPoint);
                      this.testDataTemp.add(data);
                      Log.w("Test","In TESTING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                  }
       }
	}


    public void onRadioButtonClick(View view) {
        System.out.println();
        Log.e("RadioButtonClick", "One radio button was clicked");
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radbotIdle:
                Log.w("IdleChecked", "Idle was clicked!!!!!!!!!!!!!");
                radbtnWalking.setChecked(false);
                radbtnRunning.setChecked(false);
                break;
            case R.id.radbtnwalking:
                Log.w("WalkChecked", "Walk was clicked!!!!!!!!!!!!!");
                radbtnIdle.setChecked(false);
                radbtnRunning.setChecked(false);
                break;
            case R.id.radbutrunning:
                Log.w("RunChecked", "Run was clicked!!!!!!!!!!!!!");
                radbtnIdle.setChecked(false);
                radbtnWalking.setChecked(false);
                break;
        }
    }


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:

            istesting =false;
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);

			// save prev data if available
			started = true;
			this.accel = sensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accel,
					SensorManager.SENSOR_DELAY_FASTEST);
			break;
		case R.id.btnStop:
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
            istesting =false;
			started = false;
			sensorManager.unregisterListener(this);

			break;
        case R.id.btnTest:

            /*
            istesting = true;
            TestFragment tf = new TestFragment();
            switchContent(tf);
           */

            if(trainingData.size()>0)
            {
            istesting = true;
            TestFragment tf = new TestFragment();
            switchContent(tf);

            }
            else
            {
                Toast.makeText(getApplicationContext(), "No Training data found!!!",
                        Toast.LENGTH_SHORT).show();
            }


            /*for(int i= 0; i < this.trainingData.size(); i++){
                    System.out.println("DATA " + i + "\n" + trainingData.get(i).toString());
            }*/
            break;
		default:
			break;
		}

	}
    // This function checks the sample data against the test data.
    public void classifyData(ArrayList<AccelData> sampledata, ArrayList<AccelData> testdata)    {


        //DON'T FORGET TO TAKE THIS OUT!!!!!!!

        testdata = new ArrayList<AccelData>();
        testdata.add(new AccelData(System.currentTimeMillis(), new Point3d(1,1,1)));
        testdata.add(new AccelData(System.currentTimeMillis(), new Point3d(2,1,1)));
        testdata.add(new AccelData(System.currentTimeMillis(), new Point3d(1,2,1)));
        testdata.add(new AccelData(System.currentTimeMillis(), new Point3d(1,1,2)));

        sampledata = new ArrayList<AccelData>();
        sampledata.add(new AccelData(System.currentTimeMillis(), new Point3d(50,40,30), AccelData.State.Idle));
        sampledata.add(new AccelData(System.currentTimeMillis(), new Point3d(5,411,30), AccelData.State.Idle));
        sampledata.add(new AccelData(System.currentTimeMillis(), new Point3d(45,65,10), AccelData.State.Idle));
        sampledata.add(new AccelData(System.currentTimeMillis(), new Point3d(43,64,11), AccelData.State.Run));
        sampledata.add(new AccelData(System.currentTimeMillis(), new Point3d(20,21,23), AccelData.State.Run));
        sampledata.add(new AccelData(System.currentTimeMillis(), new Point3d(3.5,7.5,4.5), AccelData.State.Run));
        sampledata.add(new AccelData(System.currentTimeMillis(), new Point3d(2,11,31), AccelData.State.Run));
        sampledata.add(new AccelData(System.currentTimeMillis(), new Point3d(8,5,2), AccelData.State.Walk));
        sampledata.add(new AccelData(System.currentTimeMillis(), new Point3d(10,20,20), AccelData.State.Walk));

        this.testDataTemp = testdata;
        this.trainingData = sampledata;

        //Don't forget ! Take that thing out............. fake data...

        ArrayList<Neighbour> neighbours = new ArrayList<Neighbour>();
        for(int j = 0; j < testdata.size(); j++){
            for (int i = 0; i < sampledata.size(); i++) {
                double distance = sampledata.get(i).getPoint3D().distance(testdata.get(j).getPoint3D());
                neighbours.add(new Neighbour(sampledata.get(i), distance));
            }

            Log.w("Test", "TARARARARARARATTATARARARARAR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

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

            neighbours = new ArrayList();
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

        //Log.w("Test", "Nr of runs is " + nrRuns + " Nr of walks is " + nrWalks + " Nr of Idle is " + nrIdle);
        if (nrRuns > nrIdle) {
            if(nrRuns > nrWalks) {
                testDataRun = new ArrayList<AccelData>(testDataTemp);
            }else  testDataWalk = new ArrayList<AccelData>(testDataTemp);
        } else {
            if(nrIdle > nrWalks) {
                testDataIdle = new ArrayList<AccelData>(testDataTemp);
            }else testDataWalk = new ArrayList<AccelData>(testDataTemp);
        }
    }

/*
    private void Sort(List<AccelData> thelist)    {

        for(int i = 0; i < thelist.size(); i++)	{// for all objects
            AccelData obj = thelist.get(i);
            for(int x = 0; x < obj.neighbours.size() - 1; x++)
                for(int y = x + 1; y < obj.neighbours.size(); y++)
                    if(obj.neighbours.get(x).getDistance() > obj.neighbours.get(y).getDistance()) {
                        Neighbour aux = obj.neighbours.get(x);
                        obj.neighbours.set(x,obj.neighbours.get(y));
                        obj.neighbours.set(y,aux);
                    }

        }
    }

*/
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
    public ArrayList<AccelData> getTestDataIdle()
    {
        return this.testDataIdle;
    }
    public ArrayList<AccelData> getTestDataWalk()
    {
        return this.testDataWalk;
    }
    public ArrayList<AccelData> getTestDataRun()
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

}
