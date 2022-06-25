package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

public class POIOnRoadNetworkDistanceCaculator implements SequenceDistanceCalculator {

    ArrayList<Point> POISet;
    double Threshold;


    public POIOnRoadNetworkDistanceCaculator(ArrayList<Point> poi, double threshold) {
        POISet = poi;
        Threshold = threshold;
    }

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);

        return getPOI(r_clone, s_clone);
    }

    private double getPOI(ArrayList<Point> r, ArrayList<Point> s) {

        double[][] dist = new double[r.size() + 1][s.size() + 1];

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
        for (int i = r.size() - 1; i >= 0; --i) {
            for (int j = s.size() - 1; j >= 0; --j) {
                dist[i][j] = pdc.getDistance(r.get(i), s.get(j)) + Math.min(dist[i + 1][j + 1], Math.min(dist[i + 1][j], dist[i][j + 1]));
            }
        }

        return dist[0][0];
    }

    private int[] getSequence(ArrayList<Point> t) {
        int[] result = new int[POISet.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.MAX_VALUE;
        }

        for (int i = 0; i < t.size(); i++) {
            int index = getMatch(t.get(i));
            if (index == -1) {
                continue;
            }
            result[index] = i;
        }

        for (int i = 0; i < result.length; i++) {
            assert (result[i] != Integer.MAX_VALUE);
        }

        return result;
    }

    private int getMatch(Point p) {
        int result = -1;
        double distance = 0;
        EuclideanDistanceCalculator edc = new EuclideanDistanceCalculator();
        for (int i = 0; i < POISet.size(); i++) {
            double temp = edc.getDistance(p, POISet.get(i));
            if (temp <= Threshold) {
                if (result == -1) {
                    result = i;
                    distance = temp;
                } else {
                    if (distance > temp) {
                        result = i;
                        distance = temp;
                    }
                }
            }
        }

        return result;
    }

}
