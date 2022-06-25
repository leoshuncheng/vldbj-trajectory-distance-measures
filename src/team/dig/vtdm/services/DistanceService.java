package team.dig.vtdm.services;

import java.util.ArrayList;
import java.util.Collections;

import team.dig.vtdm.distance.*;
import team.dig.vtdm.entities.Point;
import team.dig.vtdm.entities.Trajectory;

/**
 * Service (proxy) to put together all the distance functions.
 *
 * @author uqdalves
 */
public class DistanceService {
    public String TRANSF_NAME = "";

    // EDwP Distance
    public void EDwP(ArrayList<Trajectory> testList, int k) {
        EDwPDistanceCalculator EDwP = new EDwPDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) {
                    continue;
                }
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();

                distancesList.add(EDwP.getDistance(r, s));
            }
            Collections.sort(distancesList);
            // k = 50
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));// list * k组
            }
        }
        FileService.saveDistanceFile(topKDis, "EDwP_" + k + TRANSF_NAME, k);
    }

    // DTW Distance
    public void DTW(ArrayList<Trajectory> testList, int k) {
        DTWDistanceCalculator DTW = new DTWDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) {
                    continue;
                }
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(DTW.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "DTW_" + k + TRANSF_NAME, k);
    }

    // ERP Distance
    public void ERP(ArrayList<Trajectory> testList, int k) {
        ERPDistanceCalculator ERP = new ERPDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(ERP.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "ERP_" + k + TRANSF_NAME, k);
    }

    // TID Distance
    public void TID(ArrayList<Trajectory> testList, int k) {
        TIDDistanceCalculator TID = new TIDDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(TID.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "TID_" + k + TRANSF_NAME, k);
    }

    // OWD Distance
    public void OWD(ArrayList<Trajectory> testList, int k) {
        OWDDistanceCalculator OWD = new OWDDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(OWD.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "OWD_" + k + TRANSF_NAME, k);
    }

    // STLIP Distance
    public void STLIP(ArrayList<Trajectory> testList, int k) {
        STLIPDistanceCalculator STLIP = new STLIPDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                double STLIPvalue = STLIP.getDistance(r, s);
                if (STLIPvalue == 0.0) continue;
                distancesList.add(STLIP.getDistance(r, s));
            }

            if (distancesList.size() < k)
                continue;

            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "STLIP_" + k + TRANSF_NAME, k);
    }

    // PDTW Distance
    public void PDTWD(ArrayList<Trajectory> testList, int k) {
        PDTWDistanceCalculator PDTW = new PDTWDistanceCalculator(5);  // threshold 5
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(PDTW.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "PDTW_" + k + TRANSF_NAME, k);
    }

    // LCSS Distance
    public void LCSS(ArrayList<Trajectory> testList, double threshold, int k) {
        LCSSDistanceCalculator LCSS = new LCSSDistanceCalculator(threshold);
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(LCSS.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "LCSS_" + k + TRANSF_NAME, k);
    }

    // Frechet Distance
    public void Frechet(ArrayList<Trajectory> testList, int k) {
        FrechetDistanceCalculator FRECH = new FrechetDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(FRECH.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "FRECHET_" + k + TRANSF_NAME, k);
    }

    // STED Distance
    public void STED(ArrayList<Trajectory> testList, int k) {
        STEDDistanceCalculator STED = new STEDDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                double STEDValue = STED.getDistance(r, s);
                if (STEDValue == 0.0) continue;
                distancesList.add(STEDValue);
            }

            if (distancesList.size() < k)
                continue;

            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "STED_" + k + TRANSF_NAME, k);
    }

    // LIP Distance
    public void LIPD(ArrayList<Trajectory> testList, int k) {
        LIPDistanceCalculator LIP = new LIPDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            //System.out.println(i);
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                double LIPvalue = LIP.getDistance(r, s);
                if (LIPvalue == 0.0) continue;
                distancesList.add(LIPvalue);
            }

            if (distancesList.size() < k)
                continue;

            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "LIP_" + k + TRANSF_NAME, k);
    }

    // EDR Distance
    public void EDR(ArrayList<Trajectory> testList, double threshold, int k) {
        EDRDistanceCalculator EDR = new EDRDistanceCalculator(threshold);
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(EDR.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "EDR_" + k + TRANSF_NAME, k);

    }

    // STLCSS with SIGMOID Distance
    public void STLCSS_SIGMOID(ArrayList<Trajectory> testList,
                                 double distanceThreshold, long timeIntervalThreshold, int k) {
        STLCSSWithSigmoidDistanceCalculator STLCSS_SIG =
                new STLCSSWithSigmoidDistanceCalculator(distanceThreshold, timeIntervalThreshold);
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(STLCSS_SIG.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));// list*k组
            }
        }
        String d = distanceThreshold == 0.01 ? "01" : "0004";
        FileService.saveDistanceFile(topKDis, "STLCSS-SIGMOID_" + d + "_" + k + TRANSF_NAME, k);

    }

    // STLCSS Distance
    public void STLCSS(ArrayList<Trajectory> testList,
                         double distanceThreshold, long timeIntervalThreshold, int k) {
        STLCSSDistanceCalculator STLCSS =
                new STLCSSDistanceCalculator(distanceThreshold, timeIntervalThreshold);
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(STLCSS.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "STLCSS_" + k + TRANSF_NAME, k);

    }

    // Euclidean Distance
    public void Euclidean(ArrayList<Trajectory> testList, int k) {
        EuclideanDistanceForMultiDimensionTimeSeriesCalculator EUCLIDEAN =
                new EuclideanDistanceForMultiDimensionTimeSeriesCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(EUCLIDEAN.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "EUCLIDEAN_" + k + TRANSF_NAME, k);
    }

    // Hausdorff Distance
    public void Hausdorff(ArrayList<Trajectory> testList, int k) {
        HausdorffDistanceCalculator hausdorff =
                new HausdorffDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(hausdorff.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "HAUSDORFF_" + k + TRANSF_NAME, k);
    }

    // TSJoin Distance
    public void TSJoin(ArrayList<Trajectory> testList, double lamda, int k) {
        TSJoinDistanceCalculator ts =
                new TSJoinDistanceCalculator(lamda);//[0,1]
        ArrayList<Double> topKDis = new ArrayList<>();//
        int listsize = testList.size();
        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(ts.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));//list*k组
            }
        }
        FileService.saveDistanceFile(topKDis, "TSJOIN_" + k + TRANSF_NAME, k);

    }

    // Merge Distance
    public void Merge(ArrayList<Trajectory> testList, int k) {
        MergeDistanceCalculator md =
                new MergeDistanceCalculator();
        ArrayList<Double> topKDis = new ArrayList<>();
        int listsize = testList.size();

        for (int i = 0; i < listsize; i++) {
            ArrayList<Double> distancesList = new ArrayList<>();
            for (int j = 0; j < listsize; j++) {
                if (i == j) continue;
                ArrayList<Point> r = testList.get(i).getPointsList();
                ArrayList<Point> s = testList.get(j).getPointsList();
                distancesList.add(md.getDistance(r, s));
            }
            Collections.sort(distancesList);
            for (int n = 0; n < k; n++) {
                topKDis.add(distancesList.get(n));// list * k组
            }
        }
        FileService.saveDistanceFile(topKDis, "MERGE_" + k + TRANSF_NAME, k);
    }

    /**
     * Run all the distance functions for the given trajectories lists.
     * Top K Distances.
     * Calculates the distance in the sample list with every trajectory.
     * Save the outcomes to files.
     */
    public void runDistanceFunctions(ArrayList<Trajectory> testList, int k) {

        System.out.println("Run distance functions");

        System.out.println("<EDwP>");
        EDwP(testList, k);

        System.out.println("<DTW>");
        DTW(testList, k);

        System.out.println("<ERP>");
        ERP(testList, k);

        System.out.println("<OWD>");
        OWD(testList, k);

        System.out.println("<STLIP>");
        STLIP(testList, k);

        System.out.println("<PDTW>");
        PDTWD(testList, k);

        System.out.println("<FRECHET>");
        Frechet(testList, k);

        System.out.println("<STED>");
        STED(testList, k);

        System.out.println("<LIPD>");
        LIPD(testList, k);

        System.out.println("<EDR>");
        EDR(testList, 0.01, k);

        System.out.println("<LCSS>");
        LCSS(testList, 0.01, k);

        System.out.println("<STLCSS>");
        STLCSS(testList, 0.01, 1000, k);

        System.out.println("<EUCLIDEAN>");
        Euclidean(testList, k);

        System.out.println("<TSJOIN>");
        TSJoin(testList, 0.5, k);

        System.out.println("<MERGE>");
        Merge(testList, k);

        System.gc();

        System.out.println("Run distance functions finished");
    }

    /**
     * Make a clone of this list of points.
     */
    public static ArrayList<Point> clonePointsList(ArrayList<Point> list) {
        ArrayList<Point> new_list = new ArrayList<>();

        for (Point p : list) {
            Point p_clone = p.clone();
            new_list.add(p_clone);
        }

        return new_list;
    }
}
