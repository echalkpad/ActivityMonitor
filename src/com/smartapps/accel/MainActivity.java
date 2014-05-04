package com.smartapps.accel;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Switch;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

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
    private Sensor accel;

	private boolean started = false;
	private ArrayList<AccelData> sensorDataWalking = null;
    private ArrayList<AccelData> sensorDataRunning = null;
    private ArrayList<AccelData> sensorDataIdle= null;
    private ArrayList<AccelData> sensorDataTest= null;
   // private ConfusionMatix  confusionMatrix;
	private LinearLayout layout;
	private View mChart;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		layout = (LinearLayout) findViewById(R.id.chart_container);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		/*sensorDataRunning = new ArrayList<AccelData>();
        sensorDataIdle = new ArrayList<AccelData>();
        sensorDataWalking = new ArrayList<AccelData>();
        sensorDataTest = new ArrayList<AccelData>();*/

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
            if(radbtnIdle.isChecked()&&sensorDataIdle!=null)
            {   data.setLabel("Idle");
                sensorDataIdle.add(data);
            }
            if(radbtnWalking.isChecked()&&sensorDataWalking!=null)
            {
                data.setLabel("Walking");
                sensorDataWalking.add(data);
            }
            if (radbtnRunning.isChecked()&& sensorDataRunning!=null)
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

                 if(radbtnIdle.isChecked())
                {
                    sensorDataIdle = new ArrayList<AccelData>();
                }
                if(radbtnWalking.isChecked())
                {
                    sensorDataWalking   = new ArrayList<AccelData>();
                }
                if (radbtnRunning.isChecked())
                {
                    sensorDataRunning = new ArrayList<AccelData>();
                }
			// save prev data if available
			started = true;
			accel = sensorManager
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


                Intent mainIntent = new Intent(MainActivity.this, ClassifyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("sensorDataIdle", sensorDataIdle);
                bundle.putSerializable("sensorDataWalking", sensorDataWalking);
                bundle.putSerializable("sensorDataRunning", sensorDataRunning);
                MainActivity.this.startActivity(mainIntent);

                break;

		default:
			break;
		}

	}



}
