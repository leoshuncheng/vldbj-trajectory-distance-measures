package team.dig.vtdm.distance;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

import java.util.ArrayList;

/**
 * Created by LeoSC on 2018/12/16.
 */
public class MergeDistanceCalculator implements SequenceDistanceCalculator {

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
        return getMerge(r_clone, s_clone);
    }

    private double getMerge(ArrayList<Point> r, ArrayList<Point> s) {
        double[][] A = new double[r.size() + 1][s.size() + 1];
        double[][] B = new double[r.size() + 1][s.size() + 1];
        boolean[][] isA = new boolean[r.size()][s.size()];
        boolean[][] isB = new boolean[r.size()][s.size()];
        for (int i = 0; i < r.size(); i++) {
            for (int j = 0; j < s.size(); j++) {
                isA[i][j] = false;
                isB[i][j] = false;
            }
        }
        // Initialize a
        for (int j = 2; j <= s.size(); j++) {
            double sumb = 0;
            for (int k = 1; k <= j - 1; k++) {
                sumb += getEuDistance(s.get(k - 1), s.get(k));
            }
            A[1][j] = sumb + getEuDistance(s.get(j - 1), r.get(0));
        }
        A[1][1] = getEuDistance(s.get(0), r.get(0));
        // Initialize b
        for (int i = 2; i <= r.size(); i++) {
            double suma = 0;
            for (int k = 1; k <= i - 1; k++) {
                suma += getEuDistance(r.get(k - 1), r.get(k));
            }
            B[i][1] = suma + getEuDistance(r.get(i - 1), s.get(0));
        }
        B[1][1] = getEuDistance(r.get(0), s.get(0));

        for (int i = 2; i <= r.size(); i++) {
            A[i][1] = Math.min((A[i - 1][1] + getEuDistance(r.get(i - 2), r.get(i - 1))),
                    (B[i - 1][1] + getEuDistance(s.get(0), r.get(i - 1))));
        }

        for (int j = 2; j <= s.size(); j++) {
            B[1][j] = Math.min((A[1][j - 1] + getEuDistance(r.get(0), s.get(j - 1))),
                    (B[1][j - 1] + getEuDistance(s.get(j - 2), s.get(j - 1))));
        }

        for (int i = 0; i < s.size(); i++) {
            isA[0][i] = true;
            isB[0][i] = true;
        }

        for (int i = 0; i < r.size(); i++) {
            isB[i][0] = true;
            isA[i][0] = true;
        }

        while ((!isalldone(isA)) || (!isalldone(isB))) {
            for (int i = 2; i <= r.size(); i++) {
                for (int j = 2; j <= s.size(); j++) {
                    if (isA[i - 2][j - 1] && isB[i - 2][j - 1]) {
                        A[i][j] = Math.min((A[i - 1][j] + getEuDistance(r.get(i - 2), r.get(i - 1))),
                                (B[i - 1][j] + getEuDistance(s.get(j - 1), r.get(i - 1))));
                        isA[i - 1][j - 1] = true;
                     } else continue;
                }
            }

            for (int i = 2; i <= r.size(); i++) {
                for (int j = 2; j <= s.size(); j++) {
                    if (isA[i - 1][j - 2] && isB[i - 1][j - 2]) {
                        B[i][j] = Math.min((A[i][j - 1] + getEuDistance(r.get(i - 1), s.get(j - 1))),
                                (B[i][j - 1] + getEuDistance(s.get(j - 2), s.get(j - 1))));
                        isB[i - 1][j - 1] = true;
                    } else continue;
                }
            }
        }

        double merge = Math.min(A[r.size()][s.size()], B[r.size()][s.size()]);

        double sumLenth = getLength(r) + getLength(s);
        double MergeDistance = ((2.0 * merge) - sumLenth) / sumLenth;
        return MergeDistance;
    }

    private boolean isalldone(boolean[][] a) {
        boolean flag = true;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (!a[i][j]) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
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

    private double getLength(ArrayList<Point> p) {
        double result = 0;

        EuclideanDistanceCalculator e = new EuclideanDistanceCalculator();

        for (int i = 0; i < p.size() - 1; i++) {
            result += e.getDistance(p.get(i), p.get(i + 1));
        }

        return result;
    }
}
