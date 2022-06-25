/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team.dig.vtdm.transformation;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;

/**
 * @author uqhsu1
 */
public class WholeTrajectoryTranslationTransformation implements TransformationInterface {
    double reductionRatio = 0.5;

    public WholeTrajectoryTranslationTransformation() {}

    public WholeTrajectoryTranslationTransformation(double ReductionRatio) {
        reductionRatio = ReductionRatio;
    }

    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {
        return null;
    }

    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        ArrayList<Point> result = new ArrayList<Point>();

        assert (((Point) (list.get(0))).dimension == 2);

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                result.add((Point) list.get(i));
            } else {
                double lengthX = (((Point) list.get(i)).coordinate[0] - ((Point) list.get(0)).coordinate[0]) * reductionRatio;
                double lengthY = (list.get(i).coordinate[1] - list.get(0).coordinate[1]) * reductionRatio;
                double[] cood = new double[]{((Point) list.get(0)).coordinate[0] + lengthX, ((Point) list.get(0)).coordinate[1] + lengthY};
                Point temp = new Point(cood, ((Point) list.get(i)).timeStamp);
                result.add(temp);
            }
        }

        return result;
    }

    private Point getShiftPoint(Point p, double R, double C) {

        double[] cosC = new double[p.dimension];
        for (int i = 0; i < cosC.length; i++) {
            cosC[i] = R * (Math.cos(C * i % 31));
        }

        double[] temp = new double[p.dimension];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = p.coordinate[i] + cosC[i];
        }
        Point result = new Point(temp, p.timeStamp);

        return result;
    }

}
