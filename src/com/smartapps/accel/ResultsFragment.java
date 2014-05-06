package com.smartapps.accel;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.app.ActionBar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maryanne on 05-05-2014.
 */
public class ResultsFragment   extends ListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewContainer = inflater.inflate(R.layout.results_fragment, container, false);
        MainActivity activ = (MainActivity)getActivity();


         if(activ.getTestDataIdle().size()>0)
         {
             updateInformation(activ.getTestDataIdle());
         }
         else if(activ.getTestDataWalk().size()>0)
         {
             updateInformation(activ.getTestDataWalk());
         }
        else {
             updateInformation(activ.getTestDataRun());
         }


        return viewContainer;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activ = (MainActivity)getActivity();
        activ.setTitle("Results");

    }


    public void updateInformation(ArrayList<AccelData> tableData){
        DataItemAdapter adapter = new DataItemAdapter(getActivity());

        //EXAMPLE
        adapter.add(new DataItem("Idle", 9, 0, 0, 9));
        adapter.add(new DataItem("Walk", 1, 7, 1, 9));
        adapter.add(new DataItem("Run", 1, 3, 5, 9));

        setListAdapter(adapter);
    }




    /* Class DataItem Adapter
	 *
	 */
    public class DataItemAdapter extends ArrayAdapter<DataItem> {

        public DataItemAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parnt) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.results_row, null);
            }

            TextView dataName = (TextView) convertView.findViewById(R.id.name);
            dataName.setText(getItem(position).dataName);
            //hourView.setTextColor(getResources().getColor(R.color.black));

            int totalNrData = getItem(position).totalNrOfData;

            TextView idleValue = (TextView) convertView.findViewById(R.id.idle);
            int valueI = getItem(position).nrOfIdle;
            if(valueI % totalNrData == 0){
                idleValue.setText(Integer.toString(valueI / totalNrData));
            }else{
                idleValue.setText(Integer.toString(valueI) + "/" + Integer.toString(totalNrData));
            }

            TextView walkValue = (TextView) convertView.findViewById(R.id.walk);
            int valueW = getItem(position).nrOfWalks;
            if(valueW % totalNrData == 0){
                walkValue.setText(Integer.toString(valueW / totalNrData));
            }else{
                walkValue.setText(Integer.toString(valueW) + "/" + Integer.toString(totalNrData));
            }

            TextView runValue = (TextView) convertView.findViewById(R.id.run);
            int valueR = getItem(position).nrOfRuns;
            if(valueI % totalNrData == 0){
                runValue.setText(Integer.toString(valueR / totalNrData));
            }else{
                runValue.setText(Integer.toString(valueR) + "/" + Integer.toString(totalNrData));
            }

            return convertView;
        }



    }



    /* Class DataItem
     */
    private class DataItem {
        public String dataName;
        public int nrOfWalks;
        public int nrOfIdle;
        public int nrOfRuns;
        public int totalNrOfData;

        public DataItem(String dataName, int nrOfWalks, int nrOfIdle, int nrOfRuns, int totalNrOfData) {
            this.dataName = dataName;
            this.nrOfWalks = nrOfWalks;
            this.nrOfIdle = nrOfIdle;
            this.nrOfRuns = nrOfRuns;
            this.totalNrOfData = totalNrOfData;
        }
    }
}