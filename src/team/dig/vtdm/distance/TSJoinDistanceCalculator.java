package team.dig.vtdm.distance;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

import java.util.ArrayList;

/**
 * Created by leoSC on 2018/12/16.
 */
public class TSJoinDistanceCalculator implements SequenceDistanceCalculator {

    double lamda;// [0,1]

    public TSJoinDistanceCalculator(double lamda) {
        this.lamda = lamda;
    }

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
        return getTSJoin(r_clone, s_clone);
    }

    private double getTSJoin(ArrayList<Point> r, ArrayList<Point> s) {
        double len_r = r.size();
        double len_s = s.size();
        double r_sum = 0;
        double r_time_sum = 0;
        for (Point p : r) {
            r_sum += Math.exp(-1 * getSpatialNetworkDistence(p, s));
            r_time_sum += Math.exp(-1 * getTemporalDistence(p, s));
        }
        double r_result = r_sum / len_r;
        double r_time_result = r_time_sum / len_r;

        double s_sum = 0;
        double s_time_sum = 0;
        for (Point p : s) {
            s_sum += Math.exp(-1 * getSpatialNetworkDistence(p, r));
            s_time_sum += Math.exp(-1 * getTemporalDistence(p, r));
        }

        double s_result = s_sum / len_s;
        double s_time_result = s_time_sum / len_s;
        double Sims = r_result + s_result;
        double Simt = r_time_result + s_time_result;

        return -1 * (lamda * Sims + (1.0 - lamda) * Simt);
    }

    private double getSpatialNetworkDistence(Point p, ArrayList<Point> t) {
        //assume that the grid net is build up
        double min = Double.POSITIVE_INFINITY;

        for (Point r : t) {
            double simdis = 0;
            double eudistance = getEuDistance(r, p);
            simdis = Math.pow(2, 0.5) * eudistance;// 无法完全映射到Map上，但是使用近似的等腰直角三角形进行模拟，可以在相当程度上近似为gridMap
            if (simdis < min) min = simdis;
        }
        return min;
    }

    private double getTemporalDistence(Point p, ArrayList<Point> t) {

        double min = Double.POSITIVE_INFINITY;


        for (Point r : t) {
            double TImeDistance = Math.abs(r.timeStamp - p.timeStamp);
            if (TImeDistance < min) min = TImeDistance;
        }
        return min;
    }

    private double getEuDistance(Point x, Point y) {
        assert (x.dimension == y.dimension);

        double sum = 0;
        for (int i = 0; i < x.dimension; ++i) {
            double dif = x.coordinate[i] - y.coordinate[i];
            sum += dif * dif;
        }
        return Math.sqrt(sum);
    }
}
