package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

public class ERPDistanceCalculator implements SequenceDistanceCalculator
{
	
	@Override
	public double getDistance(ArrayList<Point> r, ArrayList<Point> s){
		 // make sure the original objects will not be changed
		ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
		ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
		
		return getERP(r_clone, s_clone); 
	}

	private ArrayList<Point> normalization(ArrayList<Point> t)
	{
		ArrayList<Point> result = new ArrayList<Point>();

		if (t.size() == 0)
		{
			return result;
		}
		assert (t.get(0).dimension == 1);

		double mean = 0;
		for (int i = 0; i < t.size(); i++)
		{
			mean += t.get(i).coordinate[0];
		}
		mean /= t.size();

		double standardDeviation = 0;
		for (int i = 0; i < t.size(); i++)
		{
			standardDeviation += Math.pow(t.get(i).coordinate[0] - mean, 2);
		}
		standardDeviation = Math.sqrt(standardDeviation);

		for (int i = 0; i < t.size(); i++)
		{

			double[] tempCoordinate = t.get(i).coordinate;
			tempCoordinate[0] = (tempCoordinate[0] - mean) / standardDeviation;//只对x做了normalization,需要改进
			
			Point temp = new Point(tempCoordinate);
			result.add(temp);
		}

		return result;
	}

	private double getERP(ArrayList<Point> rO, ArrayList<Point> sO)
	{

		ArrayList<Point> r = normalization(rO);
		ArrayList<Point> s = normalization(sO);

		double[][] erpMetric = new double[r.size() + 1][s.size() + 1];

		double rAB = 0;
		for (int i = 0; i < r.size(); i++)
		{
			rAB += Math.abs(r.get(i).coordinate[0]);
		}

		double sAB = 0;
		for (int i = 0; i < s.size(); i++)
		{
			sAB += Math.abs(s.get(i).coordinate[0]);
		}

		for (int i = 0; i <= r.size(); i++)
		{
			erpMetric[i][0] = sAB;
		}
		for (int i = 0; i <= s.size(); i++)
		{
			erpMetric[0][i] = rAB;
		}

		erpMetric[0][0] = 0;

		for (int i = 1; i <= r.size(); i++)
		{
			for (int j = 1; j <= s.size(); j++)
			{
				erpMetric[i][j] = min(
				        erpMetric[i - 1][j - 1]
				                + subcost(r.get(i - 1), s.get(j - 1)),
				        erpMetric[i - 1][j]
				                + Math.abs(r.get(i - 1).coordinate[0]),
				        erpMetric[i][j - 1]
				                + Math.abs(s.get(j - 1).coordinate[0]));
			}
		}

		return erpMetric[r.size()][s.size()];
	}

	private double min(double a, double b, double c)
	{
		if (a <= b && a <= c)
		{
			return a;
		}
		if (b <= c)
		{
			return b;
		}
		return c;
	}

	private double subcost(Point p1, Point p2)
	{
		return Math.abs(p1.coordinate[0] - p2.coordinate[0]);
	}

	public String toString()
	{
		return "ERP";
	}
}
