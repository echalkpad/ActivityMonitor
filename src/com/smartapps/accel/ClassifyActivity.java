package com.smartapps.accel;

import java.util.ArrayList;
import java.util.List;


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


public class ClassifyActivity extends Activity implements SensorEventListener,
		OnClickListener {
	private SensorManager sensorManager;
	private Button btnStartTest, btnStopTest, btnTestClassify;


    private Sensor accel;

	private boolean started = false;

    private ArrayList<AccelData> sensorDataTest= null;
   // private ConfusionMatix  confusionMatrix;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify);
        ArrayList<AccelData> sensorDataIdle = (ArrayList<AccelData>) getIntent().getSerializableExtra("sensorDataIdle");
        ArrayList<AccelData> sensorDataWalking = (ArrayList<AccelData>) getIntent().getSerializableExtra("sensorDataWalking");
        ArrayList<AccelData> sensorDataRunning = (ArrayList<AccelData>) getIntent().getSerializableExtra("sensorDataRunning");
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		btnStartTest = (Button) findViewById(R.id.btnStartTest);
		btnStopTest = (Button) findViewById(R.id.btnStopTest);
        btnTestClassify = (Button) findViewById(R.id.btnClassify);

        btnStartTest.setOnClickListener(this);
        btnStopTest.setOnClickListener(this);
        btnTestClassify.setOnClickListener(this);


        btnStartTest.setEnabled(true);
        btnStopTest.setEnabled(false);


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


        if (started) {

            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            Point3d acelPoint = new Point3d();
            acelPoint.setX(x);
            acelPoint.setY(y);
            acelPoint.setZ(z);
            long timestamp = System.currentTimeMillis();
            AccelData data = new AccelData(timestamp,acelPoint,"");
            sensorDataTest.add(data);
        }

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartTest:

            btnStartTest.setEnabled(false);
			btnStopTest.setEnabled(true);
             sensorDataTest = new ArrayList<AccelData>();
			// save prev data if available
			started = true;
			accel = sensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accel,
					SensorManager.SENSOR_DELAY_FASTEST);
			break;
		case R.id.btnStopTest:
            btnStartTest.setEnabled(true);
			btnStopTest.setEnabled(false);

			started = false;
			sensorManager.unregisterListener(this);

			break;

            case R.id.btnClassify:
                //ClassifyData();

                break;

		default:
			break;
		}

	}
    // This function checks the sample data against the test data.
    private void ClassifyData(ArrayList<AccelData> sampledata, ArrayList<AccelData> testdata)    {

        int min = Math.min(sampledata.size(), testdata.size());
        for(int i = 0; i<min; i++) {
            double distance = sampledata.get(i).getPoint3D().distance(testdata.get(i).getPoint3D());
            sampledata.get(i).neighbours.add(new Neighbour(testdata.get(i), distance));
        }
        // Sort the List
         Sort(sampledata);

        for(int i=0; i<5; i++){
            System.out.println(sampledata);
        }
    }
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

}
