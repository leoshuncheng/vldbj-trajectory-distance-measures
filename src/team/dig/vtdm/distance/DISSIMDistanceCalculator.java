package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

/**
* Dissimilarity distance measure.
*
* @author uqdalves
*/
public class DISSIMDistanceCalculator implements SequenceDistanceCalculator {
	private static double INFINITY = Double.MAX_VALUE;
	private static long TIME_INCREMENT = 1000; // 1000 Milliseconds = 1 second
/*	
	public static void main(String[] args) {
        Point p1 = new Point(new double[]{0,0}, 0);
        Point p2 = new Point(new double[]{0,10}, 10);
        Point p3 = new Point(new double[]{0,12}, 12);
        ArrayList<Point> r =  new ArrayList<>();
        r.add(p1); r.add(p2); r.add(p3);
        
        Point p4 = new Point(new double[]{2,0}, 0);
        Point p5 = new Point(new double[]{2,7}, 7);
        Point p6 = new Point(new double[]{2,10}, 10);
        ArrayList<Point> s =  new ArrayList<>();
        s.add(p4); s.add(p5); s.add(p6);
        
        double dissim = getDISSIM(r, s, 0, 10);
        
        System.out.println("DISSIM: " + dissim);
    }
*/

	@Override
	public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
		// make sure the original objects will not be changed
		ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
		ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
		
		// Time range (parameters - given)
		long t1 = getTimeIni(r_clone, s_clone);
		long tn = getTimeEnd(r_clone, s_clone);

		return getDISSIM(r_clone, s_clone, t1, tn);
	}
	
	
	/**
	 * Dissimilarity distance between two moving objects.
	 */
	private static double getDISSIM(ArrayList<Point> r, ArrayList<Point> s, long t1, long tn){
		if(r.size() < 2 || s.size() < 2){
			return INFINITY;
		}
		
		ArrayList<Double> dist_t = new ArrayList<Double>();
		int index_r = 0, index_s = 0;

		for(long t = t1; t <= tn; t += TIME_INCREMENT){
			Point r_p1 = r.get(index_r);
			Point r_p2 = r.get(index_r + 1);
			Point s_p1 = s.get(index_s);
			Point s_p2 = s.get(index_s + 1);
			
			// get the 'distance' between the two trajectories at time t
			dist_t.add(getDistance(r_p1, r_p2, s_p1, s_p2, t));
			
			// current time reached the time of the next point
			if(t > r_p2.timeStamp && index_r < r.size()-2){
				index_r++;
			}
			if(t > s_p2.timeStamp && index_s < s.size()-2){
				index_s++;
			}
		}
		
		double dissim = 0;
		for(int i = 0; i < dist_t.size()-1; i++) {
			dissim += (dist_t.get(i) + dist_t.get(i + 1)) * TIME_INCREMENT;
		}
		
		return dissim / 2;
	}

	/**
	 * Euclidean distance between two points moving with linear
	 * functions of time between consecutive time-stamps. Using
	 * factors of the trinomials a, b and c
	 */
	private static double getDistance(Point r_p1, Point r_p2, Point s_p1, Point s_p2, long time){
		// get the factors
		double a = getA(r_p1, r_p2, s_p1, s_p2);
		double b = getB(r_p1, r_p2, s_p1, s_p2);
		double c = getC(r_p1, r_p2, s_p1, s_p2);
		
		double dist = Math.sqrt(a * Math.pow(time, 2) + b * time + c);
		
		return dist;
	}

	/**
	 *  Get initial time period t1
	 */
	private long getTimeIni(ArrayList<Point> r, ArrayList<Point> s){
		// Get the trajectory with latest first point
		long t1 = s.get(0).timeStamp > r.get(0).timeStamp ?
				s.get(0).timeStamp : r.get(0).timeStamp;
		return t1;
	}
	
	/**
	 *  Get final time period tn
	 */
	private long getTimeEnd(ArrayList<Point> r, ArrayList<Point> s){
		// Get the trajectory with earliest last point
		long tn = s.get(s.size()-1).timeStamp < r.get(r.size()-1).timeStamp ?
				s.get(s.size()-1).timeStamp : r.get(r.size()-1).timeStamp;
		return tn;
	}
	
	/**
	 * Calculate the factors of the trinomial A, B and C
	 */
	private static double getA(Point p_p1, Point p_p2, Point q_p1, Point q_p2){
		double a1, a2;
		a1 = q_p2.coordinate[0] - q_p1.coordinate[0] - 
			 p_p2.coordinate[0] + p_p1.coordinate[0];		
		a2 = q_p2.coordinate[1] - q_p1.coordinate[1] - 
			 p_p2.coordinate[1] + p_p1.coordinate[1];
				
		double a = Math.pow(a1,2) + Math.pow(a2,2);
		return a;
	}
	private static double getB(Point p_p1, Point p_p2, Point q_p1, Point q_p2){
		double b1, b2, b3, b4;
		b1 = q_p2.coordinate[0] - q_p1.coordinate[0] - 
			 p_p2.coordinate[0] + p_p1.coordinate[0];
		b2 = q_p1.coordinate[0] - p_p1.coordinate[0];
		b3 = q_p2.coordinate[1] - q_p1.coordinate[1] - 
			 p_p2.coordinate[1] + p_p1.coordinate[1];
		b4 = q_p1.coordinate[1] - p_p1.coordinate[1];
				
		double b = 2 * (b1 * b2 + b3 * b4);
		return b;
	}
	private static double getC(Point p_p1, Point p_p2, Point q_p1, Point q_p2){
		double c1, c2;
		c1 = q_p1.coordinate[0] - p_p1.coordinate[0];
		c2 = q_p1.coordinate[1] - p_p1.coordinate[1];
		
		double c = Math.pow(c1,2) + Math.pow(c2,2);
		return c;
	}
	
	@Override
	public String toString() {
		return "DISSIM";
	}
}
