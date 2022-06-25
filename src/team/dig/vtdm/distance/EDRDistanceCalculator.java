package team.dig.vtdm.distance;

import java.util.ArrayList;
import java.util.List;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

public class EDRDistanceCalculator implements SequenceDistanceCalculator
{

	double Threshold;

	public EDRDistanceCalculator(double threshold)
	{
		Threshold = threshold;
	}

	@Override
	public double getDistance(ArrayList<Point> r, ArrayList<Point> s){
		 // make sure the original objects will not be changed
		ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
		ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
		
		return getEDR(r_clone, s_clone);
	}
	
	private ArrayList<Point> normalization(List<Point> t)
	{
		ArrayList<Point> result = new ArrayList<Point>();

		if (t.size() == 0)
		{
			return result;
		}

		double[] mean = new double[t.get(0).dimension];
		for (int i = 0; i < t.size(); i++)
		{
			for (int j = 0; j < t.get(0).dimension; j++)
			{
				mean[j] += t.get(i).coordinate[j];
			}
		}
		for (int i = 0; i < mean.length; i++)
		{
			mean[i] /= t.size();
		}

		double[] standardDeviation = new double[t.get(0).dimension];
		for (int i = 0; i < t.size(); i++)
		{
			for (int j = 0; j < t.get(0).dimension; j++)
			{
				standardDeviation[j] += Math.pow(t.get(i).coordinate[j]
				        - mean[j], 2);
			}
		}
		for (int i = 0; i < standardDeviation.length; i++)
		{
			standardDeviation[i] = Math.sqrt(standardDeviation[i]);
		}

		for (int i = 0; i < t.size(); i++)
		{

			double[] tempCoordinate = t.get(i).coordinate;
			for (int j = 0; j < t.get(0).dimension; j++)
			{
				tempCoordinate[j] = (tempCoordinate[j] - mean[j])
				        / standardDeviation[j];
			}
			Point temp = new Point(tempCoordinate);
			result.add(temp);
		}

		return result;
	}

	private double getEDR(ArrayList<Point> rO, ArrayList<Point> sO)
	{

		// ArrayList<Point> r = normalization(rO);
		// ArrayList<Point> s = normalization(sO);

		ArrayList<Point> r = (ArrayList<Point>) rO;
		ArrayList<Point> s = (ArrayList<Point>) sO;

		double[][] edrMetric = new double[r.size() + 1][s.size() + 1];

		for (int i = 0; i <= r.size(); i++)
		{
			edrMetric[i][0] = i;
		}
		for (int i = 0; i <= s.size(); i++)
		{
			edrMetric[0][i] = i;
		}

		edrMetric[0][0] = 0;

		for (int i = 1; i <= r.size(); i++)
		{
			for (int j = 1; j <= s.size(); j++)
			{
				edrMetric[i][j] = min(
				        edrMetric[i - 1][j - 1]
				                + subcost(r.get(i - 1), s.get(j - 1)),
				        edrMetric[i][j - 1] + 1, edrMetric[i - 1][j] + 1);
			}
		}

		return edrMetric[r.size()][s.size()];

	}

	private int subcost(Point p1, Point p2)
	{
		return (p1.distanceTo(p2) < Threshold) ? 0 : 1;
		// boolean isSame = true;
		// for (int i = 0; i < p1.dimension; i++)
		// {
		// if (Math.abs(p1.coordinate[i] - p2.coordinate[i]) > Threshold)
		// {
		// isSame = false;
		//
		// }
		// }
		//
		// /*
		// * for (int i = 0; i < p1.dimension; i++) { if
		// * (Math.abs(p1.coordinate[i] - p2.coordinate[i]) != 0) { isSame =
		// * false;
		// *
		// * } }
		// */
		//
		// if (isSame)
		// {
		// return 0;
		// }
		// return 1;
	}

	private double min(double a, double b, double c)
	{
		if (a <= b && a <= c)
		{
			return a;
		}
		else if (b <= c)
		{
			return b;
		}
		else
		{
			return c;
		}
	}

	public String toString()
	{
		return "EDR";
	}
}
