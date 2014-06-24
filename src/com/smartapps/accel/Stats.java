package com.smartapps.accel;

import java.util.ArrayList;

public class Stats {
    ArrayList<Double> nums;
    double sum = 0 , meanSum = 0;

    public Stats(ArrayList<Double> nums) {
        this.nums = nums;
    }
    /*
     * Compute the mean of an array
     */
    public double mean(){
        for(double num : nums){
            sum+=num;
        }
        return sum/(nums.size() -1);
    }

    public double varinace(double mean){
        for(double num : nums){
            meanSum += ((num - mean) * (num - mean) ) ;
        }
        return meanSum/(nums.size() -1);
    }
    // getting the standard deviation
    public double stnDev(){
        double stnDen =  Math.sqrt( varinace(mean()) );
        return stnDen;
    }
    // Getting the Maximum
    public double maxNum(){
        double max = Double.MIN_VALUE ;
        for(double num : nums){
            if (num > max){
                max = num;
            }
        }

        return max;
    }

    // Getting the minimum
    public double minNum(){
        double min = Double.MAX_VALUE ;
        for(double num : nums){
            if (num < min){
                min = num;
            }
        }

        return min;
    }
}

