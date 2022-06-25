/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team.dig.vtdm.entities;

/**
 * @author uqhsu1
 */
public class Line {
    public Point startPoint;
    public Point endPoint;
    double k = 0;
    double b = 0;
    boolean isKZero = false;
    double x = 0;

    public Line() {
        startPoint = new Point();
        endPoint = new Point();
    }

    public Line(Point start, Point end) {
        startPoint = start;
        endPoint = end;

        assert (startPoint.dimension == 2);
        assert (endPoint.dimension == 2);

        if (startPoint.coordinate[0] == endPoint.coordinate[0]) {
            isKZero = true;
            x = startPoint.coordinate[0];
        } else {
            k = (endPoint.coordinate[1] - startPoint.coordinate[1]) / (endPoint.coordinate[0] - startPoint.coordinate[0]);
            b = startPoint.coordinate[1] - k * startPoint.coordinate[0];
        }
    }

    public double getYByX(double x) {
        if (isKZero) {
            return 0;
        } else {
            return x * k + b;
        }
    }

    public Point getPointByTime(double relativeTime) {
        long startTime = startPoint.timeStamp;
        long endTime = endPoint.timeStamp;

        double X = (relativeTime) * (endPoint.coordinate[0] - startPoint.coordinate[0]) / (endTime - startTime) + startPoint.coordinate[0];

        double Y = getYByX(X);

        Point p = new Point(new double[]{X, Y}, (long) (startTime + relativeTime));

        return p;
    }

    /**
     * Distance from point to line
     *
     * @param Point p
     */
    public double getPointLineDistance(Point p) {
        double result = 0;
        if (isKZero) {
            return Math.abs(p.coordinate[0] - x);
        } else {
            result = Math.abs(k * p.coordinate[0] + (-1) * p.coordinate[1] + b);
            result = result / (Math.sqrt(k * k + 1));

        }
        return result;
    }

    /**
     * @param Line l
     * @return Point
     */
    public Point getIntersection(Line l) {
        if (isKZero && l.isKZero) {
            return null;
        } else if (isKZero) {

            double[] temp = new double[2];
            temp[0] = x;
            temp[1] = l.k * x + l.b;
            return new Point(temp);
        } else if (l.isKZero) {
            double[] temp = new double[2];
            temp[0] = l.x;
            temp[1] = k * l.x + b;
        } else if (k == l.k) {
            return null;
        }

        double[] temp = new double[2];
        temp[0] = (l.b - b) / (k - l.k);
        temp[1] = k * temp[0] + b;

        Point p = new Point(temp);

        if (isInLine(p) && l.isInLine(p)) {
            return p;
        }

        return null;
    }

    public boolean isInLine(Point p) {
        double sx = startPoint.coordinate[0];
        double sy = startPoint.coordinate[1];

        double ex = endPoint.coordinate[0];
        double ey = endPoint.coordinate[1];

        if (sx > ex) {
            double temp = sx;
            sx = ex;
            ex = temp;
        }

        if (sy > ey) {
            double temp = sy;
            sy = ey;
            ey = temp;
        }

        double x = p.coordinate[0];
        double y = p.coordinate[1];

        if (x >= sx && x < ex && y >= sy && y < ey) {
            return true;
        }

        return false;
    }

    public boolean isInLineX(double xx) {
        double sx = startPoint.coordinate[0];
        double sy = startPoint.coordinate[1];

        double ex = endPoint.coordinate[0];
        double ey = endPoint.coordinate[1];

        if (sx > ex) {
            double temp = sx;
            sx = ex;
            ex = temp;
        }

        if (sy > ey) {
            double temp = sy;
            sy = ey;
            ey = temp;
        }
        if (xx >= sx && xx < ex) {
            return true;
        }

        return false;
    }

}
