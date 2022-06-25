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
public class RamerDouglasPeuchkerAlgorithm {
    private ArrayList<Point> originalPoints;

    //threshold should be decide carefully
    private double threshold = 1;

    public RamerDouglasPeuchkerAlgorithm() {
    }

    public RamerDouglasPeuchkerAlgorithm(double t) {
        threshold = t;
    }

    public ArrayList<Point> getKeyPointArrayList(ArrayList<Point> points) {
        ArrayList<Point> Result = new ArrayList<>();
        double maxDistance = 0;

        int index = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            double distance = PerpendicularDistance((Point) points.get(0), (Point) points.get(points.size() - 1), (Point) points.get(i));

            if (distance > maxDistance) {
                index = i;
                maxDistance = distance;
            }
        }

        if (maxDistance > threshold) {
            ArrayList<Point> leftPoints = getArrayList(points, 0, index + 1);
            ArrayList<Point> rightPoints = getArrayList(points, index, points.size() - index);
            ArrayList<Point> tempLeft = getKeyPointArrayList(leftPoints);
            ArrayList<Point> tempRight = getKeyPointArrayList(rightPoints);
            Result = MergeResult(tempLeft, tempRight);
        } else {
            Result.add((Point) points.get(0));
            Result.add((Point) points.get(points.size() - 1));
        }

        return Result;
    }

    private double PerpendicularDistance(Point start, Point end, Point p) {
        double result = 0;

        assert (start.dimension == 2);
        assert (end.dimension == 2);
        assert (p.dimension == 2);

        double x1 = start.coordinate[0];
        double y1 = start.coordinate[1];
        double x2 = end.coordinate[0];
        double y2 = end.coordinate[1];
        double x3 = p.coordinate[0];
        double y3 = p.coordinate[1];

        double k = 0, b = 0;

        if (x1 == x2) {
            return Math.abs(x3 - x1);
        }

        k = (y1 - y2) / (x1 - x2);
        b = (x1 * y2 - x2 * y1) / (x1 - x2);

        double A = k, B = -1, C = b;

        result = Math.abs((A * x3 + B * y3 + C) / Math.sqrt(A * A + B * B));

        return result;
    }

    private ArrayList<Point> MergeResult(ArrayList<Point> left, ArrayList<Point> right) {
        ArrayList<Point> result = new ArrayList<Point>();
        for (int i = 0; i < left.size(); i++) {
            result.add((Point) left.get(i));
        }

        for (int i = 1; i < right.size(); i++) {
            result.add((Point) right.get(i));
        }

        return result;
    }

    private ArrayList<Point> getArrayList(ArrayList<Point> list, int start, int length) {
        ArrayList<Point> result = new ArrayList<Point>();
        for (int i = start; i < start + length; i++) {
            result.add((Point) (list.get(i)));
        }

        return result;
    }

    public void setThreshold(double t) {
        threshold = t;
    }
}
