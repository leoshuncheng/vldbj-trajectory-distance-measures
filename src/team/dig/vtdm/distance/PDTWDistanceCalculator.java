package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

public class PDTWDistanceCalculator implements SequenceDistanceCalculator {

    double c = 3;

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);

        return getPDTW(r_clone, s_clone);
    }

    private ArrayList<Point> reduceDimension(ArrayList<Point> t) {
        ArrayList<Point> result = new ArrayList<Point>();

        assert (c != 0);
        if (t.size() == 0) {
            return result;
        }

        //assert(t.get(0).dimension!=1);

        double[] tempSum = new double[t.get(0).dimension];
        for (int k = 0; k < tempSum.length; k++) {
            tempSum[k] = 0;
        }

        int N = (int) (t.size() / c);

        for (int i = 0; i < t.size(); i++) {
            for (int j = 0; j < t.get(0).dimension; j++) {
                tempSum[j] += t.get(i).coordinate[j];
            }
            if ((i + 1) % c == 0) {
                for (int j = 0; j < tempSum.length; j++) {
                    tempSum[j] = tempSum[j] / c;
                }
                //Point tempResult=new Point(tempSum);

                result.add(new Point(tempSum));

                for (int k = 0; k < tempSum.length; k++) {
                    tempSum[k] = 0;
                }
            } else if (i == t.size() - 1) {
                double lastCount = t.size() % c;
                for (int j = 0; j < tempSum.length; j++) {
                    tempSum[j] = tempSum[j] / lastCount;
                }
                //Point tempResult=new Point(tempSum);

                result.add(new Point(tempSum));

                for (int k = 0; k < tempSum.length; k++) {
                    tempSum[k] = 0;
                }
            }


        }


        return result;
    }

    public PDTWDistanceCalculator(double n) {
        c = n;
    }

    private double getPDTW(ArrayList<Point> rO, ArrayList<Point> sO) {
        ArrayList<Point> r = reduceDimension(rO);
        ArrayList<Point> s = reduceDimension(sO);
		
		/*
		double[][] dist = new double[r.size()+1][s.size()+1];
		
		// initialize the dynamic programming seeds
		for (int i = 0, j = s.size(); i <= r.size(); ++i) {
			dist[i][j] = Double.MAX_VALUE;
		}
		for (int j = 0, i = r.size(); j <= s.size(); ++j) {
			dist[i][j] = Double.MAX_VALUE;
		}
		dist[s.size()][r.size()] = 0;
		
		// state transition
		EuclideanDistanceCalculator pdc = new EuclideanDistanceCalculator();
		for (int i = r.size()-1; i >= 0; --i) {
			for (int j = s.size()-1; j >= 0; --j) {
				dist[i][j] = pdc.GetDistance(r.get(i), s.get(j)) + Math.min(dist[i+1][j+1], Math.min(dist[i+1][j], dist[i][j+1]));
			}
		}
			
		return dist[0][0];
                * */

        DTWDistanceCalculator d = new DTWDistanceCalculator();

        return d.getDistance(r, s);
    }

}
