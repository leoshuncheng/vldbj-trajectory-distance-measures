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
public class DeletePointTransformation implements TransformationInterface {
    public double deleteRate = 0.1;

    public DeletePointTransformation() {
    }

    public DeletePointTransformation(double DeletePointRate) {
        deleteRate = DeletePointRate;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        return null;
    }

    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {//escapeList¹Ç¼Ü½Úµã
        ArrayList<Point> Result = new ArrayList<>();

        int deleteCount = (int) (list.size() * deleteRate);

        if (list.size() - deleteCount <= escapeList.size()) {
            return escapeList;
        }

        double[] value = new double[list.size()];
        boolean[] mark = new boolean[list.size()];

        for (int i = 0; i < list.size(); i++) {
            value[i] = Math.random();
            mark[i] = false;
        }

        for (int i = 0; i < list.size(); i++) {
            Point temp = (Point) list.get(i);
            for (int j = 0; j < escapeList.size(); j++) {
                if (temp.isSame(escapeList.get(j))) {
                    value[i] = -1;
                }
            }
        }

        int[] deleteList = topN(list.size(), deleteCount, value);

        for (int j : deleteList) {
            mark[j] = true;
        }

        for (int i = 0; i < mark.length; i++) {
            if (!mark[i]) {
                Result.add(list.get(i));
            }
        }

        return Result;
    }

    private int[] topN(int allSize, int N, double[] valueList) {
        int[] result = new int[N];

        int[] allSizeList = sort(valueList);

        for (int i = 0; i < N; i++) {
            result[i] = allSizeList[i];
        }

        for (int i = 0; i < N; i++) {
            int min = result[i];
            int minIndex = i;
            for (int j = i + 1; j < N; j++) {
                if (min > result[j]) {
                    min = result[j];
                    minIndex = j;
                }
            }
            int temp = result[i];
            result[i] = min;
            result[minIndex] = temp;
        }

        return result;
    }

    /**
     * Sort from large to small, output index
     *
     * @param list List to be sorted
     * @return
     */
    private int[] sort(double[] list) {
        int[] result = new int[list.length];
        boolean[] mark = new boolean[list.length];

        for (int i = 0; i < mark.length; i++) {
            mark[i] = true;
            result[i] = -1;
        }
        int count = 0;
        for (int i = 0; i < list.length; i++) {
            double max = -1;
            int index = -1;
            for (int j = 0; j < list.length; j++) {
                if (mark[j]) {
                    if (max == -1) {
                        max = list[j];
                        index = j;
                    } else if (max < list[j]) {
                        max = list[j];
                        index = j;
                    }
                }
            }
            mark[index] = false;
            result[count] = index;
            count++;
        }
        return result;
    }
}
