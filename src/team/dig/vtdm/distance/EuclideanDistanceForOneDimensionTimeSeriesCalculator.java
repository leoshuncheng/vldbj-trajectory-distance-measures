package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;

public class EuclideanDistanceForOneDimensionTimeSeriesCalculator implements SequenceDistanceCalculator {

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {

        if (r.size() == 0 && s.size() == 0) {
            return 0;
        }
        if (r.size() == 0) {
            assert (s.get(0).dimension == 1);
            return Double.MAX_VALUE;
        }
        if (s.size() == 0) {
            assert (r.get(0).dimension == 1);
            return Double.MAX_VALUE;
        }
        assert (r.size() == s.size());

        double sum = 0;
        for (int i = 0; i < r.size(); ++i) {
            double dif = r.get(i).coordinate[0] - s.get(i).coordinate[0];
            sum += dif * dif;
        }

        return Math.sqrt(sum);
    }
}
