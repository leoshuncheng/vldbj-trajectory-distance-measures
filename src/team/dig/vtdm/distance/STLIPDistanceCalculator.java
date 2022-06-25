/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Line;
import team.dig.vtdm.entities.Point;
import team.dig.vtdm.entities.Polygon;
import team.dig.vtdm.services.DistanceService;

/**
 * @author uqhsu1
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author uqhsu1
 */
public class STLIPDistanceCalculator implements SequenceDistanceCalculator {

    ArrayList<Polygon> polygon;
    ArrayList<Double> weight;
    ArrayList<Double> TLIP;
    double K = 0.5;

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);

        return getSTLIP(r_clone, s_clone);
    }

    public void setK(double k) {
        K = k;
    }

    private double getSTLIP(ArrayList<Point> r, ArrayList<Point> s) {
        polygon = getPolygon(r, s);
        double result = 0;

        for (int i = 0; i < polygon.size(); i++) {
            result += (polygon.get(i).getArea() * weight.get(i).doubleValue()) * (1 + K * TLIP.get(i).doubleValue());
        }
        return result;
    }

    private double getLength(ArrayList<Point> p) {
        double result = 0;

        EuclideanDistanceCalculator e = new EuclideanDistanceCalculator();

        for (int i = 0; i < p.size() - 1; i++) {
            result += e.getDistance(p.get(i), p.get(i + 1));
        }

        return result;
    }

    private ArrayList<Polygon> getPolygon(ArrayList<Point> r, ArrayList<Point> s) {

        ArrayList<Polygon> result = new ArrayList<Polygon>();

        weight = new ArrayList<Double>();
        TLIP = new ArrayList<Double>();

        double lengthR = getLength(r);
        double lengthS = getLength(s);

        assert (r.size() > 1);
        assert (s.size() > 1); //in order to make sure the variation inside is true

        double durationR = r.get(r.size() - 1).timeStamp - r.get(0).timeStamp;
        double durationS = s.get(s.size() - 1).timeStamp - s.get(0).timeStamp;

        ArrayList<Line> rl = getPolyline(r);
        ArrayList<Line> sl = getPolyline(s);

        ArrayList<Point> intersections = new ArrayList<Point>();
        ArrayList<twoInt> index = new ArrayList<twoInt>();

        boolean[] used = new boolean[sl.size()];
        for (int i = 0; i < used.length; i++) {
            used[i] = false;
        }

        for (int i = 0; i < rl.size(); i++) {
            for (int j = 0; j < sl.size(); j++) {
                if (used[j]) {
                    continue;
                }

                Point inter = rl.get(i).getIntersection(sl.get(j));
                if (inter == null) {
                    continue;
                }
                double x = inter.coordinate[0];
                double y = inter.coordinate[1];

                double r1x = r.get(i).coordinate[0];
                double r1y = r.get(i).coordinate[1];


                double r2x = r.get(i + 1).coordinate[0];
                double r2y = r.get(i + 1).coordinate[1];

                if (r1x > r2x) {
                    double temp = r1x;
                    r1x = r2x;
                    r2x = temp;
                }

                if (r1y > r2y) {
                    double temp = r1y;
                    r1y = r2y;
                    r2y = temp;
                }

                double s1x = s.get(j).coordinate[0];
                double s1y = s.get(j).coordinate[1];

                double s2x = s.get(j + 1).coordinate[0];
                double s2y = s.get(j + 1).coordinate[1];

                if (s1x > s2x) {
                    double temp = s1x;
                    s1x = s2x;
                    s2x = temp;
                }

                if (s1y > s2y) {
                    double temp = s1y;
                    s1y = s2y;
                    s2y = temp;
                }

                if (x >= r1x && x <= r2x
                        && y >= r1y && y <= r2y
                        && x >= s1x && x < s2x
                        && y >= s1y && y <= s2y) {
                    double[] temp = new double[2];
                    temp[0] = x;
                    temp[1] = y;
                    Point tempP = new Point(temp);
                    twoInt tempI = new twoInt(i, j);

                    intersections.add(tempP);
                    index.add(tempI);
                    for (int k = 0; k <= j; k++) {
                        used[k] = true;
                    }

                    if (intersections.size() == 1) {
                        ArrayList<Point> tempPolyPoints = new ArrayList<Point>();

                        for (int ii = 0; ii <= i; ii++) {
                            tempPolyPoints.add(r.get(ii));
                        }

                        tempPolyPoints.add(tempP);

                        for (int ii = j; ii >= 0; ii--) {
                            tempPolyPoints.add(s.get(ii));
                        }

                        ArrayList<Point> tempR = new ArrayList<Point>();
                        for (int ii = 0; ii <= i; ii++) {
                            tempR.add(r.get(ii));
                        }
                        tempR.add(tempP);
                        ArrayList<Point> tempS = new ArrayList<Point>();
                        for (int ii = 0; ii <= j; ii++) {
                            tempS.add(s.get(ii));
                        }
                        tempS.add(tempP);
                        double lengthRSub = getLength(tempR);
                        double lengthSSub = getLength(tempS); // the same as TLP, so why??? no why its right

                        double weightV = (lengthRSub + lengthSSub) / (lengthR + lengthS);

                        double durationRTemp = r.get(i).timeStamp - r.get(0).timeStamp;
                        double durationSTemp = s.get(j).timeStamp - s.get(0).timeStamp;//why the durationRTemp is the time points when T1 and T2 cross each other
                        //是算的前一个点的时间啊？对的
                        double tlip = Math.abs(1 - (2 * Math.max(durationRTemp, durationSTemp)) / (durationR + durationS));

                        TLIP.add(tlip);
                        weight.add(new Double(weightV));
                        result.add(new Polygon(tempPolyPoints));
                    } else {
                        ArrayList<Point> tempPolyPoints = new ArrayList<Point>();

                        tempPolyPoints.add(intersections.get(intersections.size() - 2));//上一个交点

                        for (int ii = index.get(index.size() - 2).x + 1; ii <= i; ii++) {
                            tempPolyPoints.add(r.get(ii));
                        }

                        tempPolyPoints.add(tempP);//现在的交点

                        for (int ii = j; ii >= index.get(index.size() - 2).y + 1; ii--) {
                            tempPolyPoints.add(s.get(ii));
                        }

                        ArrayList<Point> tempR = new ArrayList<Point>();
                        for (int ii = index.get(index.size() - 2).x + 1; ii <= i; ii++) {
                            tempR.add(r.get(ii));
                        }
                        tempR.add(tempP);
                        ArrayList<Point> tempS = new ArrayList<Point>();
                        for (int ii = index.get(index.size() - 2).y + 1; ii <= j; ii++) {
                            tempS.add(s.get(ii));
                        }
                        tempS.add(tempP);
                        double lengthRSub = getLength(tempR);
                        double lengthSSub = getLength(tempS);

                        double weightV = (lengthRSub + lengthSSub) / (lengthR + lengthS);

                        double durationRTemp = r.get(i).timeStamp - r.get(index.get(index.size() - 2).x + 1).timeStamp;//相邻两个交点的时间间隔
                        double durationSTemp = s.get(j).timeStamp - s.get(index.get(index.size() - 2).y + 1).timeStamp;

                        double tlip = Math.abs(1 - (2 * Math.max(durationRTemp, durationSTemp)) / (durationR + durationS));

                        TLIP.add(tlip);
                        weight.add(new Double(weightV));
                        result.add(new Polygon(tempPolyPoints));
                    }
                }
            }
        }
        return result;
    }

    private ArrayList<Line> getPolyline(ArrayList<Point> r) {
        ArrayList<Line> result = new ArrayList<Line>();

        if (r.size() < 2) {
            return result;
        }

        for (int i = 0; i < r.size() - 1; i++) {
            Line tempLine = new Line(r.get(i), r.get(i + 1));
            result.add(tempLine);
        }

        return result;
    }

}

