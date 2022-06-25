package team.dig.vtdm.distance;

import java.util.ArrayList;
import java.util.List;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;
 
public class EuclideanDistanceForMultiDimensionTimeSeriesCalculator implements
        SequenceDistanceCalculator
{

	EuclideanDistanceCalculator EDC;

	public EuclideanDistanceForMultiDimensionTimeSeriesCalculator()
	{
		EDC = new EuclideanDistanceCalculator();
	}

	@Override
	public double getDistance(ArrayList<Point> r, ArrayList<Point> s){
		// make sure the original objects will not be changed
		ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
		ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
		        
		return getEDC(r_clone, s_clone); 
	}

	private double getEDC(ArrayList<Point> r, ArrayList<Point> s)
	{
		List<Point> longT = new ArrayList<Point>();
		List<Point> shortT = new ArrayList<Point>();

		if (r.size() == 0 && s.size() == 0)
		{
			return 0;
		}
		if (r.size() == 0 || s.size() == 0)
		{
			return Double.MAX_VALUE;
		}

		if (r.size() < s.size())
		{
			shortT = r;
			longT = s;
		}
		else
		{
			shortT = s;
			longT = r;
		}
		int k = shortT.size();

		double[] distanceOption = new double[longT.size() - shortT.size() + 1];

		for (int i = 0; i < distanceOption.length; i++)
		{
			double tempResult = 0;
			for (int j = 0; j < k; j++)
			{
				tempResult += EDC.getDistance(shortT.get(j), longT.get(j + i));
			}
			tempResult /= k;
			distanceOption[i] = tempResult;
		}

		return GetMin(distanceOption);

	}

	private double GetMin(double[] a)
	{
		assert (a.length > 0);

		double result = a[0];

		for (int i = 0; i < a.length; i++)
		{
			if (result > a[i])
			{
				result = a[i];
			}
		}

		return result;
	}

	public String toString()
	{
		return "ED with MultiDimension";
	}

}
