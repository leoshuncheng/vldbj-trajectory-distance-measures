package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

/**
 * Dynamic Time Warping distance measure.
 * 
 * @author uqhsu1, h.wang16, uqdalves
 *
 */
public class DTWDistanceCalculator implements SequenceDistanceCalculator
{

	@Override
	public double getDistance(ArrayList<Point> r, ArrayList<Point> s){
		 // make sure the original objects will not be changed
		ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
		ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
		
		return getDTW(r_clone, s_clone); 
	}

	private double getDTW(ArrayList<Point> r, ArrayList<Point> s)
	{
		if (r.size() > s.size() || r.size() < s.size())
		{
			int i = -1;
		}
		double[][] dist = new double[r.size() + 1][s.size() + 1];

		// initialize the dynamic programming seeds
		for (int i = 0; i <= r.size(); ++i)
		{
			dist[i][0] = Double.MAX_VALUE;
		}
		for (int j = 0; j <= s.size(); ++j)
		{
			dist[0][j] = Double.MAX_VALUE;
		}
		dist[r.size()][s.size()] = 0;

		// state transition
		EuclideanDistanceCalculator pdc = new EuclideanDistanceCalculator();
		for (int i = r.size() - 1; i >= 0; --i)  //倒过来算的，所以下面是+操作
		{
			for (int j = s.size() - 1; j >= 0; --j)
			{
				Point rp = r.get(i);
				Point sp = s.get(j);
				double edd = pdc.getDistance(rp, sp);
				double temp = edd
				        + Math.min(dist[i + 1][j + 1],
				                Math.min(dist[i + 1][j], dist[i][j + 1]));
				dist[i][j] = temp;
			}
		}

		return dist[0][0];
	}

	public String toString()
	{
		return "DTW";
	}
}
