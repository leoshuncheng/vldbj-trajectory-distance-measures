package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

public class LCSSDistanceCalculator implements SequenceDistanceCalculator
{

	double Threshold;
	
	@Override
	public double getDistance(ArrayList<Point> r, ArrayList<Point> s){
		 // make sure the original objects will not be changed
		ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
		ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
		
		return getLCSS(r_clone, s_clone); 
	}
	
	public LCSSDistanceCalculator(double threshold)
	{
		Threshold = threshold;
	}

	private double getLCSS(ArrayList<Point> r, ArrayList<Point> s)
	{

		double[][] LCSSMetric = new double[r.size() + 1][s.size() + 1];

		for (int i = 0; i <= r.size(); i++)
		{
			LCSSMetric[i][0] = 0;
		}
		for (int i = 0; i <= s.size(); i++)
		{
			LCSSMetric[0][i] = 0;
		}

		LCSSMetric[0][0] = 0;

		for (int i = 1; i <= r.size(); i++)
		{
			for (int j = 1; j <= s.size(); j++)
			{
				if (subcost(r.get(i - 1), s.get(j - 1)) == 0)
				{
					LCSSMetric[i][j] = LCSSMetric[i - 1][j - 1] + 1;
				}
				else
				{
					LCSSMetric[i][j] = max(LCSSMetric[i][j - 1],
					        LCSSMetric[i - 1][j]);
				}

			}
		}

		double tempR = LCSSMetric[r.size()][s.size()];

		double result = 1 - (tempR / Math.min(r.size(), s.size()));

		return (max(r.size(), s.size()) - tempR) / Math.max(r.size(), s.size());
	}

	private double max(double a, double b)
	{
		if (a >= b)
		{
			return a;
		}
		else
		{
			return b;
		}
	}

	private int subcost(Point p1, Point p2)
	{
//		boolean isSame = true;
//		for (int i = 0; i < p1.dimension; i++)
//		{
//			if (Math.abs(p1.coordinate[i] - p2.coordinate[i]) > Threshold)
//			{
//				isSame = false;
//			}
//		}
		
		EuclideanDistanceCalculator edc = new EuclideanDistanceCalculator();
		boolean isSame = true;
		if(edc.getDistance(p1, p2)>Threshold) isSame=false;

		if (isSame)
		{
			return 0;
		}
		return 1;
	}

	public String toString()
	{
		return "LCSS";
	}
}
