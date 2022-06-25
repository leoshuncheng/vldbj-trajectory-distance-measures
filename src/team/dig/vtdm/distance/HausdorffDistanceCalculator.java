package team.dig.vtdm.distance;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

import java.util.ArrayList;

/**
 * Created by leoSC on 2018/12/17.
 */
public class HausdorffDistanceCalculator implements SequenceDistanceCalculator {
    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
        return getHausdorff(r_clone, s_clone);
    }

    private double getHausdorff(ArrayList<Point> r, ArrayList<Point> s){

        double r_s=one_way_hausdorff(r,s);
        double s_r=one_way_hausdorff(s,r);
        return Math.max(r_s,s_r);

    }

    private double one_way_hausdorff(ArrayList<Point> r, ArrayList<Point> s){
        double Haudis = 0.0;
        double dis = 0.0;
        double mindis = Double.POSITIVE_INFINITY;
        for (Point rr:r){
            mindis = Double.POSITIVE_INFINITY;
            for (Point ss:s){
                 dis = getEuDistance(rr,ss);
                if (dis<mindis)mindis=dis;
            }
            if (mindis>Haudis)Haudis=mindis;
        }
        return Haudis;
    }

    private double getEuDistance(Point x, Point y) {
        assert(x.dimension == y.dimension);

        double sum = 0;
        for (int i = 0; i < x.dimension; ++i) {
            double dif = x.coordinate[i] - y.coordinate[i];
            sum += dif * dif;
        }

        return Math.sqrt(sum);
    }

}
