package team.dig.vtdm.distance;

import team.dig.vtdm.entities.Point;

public class EuclideanDistanceCalculator implements PointDistanceCalculator {

	@Override
	public double getDistance(Point x, Point y) {
		assert(x.dimension == y.dimension);
		
		double sum = 0;
		for (int i = 0; i < x.dimension; ++i) {
			double dif = x.coordinate[i] - y.coordinate[i];
			sum += dif * dif;
		}

		return Math.sqrt(sum);
	}

}
