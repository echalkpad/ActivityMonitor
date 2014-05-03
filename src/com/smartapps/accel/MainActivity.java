package com.smartapps.accel;

import java.util.ArrayList;
import java.util.List;

import android.widget.RadioButton;
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
	private ArrayList<AccelData> sensorDataWalking;
    private ArrayList<AccelData> sensorDataRunning;
    private ArrayList<AccelData> sensorDataIdle;
   // private ConfusionMatix  confusionMatrix;
	private LinearLayout layout;
	private View mChart;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		layout = (LinearLayout) findViewById(R.id.chart_container);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorDataRunning = new ArrayList<AccelData>();
        sensorDataIdle = new ArrayList<AccelData>();
        sensorDataWalking = new ArrayList<AccelData>();

		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
        btnTest = (Button) findViewById(R.id.btnTest);

        radbtnIdle = (RadioButton) findViewById(R.id.radbotIdle);
        radbtnWalking =(RadioButton) findViewById(R.id.radbtnwalking);
        radbtnRunning = (RadioButton) findViewById(R.id.radbutrunning);

		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);

		btnStart.setEnabled(true);
		btnStop.setEnabled(false);


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
            if(radbtnIdle.isChecked())
            {   data.setLabel("Idle");
                sensorDataIdle.add(data);
            }
            if(radbtnWalking.isChecked())
            {
                data.setLabel("Walking");
                sensorDataWalking.add(data);
            }
            if (radbtnRunning.isChecked())
            {
                data.setLabel("Running");
                sensorDataRunning.add(data);
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
                Log.d("IdleChecked", "Idle was clicked!!!!!!!!!!!!!");
                radbtnWalking.setChecked(false);
                radbtnRunning.setChecked(false);
                break;
            case R.id.radbtnwalking:
                Log.d("WalkChecked", "Walk was clicked!!!!!!!!!!!!!");
                radbtnIdle.setChecked(false);
                radbtnRunning.setChecked(false);
                break;
            case R.id.radbutrunning:
                Log.d("RunChecked", "Run was clicked!!!!!!!!!!!!!");
                radbtnIdle.setChecked(false);
                radbtnWalking.setChecked(false);
                break;
        }
    }


        @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:

			btnStart.setEnabled(false);
			btnStop.setEnabled(true);

			sensorDataIdle = new ArrayList<AccelData>();
            sensorDataWalking   = new ArrayList<AccelData>();
            sensorDataRunning = new ArrayList<AccelData>();
			// save prev data if available
			started = true;
			Sensor accel = sensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accel,
					SensorManager.SENSOR_DELAY_FASTEST);
			break;
		case R.id.btnStop:
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);

			started = false;
			sensorManager.unregisterListener(this);
			layout.removeAllViews();
			break;
            case R.id.btnTest:
               // btnTest.setEnabled(false);

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
