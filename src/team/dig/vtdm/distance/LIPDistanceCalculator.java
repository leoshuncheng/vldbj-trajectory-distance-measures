/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Line;
import team.dig.vtdm.entities.Point;
import team.dig.vtdm.entities.Polygon;
import team.dig.vtdm.entities.PolygonNew;
import team.dig.vtdm.services.DistanceService;

/**
 * @author uqhsu1
 */
public class LIPDistanceCalculator implements SequenceDistanceCalculator {

    ArrayList<Polygon> polygon;
    ArrayList<PolygonNew> polygonNew;
    ArrayList<Double> weight;

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);

        return getLIP(r_clone, s_clone);
    }

    private double getLIP(ArrayList<Point> r, ArrayList<Point> s) {
        /*polygon=getPolygon(r,s);
        double result=0;
        
        for(int i=0;i<polygon.size();i++){
            result+=polygon.get(i).getArea()*weight.get(i).doubleValue();
        }
        if(result>100){
            int kk=0;
        }
        //System.out.print(result+'\n');
        return result;*/

        polygonNew = getPolygonNew(r, s);    //problem!!
        double result = 0;

        for (int i = 0; i < polygonNew.size(); i++) {
            result += polygonNew.get(i).getArea() * weight.get(i).doubleValue();//使用polygon还是polygonNew?
        }
        if (result > 100) {
            int kk = 0;
        }
        //System.out.print(result+'\n');
        return result;

    }

    double getLength(ArrayList<Point> p) {
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
        double lengthR = getLength(r);
        double lengthS = getLength(s);

        ArrayList<Line> rl = getPolyline(r);
        ArrayList<Line> sl = getPolyline(s);

        ArrayList<Point> intersections = new ArrayList<Point>(); //交点
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

                if (r1x > r2x) {  //交换
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

                if (x >= r1x && x <= r2x && y >= r1y && y <= r2y && x >= s1x && x < s2x && y >= s1y && y <= s2y) {
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
                        double lengthSSub = getLength(tempS);

                        double weightV = (lengthRSub + lengthSSub) / (lengthR + lengthS);

                        weight.add(new Double(weightV));
                        result.add(new Polygon(tempPolyPoints));
                    } else {
                        ArrayList<Point> tempPolyPoints = new ArrayList<Point>();

                        tempPolyPoints.add(intersections.get(intersections.size() - 2));

                        for (int ii = index.get(index.size() - 2).x + 1; ii <= i; ii++) {
                            tempPolyPoints.add(r.get(ii));
                        }

                        tempPolyPoints.add(tempP);

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

                        weight.add(new Double(weightV));

                        result.add(new Polygon(tempPolyPoints));
                    }
                }


            }
        }

        return result;


    }

    private ArrayList<PolygonNew> getPolygonNew(ArrayList<Point> r, ArrayList<Point> s) {

        ArrayList<PolygonNew> result = new ArrayList<PolygonNew>();

        weight = new ArrayList<Double>();
        double lengthR = getLength(r);
        double lengthS = getLength(s);

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

                if (x >= r1x && x <= r2x && y >= r1y && y <= r2y && x >= s1x && x < s2x && y >= s1y && y <= s2y) { //验证是否是回环
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
                        ArrayList<Point> up = new ArrayList<Point>();
                        ArrayList<Point> low = new ArrayList<Point>();

                        for (int ii = 0; ii <= i; ii++) {
                            up.add(r.get(ii));
                        }

                        up.add(tempP);

                        for (int ii = 0; ii <= j; ii++) {
                            low.add(s.get(ii));
                        }

                        low.add(tempP);//为什么要加两个？因为要分别算两个折线的距离

                        ArrayList<Point> tempR = new ArrayList<Point>();
                        for (int ii = 0; ii <= i; ii++) { //好像少了一个交点，不能成为周长？
                            tempR.add(r.get(ii));
                        }

                        ArrayList<Point> tempS = new ArrayList<Point>();
                        for (int ii = 0; ii <= j; ii++) {
                            tempS.add(s.get(ii));
                        }

                        double lengthRSub = getLength(up);//改下：（up）?
                        double lengthSSub = getLength(low);//改下：（low）?

                        //double lengthRSub=getLength(tempR);//改下：（up）?
                        //double lengthSSub=getLength(tempS);//改下：（down）?

                        double weightV = (lengthRSub + lengthSSub) / (lengthR + lengthS);

                        weight.add(new Double(weightV));
                        result.add(new PolygonNew(up, low));// 这里up和Low有重复点?MayBe
                    } else {
                        ArrayList<Point> up = new ArrayList<>();
                        ArrayList<Point> low = new ArrayList<>();

                        up.add(intersections.get(intersections.size() - 2));// the last intersection上一个

                        for (int ii = index.get(index.size() - 2).x + 1; ii <= i; ii++) {
                            up.add(r.get(ii));
                        }

                        up.add(tempP);//刚刚加入进来的交点（new）

                        low.add(intersections.get(intersections.size() - 2));//找到上一个交点
                        for (int ii = index.get(index.size() - 2).y + 1; ii <= j; ii++) {//找到交点的下一个坐标点，开始遍历
                            low.add(s.get(ii));
                        }
                        low.add(tempP);

                        ArrayList<Point> tempR = new ArrayList<Point>();
                        for (int ii = index.get(index.size() - 2).x + 1; ii <= i; ii++) {
                            tempR.add(r.get(ii));
                        }

                        ArrayList<Point> tempS = new ArrayList<Point>();
                        for (int ii = index.get(index.size() - 2).y + 1; ii <= j; ii++) {
                            tempS.add(s.get(ii));
                        }

                        double lengthRSub = getLength(up); //why not the up and low??? yeah
                        double lengthSSub = getLength(low);

                        double weightV = (lengthRSub + lengthSSub) / (lengthR + lengthS);

                        weight.add(new Double(weightV));

                        result.add(new PolygonNew(up, low));
                    }
                    //for(int ii=0;ii<)
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

class twoInt {
    int x;
    int y;

    public twoInt(int i, int j) {
        x = i;
        y = j;
    }
}