package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

/**
 * EDwP: Edit Distance with Projections
 *
 * @author uqdalves
 */
public class EDwPDistanceCalculator implements SequenceDistanceCalculator {
    private static double INFINITY = Double.MAX_VALUE;  //ÎÞÇî

    /*
        public static void main(String[] args) {
            Point p1 = new Point(new double[]{0,0});
            Point p2 = new Point(new double[]{0,10});
            Point p3 = new Point(new double[]{0,12});
            ArrayList<Point> r =  new ArrayList<>();
            r.add(p1); r.add(p2); r.add(p3);

            Point p4 = new Point(new double[]{2,0});
            Point p5 = new Point(new double[]{2,7});
            Point p6 = new Point(new double[]{2,10});
            ArrayList<Point> s =  new ArrayList<>();
            s.add(p4); s.add(p5); s.add(p6);

            double cost = getEDwP(r, s);

            System.out.println("Cost: " + cost);
        }
    */
    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);

        return getEDwP(r_clone, s_clone);
    }

    /**
     * Edit Distance with Projections
     */
    private static double getEDwP(ArrayList<Point> r, ArrayList<Point> s) {
        double total_cost_edwp = 0;

        if (length(r) == 0 && length(s) == 0) {
//			System.out.println(0);
            return 0;
        }
        if (length(r) == 0 || length(s) == 0) {
//			System.out.println("INFINITY");
            return INFINITY;
        }

        boolean flag = false;
        while (!flag) {
            // cost of the replacement
            double replacement = 0;
            double coverage = 0;

            // Insert and replacement
            if (s.size() == 1 && r.size() > 1) {
                Point e_p1 = r.get(0);
                Point e_p2 = r.get(1);
                Point p = s.get(0);

                replacement = replacement(e_p1, e_p2, p, p);
                coverage = coverage(e_p1, e_p2, p, p);
            }
            // Insert and replacement
            else if (r.size() == 1 && s.size() > 1) {
                Point e_p1 = s.get(0);
                Point e_p2 = s.get(1);
                Point p = r.get(0);

                replacement = replacement(e_p1, e_p2, p, p);
                coverage = coverage(e_p1, e_p2, p, p);
            }
            // Insert and replacement
            else if (r.size() > 1 && s.size() > 1) {
                Point e1_p1 = r.get(0);
                Point e1_p2 = r.get(1);
                Point e2_p1 = s.get(0);
                Point e2_p2 = s.get(1);

                // get the coordinates of p_ins (projections)
                Point p_ins_e1 = projection(e1_p1, e1_p2, e2_p2);
                Point p_ins_e2 = projection(e2_p1, e2_p2, e1_p2);

                // test which replacement is better
                double replace_e1 = replacement(e1_p1, p_ins_e1, e2_p1, e2_p2);
                double replace_e2 = replacement(e2_p1, p_ins_e2, e1_p1, e1_p2);
                double cover_e1 = coverage(e1_p1, p_ins_e1, e2_p1, e2_p2);
                double cover_e2 = coverage(e2_p1, p_ins_e2, e1_p1, e1_p2);

                if ((cover_e1 * replace_e1) <= (cover_e2 * replace_e2)) {
                    // replacement 1 is better
                    replacement = replace_e1;
                    coverage = cover_e1;

                    // if the projection is not already there
                    if (!equals(p_ins_e1, e1_p1) && !equals(p_ins_e1, e1_p2)) {
                        r.add(1, p_ins_e1);
                    }
                } else {
                    // replacement 2 is better
                    replacement = replace_e2;
                    coverage = cover_e2;

                    // if the projection is not already there
                    if (!equals(p_ins_e2, e2_p1) && !equals(p_ins_e2, e2_p2)) {
                        s.add(1, p_ins_e2);
                    }
                }
            }
            // end
            else {
                flag = true;
            }

            r = rest(r);
            s = rest(s);
//			System.out.println("replacement:" + replacement);
//			System.out.println("coverage:" + coverage);
            total_cost_edwp += (replacement * coverage);
        }

        return total_cost_edwp;
    }

    /**
     * Cost of the operation where the segment e1 is matched with e2.
     */
    private static double replacement(Point e1_p1, Point e1_p2, Point e2_p1, Point e2_p2) {
        // Euclidean distances between segments' points
        double dist_p1 = distance(e1_p1, e2_p1);
        double dist_p2 = distance(e1_p2, e2_p2);

        // replacement cost
        double rep_cost = dist_p1 + dist_p2;

        return rep_cost;
    }

    /**
     * Coverage; quantifies how representative the segment being
     * edit are of the overall trajectory. Segment e1 and e2.
     * e1 = [e1.p1, e1.p2], e2 = [e2.p1, e2.p2]
     */
    private static double coverage(Point e1_p1, Point e1_p2, Point e2_p1, Point e2_p2) {
        // segments coverage
        double cover = distance(e1_p1, e1_p2) + distance(e2_p1, e2_p2);
        return cover;
    }

    /**
     * Compare if two points have the same coordinates
     */
    private static boolean equals(Point a, Point b) {
        for (int i = 0; i < a.dimension; i++) {
            if (a.coordinate[i] != b.coordinate[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a sub-trajectory containing all segments of
     * the list except the first one.
     */
    private static ArrayList<Point> rest(ArrayList<Point> list) {
        if (!list.isEmpty()) {
            list.remove(0);
        }
        return list;
    }

    /**
     * The length of the trajectory (sum of the segments length)
     */
    private static double length(ArrayList<Point> list) {
        double length = 0;

        for (int i = 0; i < list.size() - 1; i++) {
            length += distance(list.get(i), list.get(i + 1));
        }

        return length;
    }

    /**
     * Calculate the projection of the point p on to the segment e
     * e = [e.p1, e.p2]
     */
    private static Point projection(Point e_p1, Point e_p2, Point p) {
        // get dot product of [e.p2 - e.p1] and [p_proj - p]
        double dot_product = dotProduct(e_p1, e_p2, p);

        // get squared length of e
        double len_2 = Math.pow(e_p2.coordinate[0] - e_p1.coordinate[0], 2) +
                Math.pow(e_p2.coordinate[1] - e_p1.coordinate[1], 2);

        // Calculate the coordinates of p_proj (projection) using the
        // dot product and the squared length of e
        double x = e_p1.coordinate[0] +
                (dot_product * (e_p2.coordinate[0] - e_p1.coordinate[0])) / len_2;
        double y = e_p1.coordinate[1] +
                (dot_product * (e_p2.coordinate[1] - e_p1.coordinate[1])) / len_2;

        Point p_proj = new Point(new double[]{x, y});

        return p_proj;
    }

    /**
     * Calculates the dot product between segment e and point p.
     * e = [e.p1, e.p2]
     */
    private static double dotProduct(Point e_p1, Point e_p2, Point p) {
        // shift the points to the origin
        double shift_e[] = new double[p.dimension];
        double shift_p[] = new double[p.dimension];
        for (int i = 0; i < p.dimension; i++) {
            shift_e[i] = e_p2.coordinate[i] - e_p1.coordinate[i];
            shift_p[i] = p.coordinate[i] - e_p1.coordinate[i];
        }

        // calculate the dot product
        double dot_product = 0;
        for (int i = 0; i < p.dimension; i++) {
            dot_product += shift_e[i] * shift_p[i];
        }

        return dot_product;
    }

    /**
     * Euclidean distance between p and q
     */
    private static double distance(Point p, Point q) {
        EuclideanDistanceCalculator euclid =
                new EuclideanDistanceCalculator();
        return euclid.getDistance(p, q);
    }

    @Override
    public String toString() {
        return "EDwP";
    }
}