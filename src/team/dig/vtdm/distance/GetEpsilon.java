/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;

/**
 * @author uqhsu1
 */
public class GetEpsilon {
    public double getEpsilonValue(ArrayList<ArrayList<Point>> allTrajectory) {
        double epsilon = 0;

        double[] divition = new double[allTrajectory.size()];

        for (int i = 0; i < allTrajectory.size(); i++) {
            ArrayList<Point> trajectory = allTrajectory.get(i);

            divition[i] = normalization(trajectory);
        }

        double max = 0;

        for (int i = 0; i < allTrajectory.size(); i++) {
            if (max < divition[i]) {
                max = divition[i];
            }
        }

        return max / 4;

    }


    private double normalization(ArrayList<Point> t) {
        //ArrayList<Point> result=new ArrayList<Point>();

        if (t.size() == 0) {
            return 0;
        }

        double[] mean = new double[t.get(0).dimension];
        for (int i = 0; i < t.size(); i++) {
            for (int j = 0; j < t.get(0).dimension; j++) {
                mean[j] += t.get(i).coordinate[j];
            }
        }
        for (int i = 0; i < mean.length; i++) {
            mean[i] /= t.size();
        }

        double[] standardDeviation = new double[t.get(0).dimension];
        for (int i = 0; i < t.size(); i++) {
            for (int j = 0; j < t.get(0).dimension; j++) {
                standardDeviation[j] += Math.pow(t.get(i).coordinate[j] - mean[j], 2);
            }
        }
        for (int i = 0; i < standardDeviation.length; i++) {
            standardDeviation[i] = Math.sqrt(standardDeviation[i]);
        }

        double result = 0;

        for (int i = 0; i < standardDeviation.length; i++) {
            result += standardDeviation[i];
        }

        return result / standardDeviation.length;
    }


}
